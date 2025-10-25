package org.dows.member.handler.pay.impl;

import com.alipay.api.AlipayApiException;
import com.alipay.api.AlipayClient;
import com.alipay.api.domain.AlipayTradeCancelModel;
import com.alipay.api.domain.AlipayTradePrecreateModel;
import com.alipay.api.domain.AlipayTradeQueryModel;
import com.alipay.api.internal.util.AlipaySignature;
import com.alipay.api.request.AlipayTradeCancelRequest;
import com.alipay.api.request.AlipayTradePrecreateRequest;
import com.alipay.api.request.AlipayTradeQueryRequest;
import com.alipay.api.response.AlipayTradeCancelResponse;
import com.alipay.api.response.AlipayTradePrecreateResponse;
import com.alipay.api.response.AlipayTradeQueryResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.dows.member.enums.MemberChargeStateEnum;
import org.dows.member.exception.MemberException;
import org.dows.member.handler.config.AliPayProperties;
import org.dows.member.handler.pay.AliPayBiz;
import org.dows.member.handler.utils.PaymentTimeConverter;
import org.dows.member.request.pay.AliPayQrCodeRequest;
import org.dows.member.response.pay.AliPayStatusResponse;
import org.dows.member.response.pay.PayQrCodeResponse;
import org.springframework.stereotype.Component;

import java.util.Map;

@Slf4j
@Component
@RequiredArgsConstructor
public class AliPayBizImpl implements AliPayBiz {

    private final AliPayProperties aliPayProperties;
    private final AlipayClient alipayClient;

    @Override
    public PayQrCodeResponse aliPayQrCode(AliPayQrCodeRequest qrCodeRequest) {
        try {
            AlipayTradePrecreateModel model = new AlipayTradePrecreateModel();
            model.setOutTradeNo(qrCodeRequest.getOutTradeNo());
            model.setTotalAmount(qrCodeRequest.getTotalAmount().toString());
            model.setSubject(qrCodeRequest.getSubject());
            model.setTimeoutExpress(aliPayProperties.getTimeout());
            model.setProductCode(aliPayProperties.getProductCode());

            AlipayTradePrecreateRequest request = new AlipayTradePrecreateRequest();
            request.setNotifyUrl(aliPayProperties.getNotifyUrl());
            request.setBizModel(model);

            AlipayTradePrecreateResponse response = alipayClient.certificateExecute(request);
            if (response.isSuccess()) {
                PayQrCodeResponse payResponse = new PayQrCodeResponse();
                payResponse.setOutTradeNo(qrCodeRequest.getOutTradeNo());
                payResponse.setQrCode(response.getQrCode());

                return payResponse;
            } else {
                throw new MemberException("创建支付订单失败: " + response.getSubMsg());
            }
        } catch (AlipayApiException e) {
            log.error("创建支付宝订单失败", e);
            throw new MemberException("创建支付订单失败: " + e.getMessage());
        }
    }

    @Override
    public boolean verifyNotify(Map<String, String> params) {
        try {
            return AlipaySignature.rsaCheckV2(
                    params,
                    aliPayProperties.getPublicKey(),
                    aliPayProperties.getCharset(),
                    aliPayProperties.getSignType()
            );
        } catch (AlipayApiException e) {
            log.error("验证回调签名失败", e);
            return false;
        }
    }

    @Override
    public AliPayStatusResponse aliPayStatus(String outTradeNo) {
        AliPayStatusResponse queryResponse = new AliPayStatusResponse();

        // 构造请求参数以调用接口
        AlipayTradeQueryModel model = new AlipayTradeQueryModel();
        model.setOutTradeNo(outTradeNo);

        AlipayTradeQueryRequest request = new AlipayTradeQueryRequest();
        request.setBizModel(model);

        try {
            AlipayTradeQueryResponse response = alipayClient.certificateExecute(request);
            if (response.isSuccess()) {
                queryResponse.setOutTradeNo(outTradeNo);
                queryResponse.setTradeNo(response.getTradeNo());
                // 交易状态说明：WAIT_BUYER_PAY(待付款)、TRADE_SUCCESS(支付成功)、TRADE_CLOSED(交易关闭)等FAIL
                queryResponse.setTradeState(response.getTradeStatus());
                queryResponse.setTotalAmount(response.getTotalAmount());
                queryResponse.setSendPayDate(PaymentTimeConverter.toLocalDateTime(response.getSendPayDate()));
                queryResponse.setSuccess(true);
            } else {
                queryResponse.setTradeState(MemberChargeStateEnum.FAILED.getCode());
                queryResponse.setSuccess(false);
                queryResponse.setMessage(response.getMsg());
            }
        } catch (AlipayApiException e) {
            log.error("查询支付宝订单失败", e);
            queryResponse.setSuccess(false);
            queryResponse.setMessage("系统错误: " + e.getMessage());
        }
        return queryResponse;
    }

    @Override
    public void cancelPay(String outTradeNo) throws AlipayApiException {
        // 构造请求参数以调用接口
        AlipayTradeCancelModel model = new AlipayTradeCancelModel();
        model.setOutTradeNo(outTradeNo);

        AlipayTradeCancelRequest request = new AlipayTradeCancelRequest();
        request.setBizModel(model);

        AlipayTradeCancelResponse response = alipayClient.certificateExecute(request);
        if (response.isSuccess()) {
            log.info("订单撤销成功: {}", outTradeNo);
        } else {
            log.error("订单撤销失败: {}，错误信息: {}", outTradeNo, response.getMsg());

            if (!"40004".equals(response.getCode()) && !"ACQ.TRADE_NOT_EXIST".equals(response.getSubCode())) {
                throw new AlipayApiException("订单撤销失败: " + response.getMsg());
            }
        }
    }
}