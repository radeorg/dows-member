package org.dows.member.handler.config;

import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Data
@Component
@ConfigurationProperties(prefix = "wx.pay")
public class WechatPayProperties {

    /** 应用ID */
//    @Value("${wx.pay.app-id}")
    private String appId;

    /** 商户号 */
//    @Value("${wx.pay.merchant-id}")
    private String merchantId;

    /** 商户API私钥路径 */
//    @Value("${wx.pay.private-key-path}")
    private String privateKeyPath;

    /** 商户证书序列号 */
//    @Value("${wx.pay.merchant-serial-number}")
    private String merchantSerialNumber;

    /** APIv3密钥 */
//    @Value("${wx.pay.api-v3-key}")
    private String apiV3Key;

    /** 支付通知回调地址 */
//    @Value("${wx.pay.notify-url}")
    private String notifyUrl;

    /** 支付成功跳转地址 */
//    @Value("${wx.pay.return-url}")
    private String returnUrl;

    /** 微信支付平台证书路径 */
    private String platformCertPath;
}
