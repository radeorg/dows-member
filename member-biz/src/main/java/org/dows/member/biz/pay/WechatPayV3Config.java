package org.dows.member.biz.pay;

import com.wechat.pay.java.core.Config;
import com.wechat.pay.java.core.RSAPublicKeyConfig;
import com.wechat.pay.java.core.cipher.Verifier;
import com.wechat.pay.java.core.notification.NotificationConfig;
import com.wechat.pay.java.core.notification.NotificationParser;
import com.wechat.pay.java.core.notification.RSACombinedNotificationConfig;
import com.wechat.pay.java.service.payments.nativepay.NativePayService;
import jakarta.annotation.Resource;
import lombok.RequiredArgsConstructor;
import org.dows.member.config.WechatPayProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.util.StringUtils;

import java.io.IOException;

@Configuration
@RequiredArgsConstructor
public class WechatPayV3Config {

    private WechatPayProperties wechatPayProperties;
    private Verifier verifier;

    private final static String FILE_PRE = "file:";
    private final static String HTTP_PRE = "http:";
    private final static String CLASSPATH_PRE = "classpath:";
    // 缓存公钥内容，避免重复读取证书文件
    private volatile String alipayPublicKeyCache;


    /**
     * 初始化微信支付配置（自动更新平台证书）
     */
    @Bean
    public Config wechatPayConfig() {
        String privateKeyPath = resolvePath(wechatPayProperties.getPrivateKeyPath());
//        String pubKeyPath = resolvePath(wechatPayProperties.getPubKeyPath());
        return new RSAPublicKeyConfig.Builder()
                .merchantId(wechatPayProperties.getMerchantId())
                .privateKeyFromPath(privateKeyPath)
//                .publicKeyFromPath(pubKeyPath)
//                .publicKeyId(wechatPayProperties.getPublicKeyId())
                .merchantSerialNumber(wechatPayProperties.getMerchantSerialNumber())
                .apiV3Key(wechatPayProperties.getApiV3Key())
                .build();
    }

    /**
     * 初始化Native支付服务
     */
    @Bean
    public NativePayService nativePayService(Config config) {
        return new NativePayService.Builder()
                .config(config)
                .build();
    }

//    @Bean
//    public NotificationConfig wechatValidateSignConfig() {
//        String privateKeyPath = resolvePath(wechatPayProperties.getPrivateKeyPath());
//        return new RSAPublicKeyConfig.Builder()
//                .merchantId(wechatPayProperties.getMerchantId())
//                .privateKeyFromPath(privateKeyPath)
//                .merchantSerialNumber(wechatPayProperties.getMerchantSerialNumber())
//                .apiV3Key(wechatPayProperties.getApiV3Key())
//                .build();
//    }

    /**
     * 创建通知解析器（SDK 0.2.15版本最新方式）
     */
    @Bean
    public NotificationParser notificationParser() {
        String privateKeyPath = resolvePath(wechatPayProperties.getPrivateKeyPath());
        // 创建配置对象
        NotificationConfig config = new RSACombinedNotificationConfig.Builder()
//                .merchantId(wechatPayProperties.getMerchantId())
//                .privateKeyFromPath(privateKeyPath)
                .apiV3Key(wechatPayProperties.getApiV3Key())
                .merchantSerialNumber(wechatPayProperties.getMerchantSerialNumber())

                .build();

        return new NotificationParser(config);
    }

    /**
     * 解析路径
     * @param path 原始路径
     * @return 解析后的路径
     */
    private String resolvePath(String path) {
        if (!StringUtils.hasText(path)) {
            return path;
        }

        if (path.startsWith(CLASSPATH_PRE)) {
            return resolveClasspathPath(path);
        } else if (path.startsWith(HTTP_PRE)) {
            return resolveHttpPath(path);
        }

        return path;
    }

    /**
     * 解析classpath路径
     * @param path 包含classpath前缀的路径
     * @return 解析后的文件系统路径
     */
    private String resolveClasspathPath(String path) {
        try {
            String cleanedPath = removeClasspathPrefix(path);
            org.springframework.core.io.ClassPathResource resource = new org.springframework.core.io.ClassPathResource(cleanedPath);
            if (resource.exists()) {
                return resource.getURL().getPath();
            } else {
                throw new RuntimeException("Classpath resource not found: " + cleanedPath);
            }
        } catch (IOException e) {
            throw new RuntimeException("Failed to resolve classpath path: " + path, e);
        }
    }

    /**
     * 解析HTTP路径（待实现）
     * @param path HTTP路径
     * @return 解析后的路径
     */
    private String resolveHttpPath(String path) {
        // TODO: 实现HTTP方式获取
        // 可以下载文件到临时目录并返回临时文件路径
        return path;
    }

    /**
     * 移除classpath前缀
     * @param path 原始路径
     * @return 处理后的路径
     */
    private String removeClasspathPrefix(String path) {
        if (path == null) {
            return null;
        }

        if (path.startsWith(CLASSPATH_PRE + "/")) {
            return path.substring(CLASSPATH_PRE.length() + 1);
        } else if (path.startsWith(CLASSPATH_PRE)) {
            return path.substring(CLASSPATH_PRE.length());
        }
        return path;
    }
}
