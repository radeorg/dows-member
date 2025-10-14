package org.dows.member.request.user;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
@Schema(description = "新增会员实例请求")
public class UserMemberInstanceSaveRequest {

    @Schema(description = "账号ID")
    private Long accountInstanceId;

    @Schema(description = "应用ID")
    private String appId;
}