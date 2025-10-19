package org.dows.member.pay;

import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.dows.member.api.pay.WechatPayApi;
import org.dows.member.handler.pay.WechatPayHandler;
import org.dows.member.request.pay.WechatPayQrCodeRequest;
import org.dows.member.response.WechatPayQrCodeResponse;
import org.dows.member.response.WechatPayStatusResponse;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@Tag(name = "微信支付", description = "微信支付")
@RequiredArgsConstructor
public class WechatPayRest implements WechatPayApi {
    private final WechatPayHandler wechatPayHandler;

    @Override
    public WechatPayQrCodeResponse wechatPayQrCode(WechatPayQrCodeRequest request) {
        return wechatPayHandler.wechatPayQrCode(request.getOutTradeNo(), request.getTotalAmount(), request.getDescription());
    }

    @Override
    public String wechatPayNotify(HttpServletRequest request) {
        return wechatPayHandler.wechatPayNotify(request);
    }

    @Override
    public WechatPayStatusResponse wechatPayStatus(String outTradeNo) {
        return wechatPayHandler.wechatPayStatus(outTradeNo);
    }

}