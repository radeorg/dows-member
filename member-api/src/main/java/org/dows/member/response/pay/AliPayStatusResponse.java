package org.dows.member.response.pay;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
@Schema(description = "支付宝订单状态查询请求")
public class AliPayStatusResponse {

    @Schema(description = "商家交易号")
    private String outTradeNo;

    @Schema(description = "支付宝交易号")
    private String tradeNo;

    @Schema(description = "支付状态")
    private String tradeState;

    @Schema(description = "支付金额")
    private String totalAmount;

    @Schema(description = "信息")
    private String message;

    @Schema(description = "查询状态")
    private Boolean success;

}