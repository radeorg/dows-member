package org.dows.member.request;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
@Schema(description = "会员权益查询条件")
public class MemberInterestsListRequest {

    @Schema(description = "appId")
    private String appId;

    @Schema(description = "会员等级（free免费, silver白银, gold黄金, diamond钻石）")
    private String memberType;

    @Schema(description = "状态（0正常，1禁用）")
    private Integer state;
}