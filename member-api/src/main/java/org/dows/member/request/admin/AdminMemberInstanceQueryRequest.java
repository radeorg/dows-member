package org.dows.member.request.admin;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
@Schema(description = "修改会员权益请求")
public class AdminMemberInstanceQueryRequest {

    @Schema(description = "账号ID")
    private Long accountInstanceId;

    @Schema(description = "会员ID")
    private Long memberInstanceId;
}