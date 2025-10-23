package org.dows.member.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;

@Data
@Schema(description = "会员充值详情响应")
public class MemberChargeGetResponse {

    @Schema(description = "会员充值ID")
    private Long memberChargeId;

    @Schema(description = "会员权益ID")
    private Long memberInstanceId;

    @Schema(description = "账号ID")
    private Long accountInstanceId;

    @Schema(description = "会员权益ID")
    private Long memberInterestsId;

    @Schema(description = "充值金额")
    private BigDecimal amount;

    @Schema(description = "支付号")
    private String payNo;

    @Schema(description = "第三方交易号）")
    private String transactionId;

    @Schema(description = "支付通道(wechat/alipay/bank)")
    private String channel;

    @Schema(description = "充值状态(WAIT_PAY,SUCCESS,FAILED,REFUNDED,CLOSED)")
    private String state;

    @Schema(description = "充值类型（upgrade-升级, renewal-续费）")
    private String chargeType;

    @Schema(description = "充值备注")
    private String note;

    @Schema(description = "创建时间")
    private Date ts;
}