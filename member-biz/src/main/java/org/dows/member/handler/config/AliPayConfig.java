package org.dows.member.handler.config;

import com.alipay.api.*;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;
import org.springframework.util.StringUtils;

import java.io.IOException;

@Configuration
@RequiredArgsConstructor
public class AliPayConfig {

    private final static String HTTP_PRE = "http:";
    private final static String CLASSPATH_PRE = "classpath:";

    private final AliPayProperties aliPayProperties;

    // 证书模式
    @Bean
    public AlipayClient aliPayClient() {
        String privateKey = resolvePath(aliPayProperties.getPrivateKey());
        String certPath = resolvePath(aliPayProperties.getCertPath());
        String rootCertPath = resolvePath(aliPayProperties.getRootCertPath());
        String aliPayPublicCertPath = resolvePath(aliPayProperties.getAliPayPublicCertPath());

        CertAlipayRequest certRequest = new CertAlipayRequest();
        certRequest.setServerUrl(aliPayProperties.getServerUrl());
        certRequest.setAppId(aliPayProperties.getAppId());
        certRequest.setPrivateKey(privateKey);
        certRequest.setFormat(aliPayProperties.getFormat());
        certRequest.setCharset(aliPayProperties.getCharset());
        certRequest.setSignType(aliPayProperties.getSignType());
        certRequest.setCertPath(certPath);
        certRequest.setRootCertPath(rootCertPath);
        certRequest.setAlipayPublicCertPath(aliPayPublicCertPath);
        AlipayClient alipayClient = null;
        try {
            alipayClient = new DefaultAlipayClient(certRequest);
        } catch (AlipayApiException e) {
            throw new RuntimeException(e);
        }
        return  alipayClient;
    }

    /**
     * 解析路径
     * @param path 原始路径
     * @return 解析后的路径
     */
    public String resolvePath(String path) {
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
            ClassPathResource resource = new ClassPathResource(cleanedPath);
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
