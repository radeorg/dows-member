package org.dows.member.interests.user.impl;

import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.dows.member.handler.MemberInterestsQueryHandler;
import org.dows.member.interests.admin.dto.AdminMemberInterestsGetResponse;
import org.dows.member.interests.admin.dto.AdminMemberInterestsListRequest;
import org.dows.member.interests.user.UserMemberInterestsQueryApi;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@Tag(name = "权益查询", description = "user/权益查询")
@RequiredArgsConstructor
public class UserMemberInterestsQueryRest implements UserMemberInterestsQueryApi {

    private final MemberInterestsQueryHandler queryHandler;

    @Override
    public AdminMemberInterestsGetResponse get(Long memberInterestsId) {
        return queryHandler.get(memberInterestsId);
    }

    @Override
    public List<AdminMemberInterestsGetResponse> list(AdminMemberInterestsListRequest request) {
        return queryHandler.list(request);
    }
}