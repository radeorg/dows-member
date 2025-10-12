package org.dows.member.interests.user;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.dows.member.interests.admin.dto.AdminMemberInterestsGetResponse;
import org.dows.member.interests.admin.dto.AdminMemberInterestsListRequest;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@Tag(name = "权益查询", description = "user/权益查询")
@RequestMapping("/v1/user/member/interests")
public interface UserMemberInterestsQueryApi {

    @GetMapping("/get")
    @Operation(summary = "根据会员权益ID获取详情")
    AdminMemberInterestsGetResponse get(@RequestParam("memberInterestsId") Long memberInterestsId);

    @GetMapping("/list")
    @Operation(summary = "条件查询会员权益集合（无分页）")
    List<AdminMemberInterestsGetResponse> list(AdminMemberInterestsListRequest request);
}