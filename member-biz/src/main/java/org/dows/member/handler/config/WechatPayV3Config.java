//package org.dows.member.handler.config;
//
//import cn.hutool.core.io.resource.ClassPathResource;
//import com.wechat.pay.java.core.Config;
//import com.wechat.pay.java.core.RSAAutoCertificateConfig;
//import com.wechat.pay.java.core.RSAPublicKeyConfig;
//import com.wechat.pay.java.core.notification.NotificationConfig;
//import com.wechat.pay.java.core.notification.NotificationParser;
//import com.wechat.pay.java.core.notification.RSACombinedNotificationConfig;
//import com.wechat.pay.java.service.payments.nativepay.NativePayService;
//import jakarta.annotation.Resource;
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//
//@Configuration
//public class WechatPayV3Config {
//
//    @Resource
//    private WechatPayProperties wechatPayProperties;
//
//    /**
//     * 初始化微信支付配置（自动更新平台证书）
//     */
//    @Bean
//    public Config wechatPayConfig() {
//        ClassPathResource resource = new ClassPathResource(wechatPayProperties.getPrivateKeyPath());
//        String path = resource.getUrl().getPath();
//        return null;
////        return new RSAAutoCertificateConfig.Builder()
////                .merchantId(wechatPayProperties.getMerchantId())
//////                .privateKeyFromPath(wechatPayProperties.getPrivateKeyPath())
////                .privateKeyFromPath(path)
////                .merchantSerialNumber(wechatPayProperties.getMerchantSerialNumber())
////                .apiV3Key(wechatPayProperties.getApiV3Key())
////                .build();
//    }
//
//    /**
//     * 初始化Native支付服务
//     */
//    @Bean
//    public NativePayService nativePayService(Config config) {
//        return new NativePayService.Builder()
//                .config(config)
//                .build();
//    }
//
//    @Bean
//    public NotificationConfig wechatValidateSignConfig() {
//        return new RSAPublicKeyConfig.Builder()
//                .merchantId(wechatPayProperties.getMerchantId())
//                .privateKeyFromPath(wechatPayProperties.getPrivateKeyPath())
//                .merchantSerialNumber(wechatPayProperties.getMerchantSerialNumber())
//                .apiV3Key(wechatPayProperties.getApiV3Key())
//                .build();
//    }
//
//    /**
//     * 创建通知解析器（SDK 0.2.15版本最新方式）
//     */
//    @Bean
//    public NotificationParser notificationParser() {
//        // 创建配置对象
//        NotificationConfig config = new RSACombinedNotificationConfig.Builder()
//                .merchantId(wechatPayProperties.getMerchantId())
//                .privateKeyFromPath(wechatPayProperties.getPrivateKeyPath())
//                .merchantSerialNumber(wechatPayProperties.getMerchantSerialNumber())
//                .apiV3Key(wechatPayProperties.getApiV3Key())
//                .build();
//
//        return new NotificationParser(config);
//    }
//}
