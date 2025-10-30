package org.dows.member.biz.pay.impl;

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
import org.apache.commons.lang3.StringUtils;
import org.dows.member.exception.MemberException;
import org.dows.member.config.AliPayConfig;
import org.dows.member.config.AliPayProperties;
import org.dows.member.biz.pay.AliPayBiz;
import org.dows.member.biz.utils.PaymentTimeConverter;
import org.dows.member.request.pay.AliPayQrCodeRequest;
import org.dows.member.response.pay.AliPayStatusResponse;
import org.dows.member.response.pay.PayQrCodeResponse;
import org.springframework.stereotype.Component;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;

@Slf4j
@Component
@RequiredArgsConstructor
public class AliPayBizImpl implements AliPayBiz {

    private final AliPayProperties aliPayProperties;
    private final AlipayClient alipayClient;
    private final AliPayConfig aliPayConfig;

    @Override
    public PayQrCodeResponse aliPayQrCode(AliPayQrCodeRequest qrCodeRequest) {
        try {
            AlipayTradePrecreateModel model = new AlipayTradePrecreateModel();
            model.setOutTradeNo(qrCodeRequest.getOutTradeNo());
            model.setTotalAmount(qrCodeRequest.getTotalAmount().toString());
            model.setSubject(qrCodeRequest.getSubject());
            model.setTimeoutExpress(aliPayProperties.getTimeout());
            model.setTimeExpire(getExpireTime(aliPayProperties.getTimeout()));
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
                throw new MemberException("创建支付宝支付订单失败: " + response.getSubMsg());
            }
        } catch (AlipayApiException e) {
            log.error("创建支付宝支付订单失败", e);
            throw new MemberException("创建支付宝支付订单失败: " + e.getMessage());
        }
    }

    @Override
    public boolean verifyNotify(Map<String, String> params) {
        try {
            // 使用与AlipayClient相同的证书源获取公钥
            String alipayPublicKey = aliPayConfig.getAlipayPublicKey();

            // rsaCheckV1证书模式验签
            return AlipaySignature.rsaCertCheckV1(
                    params,
                    alipayPublicKey,
                    aliPayProperties.getCharset(),
                    aliPayProperties.getSignType()
            );
        } catch (AlipayApiException e) {
            log.error("支付宝验证回调签名失败", e);
            return false;
        } catch (Exception e) {
            log.error("支付宝验证回调签名失败", e);
            throw new RuntimeException(e);
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
                // 交易状态：WAIT_BUYER_PAY（交易创建，等待买家付款）、TRADE_CLOSED（未付款交易超时关闭，或支付完成后全额退款）、TRADE_SUCCESS（交易支付成功）、TRADE_FINISHED（交易结束，不可退款）
                queryResponse.setTradeState(response.getTradeStatus());
                queryResponse.setTotalAmount(response.getTotalAmount());
                queryResponse.setPayAmount(response.getBuyerPayAmount());
                queryResponse.setSendPayDate(PaymentTimeConverter.toLocalDateTime(response.getSendPayDate()));
                queryResponse.setSuccess(true);
            } else {
                log.error("支付宝订单查询失败: {}，错误信息: {}", outTradeNo, response.getMsg());
                throw new AlipayApiException("支付宝订单查询失败: " + response.getMsg());
            }
        } catch (AlipayApiException e) {
            log.error("查询支付宝订单失败", e);
            queryResponse.setSuccess(false);
            queryResponse.setMessage("查询支付宝订单失败: " + e.getMessage());
        }
        return queryResponse;
    }

    @Override
    public void cancelPay(String outTradeNo) throws Exception {
        // 构造请求参数以调用接口
        AlipayTradeCancelModel model = new AlipayTradeCancelModel();
        model.setOutTradeNo(outTradeNo);

        AlipayTradeCancelRequest request = new AlipayTradeCancelRequest();
        request.setBizModel(model);

        AlipayTradeCancelResponse response = alipayClient.certificateExecute(request);
        if (response.isSuccess()) {
            log.info("支付宝订单撤销成功: {}", outTradeNo);
        } else {
            log.error("支付宝订单撤销失败: {}，错误信息: {}", outTradeNo, response.getMsg());

            if (!"40004".equals(response.getCode()) && !"ACQ.TRADE_NOT_EXIST".equals(response.getSubCode())) {
                throw new AlipayApiException("支付宝订单撤销失败: " + response.getMsg());
            }
        }
    }

    /**
     * 获取超时时间
     * @param minutes 超时分钟数
     * @return 格式化的超时时间字符串
     */
    private static String getExpireTime(String minutes) {
        if (StringUtils.isEmpty(minutes)) {
            return "";
        }
        int min = Integer.parseInt(minutes.replace("m", ""));
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date expireDate = new Date(System.currentTimeMillis() + (long) min * 60 * 1000);
        return sdf.format(expireDate);
    }
}