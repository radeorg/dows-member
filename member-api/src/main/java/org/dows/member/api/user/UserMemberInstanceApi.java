package org.dows.member.api.user;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.dows.member.request.user.UserMemberInstanceSaveRequest;
import org.dows.member.response.MemberInstanceGetResponse;
import org.springframework.web.bind.annotation.*;

@Tag(name = "会员实例", description = "user/会员实例")
@RequestMapping("/v1/user/member/instance")
public interface UserMemberInstanceApi {

//    @PostMapping("/save")
    @Operation(summary = "新增会员")
    Long save(@RequestBody UserMemberInstanceSaveRequest request);

//
//    @Operation(summary = "到期")
//    @PostMapping("/due")
//    Boolean due(@RequestParam("memberInstanceId") Long memberInstanceId);

    @GetMapping("/get")
    @Operation(summary = "获取当前登录用户会员等级详情")
    MemberInstanceGetResponse get();
}