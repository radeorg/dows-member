package org.dows.member.response.pay;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@Schema(description = "微信支付订单状态查询请求")
public class WxPayStatusResponse {

    @Schema(description = "商家交易号")
    private String outTradeNo;

    @Schema(description = "微信交易号")
    private String tradeNo;

    @Schema(description = "订单金额")
    private String totalAmount;

    @Schema(description = "支付金额")
    private String payAmount;

    @Schema(description = "支付状态")
    private String tradeState;

    @Schema(description = "状态描述")
    private String tradeStateDesc;

    @Schema(description = "支付成功时间")
    private LocalDateTime sendPayDate;

    @Schema(description = "信息")
    private String message;

    @Schema(description = "查询状态")
    private Boolean success;
}