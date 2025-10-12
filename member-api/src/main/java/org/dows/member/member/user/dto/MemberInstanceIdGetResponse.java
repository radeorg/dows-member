package org.dows.member.member.user.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.time.OffsetDateTime;

@Data
@Schema(name = "MemberInstanceIdGetResponse", description = "会员实例详情响应")
public class MemberInstanceIdGetResponse {
    private Long memberInstanceId;
    private Long accountInstanceId;
    private Long memberInterestsId;
    private String membershipType;
    private OffsetDateTime membershipEffectiveDate;
    private OffsetDateTime membershipExpiryDate;
}