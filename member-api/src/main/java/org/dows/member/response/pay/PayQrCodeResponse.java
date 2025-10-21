package org.dows.member.response.pay;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
@Schema(description = "微信支付统一下单响应")
public class PayQrCodeResponse {

    @Schema(description = "订单号")
    private String outTradeNo;

    @Schema(description = "二维码链接")
    private String qrCode;

    @Schema(description = "过期时间")
    private String expirationTime;
}