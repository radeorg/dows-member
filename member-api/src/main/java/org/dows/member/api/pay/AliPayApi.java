package org.dows.member.api.pay;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import org.dows.member.request.pay.AliPayQrCodeRequest;
import org.dows.member.response.AliPayQrCodeResponse;
import org.dows.member.response.AliPayStatusResponse;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Tag(name = "支付宝支付", description = "支付宝支付")
@RequestMapping("/v1/ali/pay")
public interface AliPayApi {

    @PostMapping("/qrcode")
    @Operation(summary = "统一下单接口/用crt证书")
    AliPayQrCodeResponse aliPayQrCode(@RequestBody AliPayQrCodeRequest request);

    @PostMapping("/qrcode1")
    @Operation(summary = "统一下单接口/后面删除直接用公钥和私钥")
    AliPayQrCodeResponse aliPayQrCode1(@RequestBody AliPayQrCodeRequest request);


    @PostMapping("/notify")
    @Operation(summary = "支付结果回调处理")
    String aliPayNotify(HttpServletRequest request);

    @PostMapping("/queryStatus")
    @Operation(summary = "查询订单状态接口")
    AliPayStatusResponse aliPayStatus(@RequestParam("outTradeNo") String outTradeNo);
}