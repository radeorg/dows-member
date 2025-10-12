package org.dows.member.member.user.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
@Schema(name = "MemberInstancerUpGradeRequest", description = "会员实例升级请求")
public class MemberInstancerUpGradeRequest {
    private Long memberInstanceId;
    private Long memberInterestsId;
}