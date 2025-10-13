package org.dows.member.member.user;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.dows.member.member.user.dto.*;
import org.springframework.web.bind.annotation.*;

@Tag(name = "会员实例", description = "user/会员实例")
@RequestMapping("/v1/user/member/instance")
public interface UserMemberInstanceApi {

    @PostMapping("/save")
    @Operation(summary = "新增会员实例")
    Long save(@RequestBody MemberInstanceSaveRequest request);

    @PostMapping("/upgrade")
    @Operation(summary = "升级")
    Boolean upgrade(@RequestBody MemberInstancerUpGradeRequest request);

    @PostMapping("/renewal")
    @Operation(summary = "续费")
    Boolean renewal(@RequestBody MemberInstancerRenewalRequest request);

    @GetMapping("/get")
    @Operation(summary = "根据会员实例ID获取会员实例详情")
    MemberInstanceIdGetResponse get(@RequestParam("memberInstanceId") Long memberInstanceId);

    @GetMapping("/metrics")
    @Operation(summary = "根据会员实例ID查询最新一条会员每日消费度量信息")
    MemberMetricsGetResponse metrics(@RequestParam("memberInstanceId") Long memberInstanceId);
}