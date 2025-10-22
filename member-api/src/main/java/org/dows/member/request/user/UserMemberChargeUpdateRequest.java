package org.dows.member.request.user;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
@Schema(description = "更新充值记录请求")
public class UserMemberChargeUpdateRequest {

    @Schema(description = "充值记录ID")
    private Long memberChargeId;

    @Schema(description = "支付交易ID")
    private String transactionId;

    @Schema(description = "支付时间")
    private String payTime;

    @Schema(description = "支付状态")
    private String state;
}
