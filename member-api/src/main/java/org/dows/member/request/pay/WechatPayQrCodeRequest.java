package org.dows.member.request.pay;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.math.BigDecimal;

@Data
@Schema(description = "微信支付统一下单请求")
public class WechatPayQrCodeRequest {

    @Schema(description = "订单号")
    private String outTradeNo;

    @Schema(description = "支付金额")
    private BigDecimal totalAmount;

    @Schema(description = "描述")
    private  String description;

    @Schema(description = "客户端IP")
    private String clientIp;
}