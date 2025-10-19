package org.dows.member.handler.pay;

import jakarta.servlet.http.HttpServletRequest;
import org.dows.member.response.WechatPayQrCodeResponse;
import org.dows.member.response.WechatPayStatusResponse;

import java.math.BigDecimal;

public interface WechatPayHandler {

    WechatPayQrCodeResponse wechatPayQrCode(String outTradeNo, BigDecimal totalAmount, String description);

    String wechatPayNotify(HttpServletRequest request);

    WechatPayStatusResponse wechatPayStatus(String outTradeNo);
}