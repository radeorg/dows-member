package org.dows.member.biz.user;

import org.dows.member.response.MemberInstanceGetResponse;

import java.util.List;

public interface UserMemberInstanceHandler {

    MemberInstanceGetResponse getByAccountInstanceIdAndAppId(Long accountInstanceId, String appId);

    List<MemberInstanceGetResponse> listDueMemberInstance();
}