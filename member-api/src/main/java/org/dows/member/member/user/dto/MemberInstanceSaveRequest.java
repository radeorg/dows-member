package org.dows.member.member.user.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.util.Date;

@Data
@Schema(name = "MemberInstanceSaveRequest", description = "新增会员实例请求")
public class MemberInstanceSaveRequest {
    private Long accountInstanceId;
    private Long memberInterestsId;
    private String membershipType;
    private Date membershipEffectiveDate;
    private Date membershipExpiryDate;
}