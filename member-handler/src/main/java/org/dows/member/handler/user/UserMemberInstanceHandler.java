package org.dows.member.handler.user;

import org.dows.member.response.MemberInstanceGetResponse;

public interface UserMemberInstanceHandler {

    MemberInstanceGetResponse getByMemberInstanceIdAndAppId(Long memberInstanceId, String appId);

    MemberInstanceGetResponse getByAccountInstanceIdAndAppId(Long accountInstanceId, String appId);
}