package org.dows.member.handler.pay;

import jakarta.servlet.http.HttpServletRequest;
import org.dows.member.request.pay.WechatPayQrCodeRequest;
import org.dows.member.response.pay.PayQrCodeResponse;
import org.dows.member.response.pay.WechatPayStatusResponse;

import java.util.Map;

public interface WechatPayBiz {

    PayQrCodeResponse wechatPayQrCode(WechatPayQrCodeRequest request);

    Map<String, String> wechatPayNotify(HttpServletRequest request);

    WechatPayStatusResponse wechatPayStatus(String outTradeNo);
}