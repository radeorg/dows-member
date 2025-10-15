package org.dows.member.api.user;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.dows.member.response.MemberMetricsGetResponse;
import org.springframework.web.bind.annotation.*;

@Tag(name = "会员度量", description = "user/会员度量")
@RequestMapping("/v1/user/member/metrics")
public interface UserMemberMetricsApi {

    @Operation(summary = "已使用每日匹配次数加一")
    Boolean addUsedDailyMatchCount(Long accountInstanceId);

    @Operation(summary = "已使用同时面试邀约次数加一")
    Boolean addUsedActiveInviteCount(Long accountInstanceId);

    @Operation(summary = "已使用同时面试邀约次数减一")
    Boolean subUsedActiveInviteCount(Long accountInstanceId);

    @Operation(summary = "已创建JD次数加一")
    Boolean addUsedCreationJdCount(Long accountInstanceId);

    @GetMapping("/get/newest")
    @Operation(summary = "获取当前登录用户最新一条会员度量详情")
    MemberMetricsGetResponse getNewest();
}