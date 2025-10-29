package org.dows.member.biz.pay.impl;

import com.wechat.pay.java.core.notification.NotificationParser;
import com.wechat.pay.java.core.notification.RequestParam;
import com.wechat.pay.java.service.payments.model.Transaction;
import com.wechat.pay.java.service.payments.nativepay.NativePayService;
import com.wechat.pay.java.service.payments.nativepay.model.*;
import jakarta.annotation.Resource;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.dows.member.biz.pay.WechatPayBiz;
import org.dows.member.biz.utils.PaymentTimeConverter;
import org.dows.member.config.WechatPayProperties;
import org.dows.member.exception.MemberException;
import org.dows.member.request.pay.WxPayQrCodeRequest;
import org.dows.member.response.pay.PayQrCodeResponse;
import org.dows.member.response.pay.WxPayStatusResponse;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;

@Slf4j
@Component
@RequiredArgsConstructor
public class WechatPayBizImpl implements WechatPayBiz {

    @Resource
    private WechatPayProperties wechatPayProperties;
    private final NativePayService nativePayService;
    private final NotificationParser notificationParser;

    @Override
    public PayQrCodeResponse wxPayQrCode(WxPayQrCodeRequest request) {
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
            log.error("创建微信支付订单失败: {}", e.getMessage());
            throw new MemberException("创建微信支付订单失败: " + e.getMessage());
        }
    }

    @Override
    public WxPayStatusResponse wxPayNotify(String signature, String timestamp, String nonce, String serial, String body) {
        try {
            // 构造 RequestParam
            RequestParam requestParam = new RequestParam.Builder()
                    .serialNumber(serial)
                    .nonce(nonce)
                    .signature(signature)
                    .timestamp(timestamp)
                    .body(body)
                    .build();

            try {
                // 以支付通知回调为例，验签、解密并转换成 Transaction
                Transaction transaction = notificationParser.parse(requestParam, Transaction.class);
                return transactionToRes(transaction);
            } catch (Exception e) {
                // 签名验证失败，返回 401 UNAUTHORIZED 状态码
                log.error("sign verification failed", e);
                throw new RuntimeException(e);
            }
        } catch (Exception e) {
            log.error("微信验证回调签名失败", e);
            throw new RuntimeException(e);
        }
    }

    @Override
    public WxPayStatusResponse wxPayStatus(String outTradeNo) {
        QueryOrderByOutTradeNoRequest request = new QueryOrderByOutTradeNoRequest();
        request.setOutTradeNo(outTradeNo);
        request.setMchid(wechatPayProperties.getMerchantId());

        try {
            Transaction transaction = nativePayService.queryOrderByOutTradeNo(request);

            return transactionToRes(transaction);
        } catch (Exception e) {
            log.error("微信订单查询失败: {}，错误信息: {}", outTradeNo, e.getMessage());

            WxPayStatusResponse queryResponse = new WxPayStatusResponse();
            queryResponse.setSuccess(false);
            queryResponse.setMessage("查询支付宝订单失败: " + e.getMessage());
            return queryResponse;
        }
    }

    @Override
    public void closePay(String outTradeNo) {
        try {
            // 构建关闭订单请求
            CloseOrderRequest request = new CloseOrderRequest();
            request.setOutTradeNo(outTradeNo);
            request.setMchid(wechatPayProperties.getMerchantId());

            // 调用微信支付关闭订单接口
            nativePayService.closeOrder(request);
        } catch (Exception e) {
            log.error("关闭微信订单异常: outTradeNo={}, mess={}", outTradeNo, e.getMessage());
        }
    }

    private WxPayStatusResponse transactionToRes(Transaction transaction){
        WxPayStatusResponse response = new WxPayStatusResponse();
        response.setOutTradeNo(transaction.getOutTradeNo());
        response.setTradeNo(transaction.getTransactionId());
        // 交易状态说明：WAIT_BUYER_PAY(待付款)、TRADE_SUCCESS(支付成功)、TRADE_CLOSED(交易关闭)等FAIL
        response.setTradeState(transaction.getTradeState().name());
        response.setTradeStateDesc(transaction.getTradeStateDesc());
        if (transaction.getAmount() != null){
            response.setTotalAmount(amountToStr(transaction.getAmount().getTotal()));
        }
        response.setPayAmount(response.getTotalAmount());
        response.setSuccessTime(paymentTimeConverter(transaction.getSuccessTime()));
     //   response.setSuccessTime(PaymentTimeConverter.format(transaction.getSuccessTime()));
        response.setSuccess(true);

        return response;
    }

    private String amountToStr(Integer amount){
        double tempResult = (double) amount / 100;
        return String.format("%.2f", tempResult);
    }
    private LocalDateTime paymentTimeConverter(String successTime){
        if (successTime != null){
            LocalDateTime localDateTime2 = LocalDateTime.ofInstant(Instant.parse(successTime), ZoneId.systemDefault());
            return localDateTime2;
        }
      return null;
    }
}