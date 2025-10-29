package org.dows.member.biz.pay;

import org.dows.member.exception.PayException;
import org.dows.member.request.pay.PayQrCodeRequest;
import org.dows.member.response.pay.AliPayStatusResponse;
import org.dows.member.response.pay.PayQrCodeResponse;
import org.dows.member.response.pay.WxPayStatusResponse;

import java.util.Map;

/**
 * 充值记录（充值订单）
 */
public interface PaymentBiz {

    /**
     * 创建支付信息
     * @return 支付二维码链接
     */
    PayQrCodeResponse createNativePayment(PayQrCodeRequest request);

    /**
     * 支付宝回调
     */
    String aliPayNotify(Map<String, String> params);

    /**
     * 取消支付宝支付（支付宝官网建议：多次轮询未成功的通过调取消接口，而不是关闭接口）
     */
    void cancelAliPay(String outTradeNo) throws PayException;

    /**
     * 查询支付宝支付状态
     */
    AliPayStatusResponse aliPayStatus(String outTradeNo);

    /**
     * 微信支付回调
     * @param signature 验签的签名值
     * @param timestamp 验签的时间戳
     * @param nonce 验签的随机字符串
     * @param serial 验签的微信支付平台证书序列号/微信支付公钥ID
     * @param body 回调内容
     */
    void wxPayNotify(String signature, String timestamp, String nonce, String serial, String body);

    /**
     * 查询微信支付状态
     */
    WxPayStatusResponse wxPayStatus(String outTradeNo);

    /**
     * 关闭微信支付（微信没有取消）
     */
    void closeWxPay(String outTradeNo) throws PayException;
}
