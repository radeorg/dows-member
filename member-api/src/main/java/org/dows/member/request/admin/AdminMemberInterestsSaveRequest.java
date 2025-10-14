package org.dows.member.request.admin;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
@Schema(description = "新增会员权益请求")
public class AdminMemberInterestsSaveRequest {

    @Schema(description = "会员等级（free免费, silver白银, gold黄金, diamond钻石）")
    private String memberType;

    @Schema(description = "会员价格")
    private Double amount;

    @Schema(description = "会员有效期天数")
    private Integer expiryDay;

    @Schema(description = "单次上传简历上限")
    private Integer singleUploadCount;

    @Schema(description = "每日匹配简历上限")
    private Integer dailyMatchCount;

    @Schema(description = "JD创建总数上限")
    private Integer creationJdCount;

    @Schema(description = "同时面试邀约上限")
    private Integer activeInterviewCount;

    @Schema(description = "邮件推送功能开关")
    private Integer emailPushEnabled;

    @Schema(description = "使用时间段")
    private String useDate;

    @Schema(description = "是否排除节假日")
    private Integer holidayExclude;

    @Schema(description = "匹配优先级")
    private Integer matchingPriority;
}