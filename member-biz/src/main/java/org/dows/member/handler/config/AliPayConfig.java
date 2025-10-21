package org.dows.member.handler.config;

import com.alipay.api.*;
import org.springframework.beans.factory.annotation.Value;

public class AliPayConfig {
    /**
     * 商户号
     */
    @Value("${pay.alipay.appId}")
    public static String appId;
    /**
     * 应用私钥
     */
    @Value("${pay.alipay.privateKey}")
    public static  String privateKey;

    /**
     * 应用私钥
     */
    @Value("${pay.alipay.publicKey}")
    public static  String publicKey;
    /**
     * 应用公钥证书本地路径
     */
    @Value("${pay.alipay.certPath}")
    public static String certPath;
    /**
     * 支付宝公钥证书本地路径
     */
    @Value("${pay.alipay.alipayPublicCertPath}")
    public static String alipayPublicCertPath;
    /**
     * 支付宝根证书本地路径
     */
    @Value("${pay.alipay.rootCertPath}")
    public static String rootCertPath;

   //证书模式
    public static AlipayClient aliPayClient()  {
        CertAlipayRequest certRequest = new CertAlipayRequest();
        certRequest.setServerUrl("https://openapi.alipay.com/gateway.do");
        certRequest.setAppId(appId);
        certRequest.setPrivateKey(privateKey);
        certRequest.setFormat("json");
        certRequest.setCharset("UTF-8");
        certRequest.setSignType("RSA2");
        certRequest.setCertPath(certPath);
        certRequest.setAlipayPublicCertPath(alipayPublicCertPath);
        certRequest.setRootCertPath(rootCertPath);
        AlipayClient alipayClient = null;
        try {
            alipayClient = new DefaultAlipayClient(certRequest);
        } catch (AlipayApiException e) {
            throw new RuntimeException(e);
        }
        return  alipayClient;
    }

    //密钥的加密方式  把公钥配置在支付宝里面
    public static AlipayConfig getAlipayConfig() {
        AlipayConfig alipayConfig = new AlipayConfig();
        alipayConfig.setServerUrl("https://openapi.alipay.com/gateway.do");
        alipayConfig.setAppId(appId);
        alipayConfig.setPrivateKey(privateKey);
        alipayConfig.setFormat("json");
        alipayConfig.setAlipayPublicKey(publicKey);
        alipayConfig.setCharset("UTF-8");
        alipayConfig.setSignType("RSA2");
        return alipayConfig;
    }


}
