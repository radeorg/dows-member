package org.dows.member.config;
import com.wechat.pay.java.core.Config;
import com.wechat.pay.java.core.RSAAutoCertificateConfig;
import com.wechat.pay.java.service.payments.nativepay.NativePayService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.io.InputStream;

@Configuration
public class WxPayConfig {
    /**
     * 商户号
     */
    public static String merchantId = "190000** **";
    /**
     * 商户API私钥路径
     */
    @Value("${pay.weixin.privateKeyPath}")
    public  String privateKeyPath;
    /**
     * 商户证书序列号
     */
    public static String merchantSerialNumber = "5157F09EFDC096DE15EBE81A47057A72****** **";
    /**
     * 商户APIV3密钥
     */
    public static String apiV3key = "your-api-v3-key";

    @Bean
    public NativePayService nativePayService() {
        Config config = new RSAAutoCertificateConfig.Builder()
                .merchantId(merchantId)
                .privateKeyFromPath(privateKeyPath)
                .merchantSerialNumber(merchantSerialNumber)
                .apiV3Key(apiV3key)
                .build();
        return new NativePayService.Builder().config(config).build();
    }

}
