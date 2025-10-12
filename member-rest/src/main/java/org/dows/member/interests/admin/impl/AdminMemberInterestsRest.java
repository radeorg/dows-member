package org.dows.member.interests.admin.impl;

import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.dows.member.handler.MemberInterestsHandler;
import org.dows.member.interests.admin.AdminMemberInterestsApi;
import org.dows.member.interests.admin.dto.AdminMemberInterestsSaveRequest;
import org.dows.member.interests.admin.dto.AdminMemberInterestsUpdateRequest;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Tag(name = "会员权益", description = "admin/会员权益配置")
@RequiredArgsConstructor
public class AdminMemberInterestsRest implements AdminMemberInterestsApi {

    private final MemberInterestsHandler memberInterestsHandler;

    @Override
    public Long save(AdminMemberInterestsSaveRequest request) {
        return memberInterestsHandler.save(request);
    }

    @Override
    public Boolean update(AdminMemberInterestsUpdateRequest request) {
        return memberInterestsHandler.update(request);
    }

    @Override
    public Boolean disable(Long memberInterestsId) {
        return memberInterestsHandler.setDisabled(memberInterestsId, true);
    }

    @Override
    public Boolean enable(Long memberInterestsId) {
        return memberInterestsHandler.setDisabled(memberInterestsId, false);
    }
}