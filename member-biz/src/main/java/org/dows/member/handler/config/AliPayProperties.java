package org.dows.member.handler.config;

import lombok.Data;
//import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Data
@Component
@ConfigurationProperties(prefix = "ali.pay")
public class AliPayProperties {

    /** 商户号 */
    private String appId;

    /** 应用私钥，由开发者自己生成 */
    private String  privateKey;

    /** 应用公钥，由支付宝生成 */
    private String publicKey;

    /** 应用公钥证书路径 */
    private String certPath;

    /** 支付宝公钥证书本地路径 */
    private String aliPayPublicCertPath;

    /** 支付宝根证书本地路径 */
    private String rootCertPath;

    /** 务器异步通知页面路径  需http://格式的完整路径，不能加?id=123这类自定义参数，必须外网可以正常访问 */
    private String notifyUrl;

    /** 支付成功跳转地址 */
    private String returnUrl;

    /** 支付宝网关（固定） */
    private String serverUrl;

    /** 参数返回格式，只支持 JSON（固定） */
    private String format = "JSON";

    /** 编码集，支持 GBK/UTF-8 */
    private String charset = "UTF-8";

    /** 生成签名字符串所使用的签名算法类型，目前支持 RSA2 算法。 */
    private String signType = "RSA2";
}
