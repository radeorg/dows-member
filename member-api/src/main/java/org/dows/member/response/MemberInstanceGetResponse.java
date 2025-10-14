package org.dows.member.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.util.Date;

@Data
@Schema(description = "会员实例详情响应")
public class MemberInstanceGetResponse {

    @Schema(description = "会员权益ID")
    private Long memberInstanceId;

    @Schema(description = "账号ID")
    private Long accountInstanceId;

    @Schema(description = "会员权益ID")
    private Long memberInterestsId;

    @Schema(description = "会员等级（free免费, silver白银, gold黄金, diamond钻石）")
    private String memberType;

    @Schema(description = "会员生效日期（为空代表免费会员）")
    private Date effectiveDate;

    @Schema(description = "会员过期日期（为空代表永久会员）")
    private Date expiryDate;
}