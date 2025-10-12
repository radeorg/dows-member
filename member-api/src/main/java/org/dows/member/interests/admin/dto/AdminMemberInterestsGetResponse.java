package org.dows.member.interests.admin.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.time.OffsetDateTime;

@Data
@Schema(name = "AdminMemberInterestsGetResponse", description = "会员权益响应对象")
public class AdminMemberInterestsGetResponse {
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
    private Boolean disabled;
    private Long operatorId;
    private String operator;
    private OffsetDateTime ts;
    private OffsetDateTime ut;
}