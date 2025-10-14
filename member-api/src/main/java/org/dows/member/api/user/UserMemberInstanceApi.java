package org.dows.member.api.user;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.dows.member.request.user.UserMemberInstanceSaveRequest;
import org.dows.member.request.user.UserMemberInstanceRenewalRequest;
import org.dows.member.request.user.UserMemberInstanceUpGradeRequest;
import org.dows.member.response.MemberInstanceGetResponse;
import org.springframework.web.bind.annotation.*;

@Tag(name = "会员实例", description = "user/会员实例")
@RequestMapping("/v1/user/member/instance")
public interface UserMemberInstanceApi {

    @PostMapping("/save")
    @Operation(summary = "新增会员实例")
    Long save(@RequestBody UserMemberInstanceSaveRequest request);

    @PostMapping("/upgrade")
    @Operation(summary = "升级")
    Boolean upgrade(@RequestBody UserMemberInstanceUpGradeRequest request);

    @PostMapping("/renewal")
    @Operation(summary = "续费")
    Boolean renewal(@RequestBody UserMemberInstanceRenewalRequest request);

    @Operation(summary = "到期")
    @PostMapping("/expiration")
    Boolean expiration(@RequestParam("memberInstanceId") Long memberInstanceId);

    @GetMapping("/get")
    @Operation(summary = "根据会员实例ID获取会员实例详情")
    MemberInstanceGetResponse getById(@RequestParam("memberInstanceId") Long memberInstanceId);

    @GetMapping("/getByAccountId")
    @Operation(summary = "根据账号实例ID获取会员实例详情")
    MemberInstanceGetResponse getByAccountInstanceId(@RequestParam("accountInstanceId") Long accountInstanceId);
}