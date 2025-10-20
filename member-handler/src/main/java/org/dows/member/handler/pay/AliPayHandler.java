package org.dows.member.handler.pay;

import jakarta.servlet.http.HttpServletRequest;
import org.dows.member.response.AliPayQrCodeResponse;
import org.dows.member.response.AliPayStatusResponse;
import org.dows.member.response.WechatPayQrCodeResponse;
import org.dows.member.response.WechatPayStatusResponse;

import java.math.BigDecimal;

public interface AliPayHandler {

    AliPayQrCodeResponse aliPayQrCode(BigDecimal totalAmount, String description);

    AliPayQrCodeResponse aliPayQrCode1(BigDecimal totalAmount, String description);

    String aliPayNotify(HttpServletRequest request);

    AliPayStatusResponse aliPayStatus(String outTradeNo);
}