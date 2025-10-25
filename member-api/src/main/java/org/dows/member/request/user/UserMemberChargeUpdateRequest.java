package org.dows.member.request.user;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Schema(description = "更新充值记录请求")
public class UserMemberChargeUpdateRequest {

    @Schema(description = "支付单号")
    private String payNo;

    @Schema(description = "支付交易ID")
    private String transactionId;

    @Schema(description = "支付时间")
    private LocalDateTime payTime;

    @Schema(description = "支付状态")
    private String state;
}
