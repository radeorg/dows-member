package org.dows.member.interests.admin.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
@Schema(name = "AdminMemberInterestsSaveRequest", description = "新增会员权益请求")
public class AdminMemberInterestsSaveRequest {
    @Schema(description = "会员等级")
    private String memberType;
    @Schema(description = "会员价格")
    private Double amount;
    @Schema(description = "会员有效期天数")
    private Integer expiryDay;
    private Integer singleUploadCount;
    private Integer dailyMatchCount;
    private Integer creationJdCount;
    private Integer activeInterviewCount;
    private Boolean emailPushEnabled;
    @Schema(description = "使用时间段")
    private String useDate;
    private Boolean holidayExclude;
    private Integer matchingPriority;
}