package org.dows.member.member.user.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.time.OffsetDateTime;

@Data
@Schema(name = "MemberInstancerSaveRequest", description = "新增会员实例请求")
public class MemberInstancerSaveRequest {
    private Long accountInstanceId;
    private Long memberInterestsId;
    private String membershipType;
    private OffsetDateTime membershipEffectiveDate;
    private OffsetDateTime membershipExpiryDate;
}