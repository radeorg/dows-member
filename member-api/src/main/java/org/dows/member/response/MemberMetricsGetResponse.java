package org.dows.member.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.util.Date;

@Data
@Schema(description = "会员每日消费度量响应")
public class MemberMetricsGetResponse {

    @Schema(description = "会员每日消费度量ID")
    private Long memberMetricsId;

    @Schema(description = "会员实例ID")
    private Long memberInstanceId;

    @Schema(description = "账号实例ID")
    private Long accountInstanceId;

    @Schema(description = "会员权益ID")
    private Long memberInterestsId;

    @Schema(description = "每日可使用匹配简历上限")
    private Integer dailyMatchCount;

    @Schema(description = "已使用匹配简历数")
    private Integer usedMatchCount;

    @Schema(description = "可使用同时面试邀约上限")
    private Integer activeInterviewCount;

    @Schema(description = "已使用邀约数")
    private Integer usedInterviewCount;

    @Schema(description = "可创建JD上限")
    private Integer creationJdCount;

    @Schema(description = "已创建JD数")
    private Integer usedCreationJdCount;

    @Schema(description = "单次上传简历上限")
    private Integer singleUploadCount;

    @Schema(description = "appId")
    private String appId;

    @Schema(description = "创建时间")
    private Date ts;

    @Schema(description = "最后更新时间")
    private Date ut;
}