package org.dows.member.request.user;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
@Schema(description = "会员实例续费请求")
public class UserMemberInstanceRenewalRequest {

    @Schema(description = "账号ID")
    private Long accountInstanceId;

    @Schema(description = "会员权益ID")
    private Long memberInterestsId;

    @Schema(description = "应用ID")
    private String appId;
}