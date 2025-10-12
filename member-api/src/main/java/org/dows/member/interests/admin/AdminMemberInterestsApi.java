package org.dows.member.interests.admin;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.dows.member.interests.admin.dto.AdminMemberInterestsSaveRequest;
import org.dows.member.interests.admin.dto.AdminMemberInterestsUpdateRequest;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Tag(name = "会员权益", description = "admin/会员权益配置")
@RequestMapping("/v1/admin/member/interests")
public interface AdminMemberInterestsApi {

    @PostMapping("/save")
    @Operation(summary = "新增会员权益")
    Long save(@RequestBody AdminMemberInterestsSaveRequest request);

    @PostMapping("/update")
    @Operation(summary = "修改会员权益")
    Boolean update(@RequestBody AdminMemberInterestsUpdateRequest request);

    @PostMapping("/disable")
    @Operation(summary = "禁用会员权益")
    Boolean disable(@RequestParam("memberInterestsId") Long memberInterestsId);

    @PostMapping("/enable")
    @Operation(summary = "启用会员权益")
    Boolean enable(@RequestParam("memberInterestsId") Long memberInterestsId);
}