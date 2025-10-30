package org.dows.member.biz.pay.impl;

import com.alibaba.fastjson.JSON;
import com.mybatisflex.core.query.QueryWrapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.dows.member.biz.pay.WechatPayBiz;
import org.dows.member.constant.MemberExceptionStatusCode;
import org.dows.member.entity.MemberInstanceEntity;
import org.dows.member.entity.MemberInterestsEntity;
import org.dows.member.enums.*;
import org.dows.member.exception.MemberException;
import org.dows.member.config.AliPayProperties;
import org.dows.member.biz.pay.AliPayBiz;
import org.dows.member.biz.pay.PaymentBiz;
import org.dows.member.biz.user.UserMemberChargeBiz;
import org.dows.member.exception.PayException;
import org.dows.member.request.pay.AliPayQrCodeRequest;
import org.dows.member.request.pay.PayQrCodeRequest;
import org.dows.member.request.pay.WxPayQrCodeRequest;
import org.dows.member.request.user.UserMemberChargeSaveRequest;
import org.dows.member.request.user.UserMemberChargeUpdateRequest;
import org.dows.member.response.MemberChargeGetResponse;
import org.dows.member.response.pay.AliPayStatusResponse;
import org.dows.member.response.pay.PayQrCodeResponse;
import org.dows.member.response.pay.WxPayStatusResponse;
import org.dows.member.service.MemberInstanceService;
import org.dows.member.service.MemberInterestsService;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

@Slf4j
@Component
@RequiredArgsConstructor
public class PaymentBizImpl implements PaymentBiz {

    private final MemberInstanceService memberInstanceService;
    private final MemberInterestsService memberInterestsService;
    private final UserMemberChargeBiz userMemberChargeBiz;
    private final WechatPayBiz wechatPayBiz;
    private final AliPayBiz aliPayBiz;
    private final AliPayProperties aliPayProperties;
    private final SimpMessagingTemplate messagingTemplate;
    // 轮询线程池
    private final ScheduledExecutorService pollingExecutor = Executors.newScheduledThreadPool(5);
    private final ThreadPoolTaskExecutor aliPayPollingTask;

    @Transactional
    @Override
    public PayQrCodeResponse createNativePayment(PayQrCodeRequest request) {
        // 查询会员实例
        MemberInstanceEntity oldInstance = getMemberInterestsByAccountInstanceIdAndAppId(
                request.getAccountInstanceId(),
                request.getAppId()
        );

        // 校验会员实例是否存在
        isMemberInstanceExist(oldInstance);

        // 查询是否还有待支付的充值记录，有的话则关闭
        MemberChargeGetResponse chargeGetResponse = userMemberChargeBiz.getWaitPayByAccountInstanceId(request.getAccountInstanceId());
        if (chargeGetResponse != null) {
            if (chargeGetResponse.getChannel().equals(PayChannelEnum.WX.getCode())) {
                closeWxPay(chargeGetResponse.getPayNo());
            } else if (chargeGetResponse.getChannel().equals(PayChannelEnum.ALI.getCode())){
                cancelAliPay(chargeGetResponse.getPayNo());
            }
        }

        // 查询当前缴费的会员等级，并验证是否存在及状态是否正常
        MemberInterestsEntity interests = getMemberInterestsById(request.getMemberInterestsId());

        // 校验是否可执行当前操作，并返回会员变更类型
        String chargeType = validateMemberInterest(oldInstance, interests, request);

        // 保存充值记录（充值订单）
        String memberType = MemberTypeEnum.getDescByCode(interests.getMemberType());
        MemberChargeGetResponse chargeEntity = saveMemberCharge(oldInstance,
                interests,
                request.getPayChannel(),
                chargeType,
                MemberChargeTypeEnum.getDescByCode(chargeType) + "(" + memberType + ")");

        // 调用第三方支付
        if (request.getPayChannel().equals(PayChannelEnum.WX.getCode())) {
            WxPayQrCodeRequest payQrCodeRequest = new WxPayQrCodeRequest();
            payQrCodeRequest.setOutTradeNo(chargeEntity.getPayNo());
            payQrCodeRequest.setTotalAmount(interests.getAmount());
            payQrCodeRequest.setDescription(chargeEntity.getNote());

           return wechatPayBiz.wxPayQrCode(payQrCodeRequest);
        } else if (request.getPayChannel().equals(PayChannelEnum.ALI.getCode())){
            AliPayQrCodeRequest payQrCodeRequest = new AliPayQrCodeRequest();
            payQrCodeRequest.setOutTradeNo(chargeEntity.getPayNo());
            payQrCodeRequest.setTotalAmount(interests.getAmount());
            payQrCodeRequest.setSubject(chargeEntity.getNote());

            PayQrCodeResponse response = aliPayBiz.aliPayQrCode(payQrCodeRequest);

            // 发起轮询
            startPaymentPolling(response.getOutTradeNo());

            return response;
        }
        return null;
    }

    @Override
    public String aliPayNotify(Map<String, String> params) {
        log.info("收到支付宝回调通知: {}", JSON.toJSONString(params, true));

        // 验证回调签名
        if (!aliPayBiz.verifyNotify(params)) {
            log.warn("支付宝回调签名验证失败");
            return "fail";
        }

        // 处理订单逻辑
        String outTradeNo = params.get("out_trade_no");
        String tradeStatus = params.get("trade_status");

        if (AliPayStateEnum.TRADE_SUCCESS.getCode().equals(tradeStatus)) {
            log.info("支付宝订单支付成功，商户订单号: {}", outTradeNo);

            AliPayStatusResponse orderStatus = aliPayStatus(outTradeNo);

            sendPaymentSuccessNotification(outTradeNo, orderStatus);
        }

        return "success";
    }

    @Override
    public void cancelAliPay(String outTradeNo) {
        try {
            aliPayBiz.cancelPay(outTradeNo);
            userMemberChargeBiz.close(outTradeNo);
        } catch (Exception e) {
            log.error("取消支付宝支付失败：" + e.getMessage());
            throw new PayException(e.getMessage());
        }
    }

    @Override
    public AliPayStatusResponse aliPayStatus(String outTradeNo) {
        AliPayStatusResponse orderStatus = aliPayBiz.aliPayStatus(outTradeNo);

        String state = getChargeState(PayChannelEnum.ALI.getCode(), orderStatus.getTradeState());

        UserMemberChargeUpdateRequest chargeUpdateRequest = new UserMemberChargeUpdateRequest();
        chargeUpdateRequest.setPayNo(outTradeNo);
        chargeUpdateRequest.setState(state);
        chargeUpdateRequest.setTransactionId(orderStatus.getTradeNo());
        chargeUpdateRequest.setPayTime(orderStatus.getSendPayDate());
        userMemberChargeBiz.update(chargeUpdateRequest);

        // 统一转成本系统的支付状态
        orderStatus.setTradeState(state);
        orderStatus.setTradeStateDesc(MemberChargeStateEnum.getDescByCode(state));

        return orderStatus;
    }

    @Override
    public void wxPayNotify(String signature, String timestamp, String nonce, String serial, String body) {
        try {
            WxPayStatusResponse payStatusResponse = wechatPayBiz.wxPayNotify(signature, timestamp, nonce, serial, body);

            wxPayStatus(payStatusResponse.getOutTradeNo());
        } catch (Exception e) {
            log.error("微信验证回调签名失败", e);

            throw new PayException(e.getMessage());
        }
    }

    @Override
    public WxPayStatusResponse wxPayStatus(String outTradeNo) {
        WxPayStatusResponse orderStatus = wechatPayBiz.wxPayStatus(outTradeNo);

        String state = getChargeState(PayChannelEnum.WX.getCode(), orderStatus.getTradeState());

        UserMemberChargeUpdateRequest chargeUpdateRequest = new UserMemberChargeUpdateRequest();
        chargeUpdateRequest.setPayNo(orderStatus.getOutTradeNo());
        chargeUpdateRequest.setState(state);
        chargeUpdateRequest.setTransactionId(orderStatus.getTradeNo());
        chargeUpdateRequest.setPayTime(orderStatus.getSendPayDate());
        userMemberChargeBiz.update(chargeUpdateRequest);

        // 统一转成本系统的支付状态
        orderStatus.setTradeState(state);
        orderStatus.setTradeStateDesc(MemberChargeStateEnum.getDescByCode(state));

        return orderStatus;
    }

    @Override
    public void closeWxPay(String outTradeNo) throws PayException {
        try {
            wechatPayBiz.closePay(outTradeNo);
            userMemberChargeBiz.close(outTradeNo);
        } catch (Exception e) {
            log.error("取消微信支付失败：" + e.getMessage());
            throw new PayException(e.getMessage());
        }
    }

    private MemberChargeGetResponse saveMemberCharge(MemberInstanceEntity instance,
                                                     MemberInterestsEntity interests,
                                                     String channel,
                                                     String chargeType,
                                                     String note) {
        UserMemberChargeSaveRequest request = new UserMemberChargeSaveRequest();
        request.setAppId(instance.getAppId());
        request.setAccountInstanceId(instance.getAccountInstanceId());
        request.setMemberInstanceId(instance.getMemberInstanceId());
        request.setMemberInterestsId(interests.getMemberInterestsId());
        request.setAmount(interests.getAmount());
        request.setChannel(channel);
        request.setNote(note);
        request.setChargeType(chargeType);

        return userMemberChargeBiz.save(request);
    }

    private String validateMemberInterest(MemberInstanceEntity oldInstance,
                                          MemberInterestsEntity interests,
                                          PayQrCodeRequest request){
        if (request.getChargeType().equals(MemberChargeTypeEnum.RENEWAL.getCode())) {
            // 校验续费的等级跟当前等级是否一致
            if (!oldInstance.getMemberInterestsId().equals(request.getMemberInterestsId())) {
                throw new MemberException("当前不是【" + MemberTypeEnum.getDescByCode(interests.getMemberType()) + "】，不能进行续费操作");
            }
            return MemberChargeTypeEnum.RENEWAL.getCode();
        } else {
            // 校验升级的等级跟当前等级是否一致
            if (oldInstance.getMemberInterestsId().equals(request.getMemberInterestsId())) {
                throw new MemberException("当前已是【" + MemberTypeEnum.getDescByCode(oldInstance.getMemberType()) + "】，不可重复操作");
            }
            return MemberChargeTypeEnum.UP_GRADE.getCode();
        }
    }

    private MemberInterestsEntity getMemberInterestsById(Long memberInterestsId) {
        MemberInterestsEntity interests = memberInterestsService.getById(memberInterestsId);

        isMemberInterestsExist(interests);

        checkMemberInterestsState(interests);

        return interests;
    }

    private MemberInstanceEntity getMemberInterestsByAccountInstanceIdAndAppId(Long accountInstanceId, String appId) {
        return memberInstanceService.getOne(QueryWrapper.create()
                .eq(MemberInstanceEntity::getAccountInstanceId, accountInstanceId)
                .eq(MemberInstanceEntity::getAppId, appId));
    }

    private void isMemberInstanceExist(MemberInstanceEntity entity) {
        if (Objects.isNull(entity)) {
            throw new MemberException(MemberExceptionStatusCode.MEMBER_INSTANCE_NOT_FOUND);
        }
    }

    protected void isMemberInterestsExist(MemberInterestsEntity entity) {
        if (Objects.isNull(entity)) {
            throw new MemberException(MemberExceptionStatusCode.INTERESTS_NOT_FOUND);
        }
    }

    private void checkMemberInterestsState(MemberInterestsEntity interests){
        if (interests.getState() == 1) {
            throw new MemberException(MemberExceptionStatusCode.HAS_DISABLED);
        }
    }

    /**
     * 启动支付轮询
     */
    private void startPaymentPolling(String outTradeNo) {
        CompletableFuture<AliPayStatusResponse> pollingFuture = startPaymentPolling(
                outTradeNo,
                aliPayProperties.getPollingMaxTimes(),
                aliPayProperties.getPollingIntervalSeconds());

        // 处理轮询结果
        pollingFuture.whenComplete((orderStatus, throwable) -> {
            if (throwable != null) {
                log.error("支付轮询异常，订单号: {}", outTradeNo, throwable);
            } else {
                log.info("支付轮询完成，订单号: {}，状态: {}", outTradeNo, orderStatus.getTradeState());
                // 推送结果给前端
                sendPaymentSuccessNotification(outTradeNo, orderStatus);
            }
        });
    }

    /**
     * 发送支付成功通知给前端
     */
    private void sendPaymentSuccessNotification(String outTradeNo, AliPayStatusResponse orderStatus) {
        try {
            Map<String, Object> message = new HashMap<>();
            message.put("outTradeNo", outTradeNo);
            message.put("tradeStatus", orderStatus.getTradeState());
            message.put("totalAmount", orderStatus.getTotalAmount());

            messagingTemplate.convertAndSend("/topic/ws/ali/pay/" + outTradeNo, message);
            log.info("已发送支付结果通知，订单号: {}", outTradeNo);
        } catch (Exception e) {
            log.error("发送支付通知失败", e);
        }
    }

    /**
     * 发起支付轮询
     * @param outTradeNo 商户订单号
     * @param maxPollingTimes 最大轮询次数
     * @param intervalSeconds 轮询间隔秒数
     * @return 支付结果异步Future
     */
    public CompletableFuture<AliPayStatusResponse> startPaymentPolling(String outTradeNo, int maxPollingTimes, int intervalSeconds) {
        CompletableFuture<AliPayStatusResponse> future = new CompletableFuture<>();
        // 沙箱环境使用默认初始延迟，否则使用传入的初始延迟
        int actualInitialDelay = aliPayProperties.isSandBoxEnabled() ? aliPayProperties.getSandBoxInitialDelaySeconds() : intervalSeconds;

        AliPayPollingTask task = new AliPayPollingTask(outTradeNo, maxPollingTimes, intervalSeconds, future);

        // 延迟actualInitialDelay后开始第一次轮询
        pollingExecutor.schedule(task, actualInitialDelay, TimeUnit.SECONDS);

        return future;
    }

    /**
     * 支付宝支付轮询任务
     */
    private class AliPayPollingTask implements Runnable {
        private final String outTradeNo;
        private final int maxPollingTimes;
        private final int intervalSeconds;
        private final CompletableFuture<AliPayStatusResponse> future;
        private int currentPollingCount = 0;

        public AliPayPollingTask(String outTradeNo,
                                 int maxPollingTimes,
                                 int intervalSeconds,
                                 CompletableFuture<AliPayStatusResponse> future) {
            this.outTradeNo = outTradeNo;
            this.maxPollingTimes = maxPollingTimes;
            this.intervalSeconds = intervalSeconds;
            this.future = future;
        }

        @Override
        public void run() {
            currentPollingCount++;
            log.info("开始第{}次轮询查询，订单号: {}", currentPollingCount, outTradeNo);

            try {
                AliPayStatusResponse response = aliPayStatus(outTradeNo);
                String tradeStatus = response.getTradeState();

                // 判断交易状态
                if ("TRADE_SUCCESS".equals(tradeStatus)) {
                    // 支付成功，完成Future
                    future.complete(response);

                    log.info("轮询查询到支付成功，订单号: {}", outTradeNo);
                } else if ("TRADE_CLOSED".equals(tradeStatus) || "TRADE_FINISHED".equals(tradeStatus)) {
                    // 交易已关闭或完成
                    future.complete(response);

                    log.info("轮询查询到交易已关闭/完成，订单号: {}", outTradeNo);
                } else if ("WAIT_BUYER_PAY".equals(tradeStatus)) {
                    // 等待买家付款
                    if (currentPollingCount >= maxPollingTimes) {
                        // 达到最大轮询次数，撤销交易
//                        log.warn("达到最大轮询次数，准备撤销交易，订单号: {}", outTradeNo);
//
//                        // 达到最大轮询数撤销交易
//                        cancelAliPay(outTradeNo);
//
//                        response.setTradeState("TRADE_CANCELED");

                        future.complete(response);
                    } else {
                        // 继续轮询
                        log.info("等待买家付款，继续轮询，订单号: {}", outTradeNo);

                        pollingExecutor.schedule(this, intervalSeconds, TimeUnit.SECONDS);
                    }
                } else {
                    // 其他状态
                    future.complete(response);

                    log.info("轮询查询到其他状态: {}，订单号: {}", tradeStatus, outTradeNo);
                }
            } catch (Exception e) {
                log.error("轮询查询异常，订单号: {}", outTradeNo, e);

                if (currentPollingCount >= maxPollingTimes) {
                    future.completeExceptionally(e);
                } else {
                    // 异常情况下仍继续轮询
                    pollingExecutor.schedule(this, intervalSeconds, TimeUnit.SECONDS);
                }
            }
        }
    }

    private String getChargeState(String channel, String state) {
        if (channel.equals(PayChannelEnum.ALI.getCode())) {
            if (state.equals(AliPayStateEnum.TRADE_SUCCESS.getCode())) {
                return MemberChargeStateEnum.SUCCESS.getCode();
            } else if (state.equals(AliPayStateEnum.TRADE_CLOSED.getCode())) {
                return MemberChargeStateEnum.CLOSED.getCode();
            } else if (state.equals(AliPayStateEnum.WAIT_BUYER_PAY.getCode())) {
                return MemberChargeStateEnum.WAIT_PAY.getCode();
            } else if (state.equals(AliPayStateEnum.TRADE_FINISHED.getCode())) {
                return MemberChargeStateEnum.FINISHED.getCode();
            }
        } else if (channel.equals(PayChannelEnum.WX.getCode())) {
            if (state.equals(WechatPayStateEnum.SUCCESS.getCode())) {
                return MemberChargeStateEnum.SUCCESS.getCode();
            } else if (state.equals(WechatPayStateEnum.CLOSED.getCode())) {
                return MemberChargeStateEnum.CLOSED.getCode();
            } else if (state.equals(WechatPayStateEnum.REFUND.getCode())) {
                return MemberChargeStateEnum.REFUND.getCode();
            } else if (state.equals(WechatPayStateEnum.NOT_PAY.getCode())) {
                return MemberChargeStateEnum.WAIT_PAY.getCode();
            }
        }
        return "";
    }

}
