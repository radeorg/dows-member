package org.dows.member.api.pay;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import org.dows.member.request.pay.PayQrCodeRequest;
import org.dows.member.response.pay.AliPayStatusResponse;
import org.dows.member.response.pay.PayQrCodeResponse;
import org.springframework.web.bind.annotation.*;

@Tag(name = "支付宝支付", description = "支付宝支付")
@RequestMapping("/v1/ali/pay")
public interface AliPayApi {

    @PostMapping("/qrcode")
    @Operation(summary = "统一下单接口/用crt证书")
    PayQrCodeResponse aliPayQrCode(@RequestBody PayQrCodeRequest request);

    @PostMapping("/notify")
    @Operation(summary = "支付结果回调处理")
    String aliPayNotify(HttpServletRequest request);

    @GetMapping("/queryStatus")
    @Operation(summary = "查询订单状态接口")
    AliPayStatusResponse aliPayStatus(@RequestParam("outTradeNo") String outTradeNo);
}