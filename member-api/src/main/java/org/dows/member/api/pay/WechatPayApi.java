package org.dows.member.api.pay;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import org.dows.member.request.pay.WechatPayQrCodeRequest;
import org.dows.member.response.WechatPayQrCodeResponse;
import org.dows.member.response.WechatPayStatusResponse;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name = "微信支付", description = "微信支付")
@RequestMapping("/v1/wechat/pay")
public interface WechatPayApi {

    @PostMapping("/qrcode")
    @Operation(summary = "统一下单接口")
    WechatPayQrCodeResponse wechatPayQrCode(@RequestBody WechatPayQrCodeRequest request);

    @PostMapping("/notify")
    @Operation(summary = "支付结果回调处理")
    String wechatPayNotify(HttpServletRequest request);

    @PostMapping("/queryStatus")
    @Operation(summary = "查询订单状态接口")
    WechatPayStatusResponse wechatPayStatus(@RequestParam("outTradeNo") String outTradeNo);
}