package org.dows.member.pay;

import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.dows.member.api.pay.AliPayApi;
import org.dows.member.handler.pay.AliPayHandler;
import org.dows.member.request.pay.AliPayQrCodeRequest;
import org.dows.member.response.AliPayQrCodeResponse;
import org.dows.member.response.AliPayStatusResponse;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Tag(name = "支付宝支付", description = "支付宝支付")
@RequiredArgsConstructor
public class AliPayRest implements AliPayApi {
    private final AliPayHandler aliPayHandler;



    @Override
    public AliPayQrCodeResponse aliPayQrCode(AliPayQrCodeRequest request) {
        return aliPayHandler.aliPayQrCode( request.getTotalAmount(), request.getDescription());
    }

    @Override
    public AliPayQrCodeResponse aliPayQrCode1(AliPayQrCodeRequest request) {
        return aliPayHandler.aliPayQrCode( request.getTotalAmount(), request.getDescription());
    }

    @Override
    public String aliPayNotify(HttpServletRequest request) {
        return aliPayHandler.aliPayNotify(request);
    }

    @Override
    public AliPayStatusResponse aliPayStatus(String outTradeNo) {
        return aliPayHandler.aliPayStatus(outTradeNo);
    }
}