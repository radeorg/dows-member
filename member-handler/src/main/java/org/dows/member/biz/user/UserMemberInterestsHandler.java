package org.dows.member.biz.user;

import org.dows.member.request.MemberInterestsListRequest;
import org.dows.member.response.MemberInterestsGetResponse;

import java.util.List;

public interface UserMemberInterestsHandler  {

    MemberInterestsGetResponse getById(Long id);

    List<MemberInterestsGetResponse> list(MemberInterestsListRequest request);
}