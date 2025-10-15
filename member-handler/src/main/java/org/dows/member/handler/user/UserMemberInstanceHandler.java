package org.dows.member.handler.user;

import org.dows.member.response.MemberInstanceGetResponse;

import java.util.List;

public interface UserMemberInstanceHandler {

    MemberInstanceGetResponse getByAccountInstanceIdAndAppId(Long accountInstanceId, String appId);

    List<MemberInstanceGetResponse> listDueMemberInstance();
}