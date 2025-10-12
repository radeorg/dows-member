package org.dows.member.interests.admin.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
@Schema(name = "AdminMemberInterestsUpdateRequest", description = "修改会员权益请求")
public class AdminMemberInterestsUpdateRequest {
    private Long memberInterestsId;
    private String memberType;
    private Double amount;
    private Integer expiryDay;
    private Integer singleUploadCount;
    private Integer dailyMatchCount;
    private Integer creationJdCount;
    private Integer activeInterviewCount;
    private Boolean emailPushEnabled;
    private String useDate;
    private Boolean holidayExclude;
    private Integer matchingPriority;
}