package org.dows.member.api.user;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.dows.member.response.MemberMetricsGetResponse;
import org.springframework.web.bind.annotation.*;

@Tag(name = "会员度量", description = "user/会员度量")
@RequestMapping("/v1/user/member/metrics")
public interface UserMemberMetricsApi {

    @GetMapping("/get/newest")
    @Operation(summary = "获取当前登录用户最新一条会员度量详情")
    MemberMetricsGetResponse getNewest();

    @Operation(summary = "增加已使用每日匹配次数")
    void addUsedDailyMatchCount(int matchNum);

    @Operation(summary = "已使用同时面试邀约次数加一")
    void addUsedActiveInviteCount();

    @Operation(summary = "已使用同时面试邀约次数减一")
    void subUsedActiveInviteCount();

    @Operation(summary = "已创建JD次数加一")
    void addUsedCreationJdCount();

    @Operation(summary = "简历上传权限校验")
    void validateUploadPermission(int uploadNum);

    @Operation(summary = "人岗匹配权限校验")
    void validateMatchJdPermission(int matchNum);

    @Operation(summary = "创建JD权限校验")
    void validateCreationJdPermission();

    @Operation(summary = "面试邀约权限校验")
    void validateInterviewPermission();

    @Operation(summary = "推送邮箱权限校验")
    void validatePushEmailPermission();
}