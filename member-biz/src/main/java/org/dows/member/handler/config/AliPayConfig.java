package org.dows.member.handler.config;

import com.alipay.api.*;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.FileSystemResource;

import java.io.IOException;

@Configuration
@RequiredArgsConstructor
public class AliPayConfig {

    private final AliPayProperties aliPayProperties;

    // 证书模式
    @Bean
    public AlipayClient aliPayClient() throws IOException {

        String certPath = aliPayProperties.getCertPath();
        if(certPath.startsWith("classpath:")){
            certPath = new ClassPathResource(aliPayProperties.getCertPath()).getURL().getPath();
        }else if(certPath.startsWith("file:")){
            certPath = new FileSystemResource(certPath).getURL().getPath();
        }
        String rootCertPath = aliPayProperties.getRootCertPath();
        if(rootCertPath.startsWith("classpath:")){
            rootCertPath= new ClassPathResource(aliPayProperties.getRootCertPath()).getURL().getPath();
        }else if(rootCertPath.startsWith("file:")){
            rootCertPath= new FileSystemResource(aliPayProperties.getRootCertPath()).getURL().getPath();;
        }

        String aliPayPublicCertPath = aliPayProperties.getAliPayPublicCertPath();
        if(aliPayPublicCertPath.startsWith("classpath:")){
            aliPayPublicCertPath = new ClassPathResource(aliPayProperties.getAliPayPublicCertPath()).getURI().getPath();
        }else if(aliPayPublicCertPath.startsWith("file:")){
            rootCertPath= new FileSystemResource(aliPayPublicCertPath).getURL().getPath();;
        }
        CertAlipayRequest certRequest = new CertAlipayRequest();
        certRequest.setServerUrl(aliPayProperties.getServerUrl());
        certRequest.setAppId(aliPayProperties.getAppId());
        certRequest.setPrivateKey(aliPayProperties.getPrivateKey());
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
}
