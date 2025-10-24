package org.dows.member.request.pay;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.math.BigDecimal;

@Data
@Schema(description = "支付宝统一下单请求")
public class AliPayQrCodeRequest {

    @NotBlank(message = "订单号不能为空")
    @Schema(description = "订单号")
    private String outTradeNo;

    @NotNull(message = "金额不能为空")
    @Schema(description = "支付金额")
    private BigDecimal totalAmount;

    @NotBlank(message = "商品标题不能为空")
    @Schema(description = "商品标题")
    private String subject;
}