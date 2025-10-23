package org.dows.member.handler.config;

import cn.hutool.core.io.resource.ClassPathResource;
import com.alipay.api.*;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@RequiredArgsConstructor
public class AliPayConfig {

    private final AliPayProperties aliPayProperties;

    // 证书模式
    @Bean
    public AlipayClient aliPayClient() {
        ClassPathResource certPath = new ClassPathResource(aliPayProperties.getCertPath());
        ClassPathResource publicCertPath = new ClassPathResource(aliPayProperties.getAliPayPublicCertPath());
        ClassPathResource rootCertPath = new ClassPathResource(aliPayProperties.getRootCertPath());

        CertAlipayRequest certRequest = new CertAlipayRequest();
        certRequest.setServerUrl(aliPayProperties.getServerUrl());
        certRequest.setAppId(aliPayProperties.getAppId());
        certRequest.setPrivateKey(aliPayProperties.getPrivateKey());
        certRequest.setFormat(aliPayProperties.getFormat());
        certRequest.setCharset(aliPayProperties.getCharset());
        certRequest.setSignType(aliPayProperties.getSignType());
        certRequest.setCertPath(certPath.getUrl().getPath());
        certRequest.setAlipayPublicCertPath(publicCertPath.getUrl().getPath());
        certRequest.setRootCertPath(rootCertPath.getUrl().getPath());
//        certRequest.setCertPath(aliPayProperties.getCertPath());
//        certRequest.setAlipayPublicCertPath(aliPayProperties.getAliPayPublicCertPath());
//        certRequest.setRootCertPath(aliPayProperties.getRootCertPath());
        AlipayClient alipayClient = null;
        try {
            alipayClient = new DefaultAlipayClient(certRequest);
        } catch (AlipayApiException e) {
            throw new RuntimeException(e);
        }
        return alipayClient;
    }
}
