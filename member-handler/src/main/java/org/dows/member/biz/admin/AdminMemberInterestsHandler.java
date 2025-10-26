package org.dows.member.biz.admin;

import org.dows.member.request.MemberInterestsListRequest;
import org.dows.member.request.admin.AdminMemberInterestsSaveRequest;
import org.dows.member.request.admin.AdminMemberInterestsUpdateRequest;
import org.dows.member.response.MemberInterestsGetResponse;

import java.util.List;

public interface AdminMemberInterestsHandler {

    Long save(AdminMemberInterestsSaveRequest req);

    Boolean update(AdminMemberInterestsUpdateRequest req);

    Boolean enable(Long memberInterestsId);

    Boolean disable(Long memberInterestsId);

    MemberInterestsGetResponse getById(Long id);

    List<MemberInterestsGetResponse> list(MemberInterestsListRequest request);
}