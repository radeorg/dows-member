package org.dows.member.handler.pay.impl;

import com.wechat.pay.java.core.notification.NotificationParser;
import com.wechat.pay.java.core.notification.RequestParam;
import com.wechat.pay.java.service.payments.model.Transaction;
import com.wechat.pay.java.service.payments.nativepay.NativePayService;
import com.wechat.pay.java.service.payments.nativepay.model.Amount;
import com.wechat.pay.java.service.payments.nativepay.model.PrepayRequest;
import com.wechat.pay.java.service.payments.nativepay.model.PrepayResponse;
import com.wechat.pay.java.service.payments.nativepay.model.QueryOrderByOutTradeNoRequest;
import com.wechat.pay.java.service.weixinpayscanandride.model.TradeState;
import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.dows.member.entity.MemberChargeEntity;
import org.dows.member.enums.MemberChargeTypeEnum;
import org.dows.member.exception.MemberException;
import org.dows.member.exception.WechatPayException;
import org.dows.member.handler.config.WechatPayProperties;
import org.dows.member.handler.pay.WechatPayBiz;
import org.dows.member.handler.user.UserMemberChargeHandler;
import org.dows.member.handler.user.UserMemberInstanceBiz;
import org.dows.member.request.pay.WechatPayQrCodeRequest;
import org.dows.member.response.pay.PayQrCodeResponse;
import org.dows.member.response.pay.WechatPayStatusResponse;
import org.dows.member.handler.pay.WechatPayBiz;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

@Slf4j
@Component
@RequiredArgsConstructor
public class WechatPayBizImpl implements WechatPayBiz {

    @Resource
    private WechatPayProperties wechatPayProperties;
    private final NativePayService nativePayService;
    private final NotificationParser notificationParser;
    private final UserMemberChargeHandler userMemberChargeHandler;
    private final UserMemberInstanceBiz userMemberInstanceBiz;

    @Override
    public PayQrCodeResponse wechatPayQrCode(WechatPayQrCodeRequest request) {
        PrepayRequest prepayRequest = new PrepayRequest();
        Amount amount = new Amount();
        amount.setTotal(request.getTotalAmount().multiply(new BigDecimal("100")).intValue()); // 单位：分
        prepayRequest.setAmount(amount);
        prepayRequest.setAppid(wechatPayProperties.getAppId());
        prepayRequest.setMchid(wechatPayProperties.getMerchantId());
        prepayRequest.setDescription(request.getDescription());
        prepayRequest.setNotifyUrl(wechatPayProperties.getNotifyUrl());
        prepayRequest.setOutTradeNo(request.getOutTradeNo());
        try {
            PrepayResponse response = nativePayService.prepay(prepayRequest);

            PayQrCodeResponse qrCodeResponse = new PayQrCodeResponse();
            qrCodeResponse.setQrCode(response.getCodeUrl()); // 返回支付二维码链接
            qrCodeResponse.setOutTradeNo(request.getOutTradeNo());

            return qrCodeResponse;
        } catch (Exception e) {
            log.error("微信支付接口调用失败: {}", e.getMessage());
            throw new MemberException("创建支付订单失败: " + e.getMessage());
        }
    }

    @Override
    public Map<String, String> wechatPayNotify(HttpServletRequest request) {
        try {
            // 1. 从请求头获取参数
            String signature = request.getHeader("Wechatpay-Signature");
            String timestamp = request.getHeader("Wechatpay-Timestamp");
            String nonce = request.getHeader("Wechatpay-Nonce");
            String serial = request.getHeader("Wechatpay-Serial");
            String body = request.getReader().lines().collect(Collectors.joining()); // 原始请求体
            RequestParam requestParam = new RequestParam.Builder()
                    .serialNumber(serial)
                    .nonce(nonce)
                    .signature(signature)
                    .timestamp(timestamp)
                    .body(body)
                    .build();

            // 2. 解析回调内容（自动验签和解密）
            Transaction transaction = notificationParser.parse(requestParam, Transaction.class);

            // 3. 幂等性校验
            MemberChargeEntity memberCharge = userMemberChargeHandler.getByPayNo(transaction.getOutTradeNo());
            if (memberCharge == null) {
                throw new WechatPayException("未查询到对应支付订单");
            }

            // 4. 业务处理（如更新订单状态）
            memberCharge.setTransactionId(transaction.getOutTradeNo());
            memberCharge.setChargeTime(payTimeToPareDate(transaction.getSuccessTime()));
            memberCharge.setState(transaction.getTradeState().name());

            if (memberCharge.getChargeType().equals(MemberChargeTypeEnum.RENEWAL.getCode())) {
                userMemberInstanceBiz.renewal(memberCharge);
            } else if (memberCharge.getChargeType().equals(MemberChargeTypeEnum.UP_GRADE.getCode())) {
                userMemberInstanceBiz.upgrade(memberCharge);
            }

            log.info("支付成功: {}", transaction.getOutTradeNo());

            Map<String, String> response = new HashMap<>();
            response.put("code", "SUCCESS");
            response.put("message", "成功");

            return response;
        } catch (Exception e) {
            log.error(e.getMessage());

            Map<String, String> response = new HashMap<>();
            response.put("code", "FAIL");
            response.put("message", e.getMessage());
            return response;
        }
    }

    @Override
    public WechatPayStatusResponse wechatPayStatus(String outTradeNo) {
        WechatPayStatusResponse wechatPayStatusResponse = new WechatPayStatusResponse();

        // 1. 先查本地数据库
        //  Order order = orderMapper.selectByOutTradeNo(outTradeNo);
//        if (order == null) {
//            throw new BusinessException("订单不存在");
//        }

        // 2. 如果本地状态已明确（成功/关闭），直接返回
//        if ("PAID".equals(order.getStatus()) || "CLOSED".equals(order.getStatus())) {
//            return buildOrderStatusVO(order);
//        }

        // 3. 本地状态未明确，调用微信支付查单接口确认
        QueryOrderByOutTradeNoRequest request = new QueryOrderByOutTradeNoRequest();
        request.setOutTradeNo(outTradeNo);
        request.setMchid(wechatPayProperties.getMerchantId());

        Transaction transaction = nativePayService.queryOrderByOutTradeNo(request);
        String stateDesc = transaction != null ? transaction.getTradeStateDesc() : "订单不存在";
        wechatPayStatusResponse.setTradeStateDesc(stateDesc);
        if (transaction != null && TradeState.SUCCESS.equals(transaction.getTradeState())) {
            // 支付成功，处理后续业务（如更新订单状态、发货等）
            System.out.println("支付成功，微信订单号：" + transaction.getTransactionId());
            wechatPayStatusResponse.setSuccessTime(transaction.getSuccessTime());
            // 4. 更新本地订单状态
public class WechatPayBizImpl implements WechatPayBiz {

        } else {
            // 支付未成功，根据状态做进一步处理
            System.out.println("支付未成功：" + stateDesc);
        }
        return wechatPayStatusResponse;
    }

    private Date payTimeToPareDate(String successTime){
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ssXXX");
        ZonedDateTime zonedDateTime = ZonedDateTime.parse(successTime, formatter);
        return Date.from(zonedDateTime.toInstant());
    }
}