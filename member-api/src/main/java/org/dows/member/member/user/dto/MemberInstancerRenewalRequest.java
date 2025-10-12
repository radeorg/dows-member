package org.dows.member.member.user.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
@Schema(name = "MemberInstancerRenewalRequest", description = "会员实例续费请求")
public class MemberInstancerRenewalRequest {
    private Long memberInstanceId;
    private Long memberInterestsId;
}