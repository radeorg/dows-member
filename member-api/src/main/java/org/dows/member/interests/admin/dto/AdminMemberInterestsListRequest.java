package org.dows.member.interests.admin.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
@Schema(name = "AdminMemberInterestsListRequest", description = "会员权益查询条件")
public class AdminMemberInterestsListRequest {
    private String memberType;
    private Boolean disabled;
}