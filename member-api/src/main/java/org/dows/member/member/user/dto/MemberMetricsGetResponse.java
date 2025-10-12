package org.dows.member.member.user.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
@Schema(name = "MemberMetricsGetResponse", description = "会员每日消费度量响应")
public class MemberMetricsGetResponse {
    private Long memberMetricsId;
    private Long memberInstanceId;
    private Long accountInstanceId;
    private Long memberInterestsId;
    private Integer matchCount;
    private Integer usedMatchCount;
    private Integer inviteCount;
    private Integer usedInviteCount;
    private Integer creationJdCount;
    private Integer usedCreationJdCount;
    private Integer singleUploadCount;
}