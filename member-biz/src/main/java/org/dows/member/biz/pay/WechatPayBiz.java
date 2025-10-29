package org.dows.member.biz.pay;

import org.dows.member.request.pay.WxPayQrCodeRequest;
import org.dows.member.response.pay.PayQrCodeResponse;
import org.dows.member.response.pay.WxPayStatusResponse;

public interface WechatPayBiz {

    PayQrCodeResponse wxPayQrCode(WxPayQrCodeRequest request);

    /**
     * 微信支付回调
     * @param signature 验签的签名值
     * @param timestamp 验签的时间戳
     * @param nonce 验签的随机字符串
     * @param serial 验签的微信支付平台证书序列号/微信支付公钥ID
     * @param body 回调内容
     */
    WxPayStatusResponse wxPayNotify(String signature, String timestamp, String nonce, String serial, String body);

    /**
     * 查询微信支付订单
     */
    WxPayStatusResponse wxPayStatus(String outTradeNo);

    /**
     * 关闭微信支付
     */
    void closePay(String outTradeNo);
}