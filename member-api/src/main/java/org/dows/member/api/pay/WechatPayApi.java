package org.dows.member.api.pay;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import org.dows.member.request.pay.PayQrCodeRequest;
import org.dows.member.response.pay.PayQrCodeResponse;
import org.dows.member.response.pay.WxPayStatusResponse;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.Map;

@Tag(name = "微信支付", description = "微信支付")
@RequestMapping("/v1/wx/pay")
public interface WechatPayApi {

    @PostMapping("/qrcode")
    @Operation(summary = "统一下单接口")
    PayQrCodeResponse wechatPayQrCode(@RequestBody PayQrCodeRequest request);

    @PostMapping("/notify")
    @Operation(summary = "支付结果回调处理")
    Map<String, String> wechatPayNotify(HttpServletRequest request) throws IOException;

    @GetMapping("/queryStatus")
    @Operation(summary = "查询订单状态接口")
    WxPayStatusResponse wechatPayStatus(@RequestParam("outTradeNo") String outTradeNo);
}