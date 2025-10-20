package org.dows.member.request.user;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
@Schema(description = "会员实例升级请求")
public class UserMemberInstanceUpGradeRequest {

    @Schema(description = "账号ID")
    private Long accountInstanceId;

    @Schema(description = "会员权益ID")
    private Long memberInterestsId;
}