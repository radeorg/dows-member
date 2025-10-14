package org.dows.member.admin;

import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.dows.member.api.admin.AdminMemberInterestsApi;
import org.dows.member.handler.admin.AdminMemberInterestsHandler;
import org.dows.member.request.MemberInterestsListRequest;
import org.dows.member.response.MemberInterestsGetResponse;
import org.dows.member.request.admin.AdminMemberInterestsSaveRequest;
import org.dows.member.request.admin.AdminMemberInterestsUpdateRequest;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@Tag(name = "会员权益", description = "admin/会员权益配置")
@RequiredArgsConstructor
public class AdminMemberInterestsRest implements AdminMemberInterestsApi {

    private final AdminMemberInterestsHandler adminMemberInterestsHandler;

    @Override
    public Long save(AdminMemberInterestsSaveRequest request) {
        return adminMemberInterestsHandler.save(request);
    }

    @Override
    public Boolean update(AdminMemberInterestsUpdateRequest request) {
        return adminMemberInterestsHandler.update(request);
    }

    @Override
    public Boolean disable(Long memberInterestsId) {
        return adminMemberInterestsHandler.disable(memberInterestsId);
    }

    @Override
    public Boolean enable(Long memberInterestsId) {
        return adminMemberInterestsHandler.enable(memberInterestsId);
    }

    @Override
    public MemberInterestsGetResponse getById(Long memberInterestsId) {
        return adminMemberInterestsHandler.getById(memberInterestsId);
    }

    @Override
    public List<MemberInterestsGetResponse> list(MemberInterestsListRequest request) {
        return adminMemberInterestsHandler.list(request);
    }
}