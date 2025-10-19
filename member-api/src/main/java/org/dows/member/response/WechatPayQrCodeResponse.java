package org.dows.member.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
@Schema(description = "微信支付统一下单响应")
public class WechatPayQrCodeResponse {

    @Schema(description = "订单号")
    private String outTradeNo;

    @Schema(description = "二维码链接")
    private String qrCode;
}