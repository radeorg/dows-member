package org.dows.member.request.user;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.math.BigDecimal;

@Data
@Schema(description = "保存充值记录请求")
public class UserMemberChargeSaveRequest {

    @Schema(description = "账号ID")
    private Long accountInstanceId;

    @Schema(description = "会员ID")
    private Long memberInstanceId;

    @Schema(description = "会员权益ID")
    private Long memberInterestsId;

    @Schema(description = "应用ID")
    private String appId;

    @Schema(description = "充值金额")
    private BigDecimal amount;

    @Schema(description = "充值类型")
    private String chargeType;

    @Schema(description = "充值渠道")
    private String channel;

    @Schema(description = "充值备注")
    private String note;
}
