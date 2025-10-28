package org.dows.member.pay;

import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.dows.member.api.pay.WechatPayApi;
import org.dows.member.biz.pay.PaymentBiz;
//import org.dows.member.handler.pay.WechatPayBiz;
import org.dows.member.biz.pay.WechatPayBiz;
import org.dows.member.request.pay.PayQrCodeRequest;
import org.dows.member.response.pay.PayQrCodeResponse;
import org.dows.member.response.pay.WechatPayStatusResponse;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@Tag(name = "微信支付", description = "微信支付")
@RequiredArgsConstructor
public class WechatPayRest implements WechatPayApi {

    private final PaymentBiz paymentBiz;
    private final WechatPayBiz wechatPayBiz;

    @Override
    public PayQrCodeResponse wechatPayQrCode(PayQrCodeRequest request) {
        return paymentBiz.createNativePayment(request);
    }

    @Override
    public Map<String, String> wechatPayNotify(HttpServletRequest request) {
//        return wechatPayBiz.wechatPayNotify(request);
        return null;
    }

    @Override
    public WechatPayStatusResponse wechatPayStatus(String outTradeNo) {
        return null;
    }
}