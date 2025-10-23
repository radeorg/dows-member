package org.dows.member.handler.user;

import org.dows.member.request.user.UserMemberChargeSaveRequest;
import org.dows.member.request.user.UserMemberChargeUpdateRequest;
import org.dows.member.response.MemberChargeGetResponse;

public interface UserMemberChargeBiz {

    MemberChargeGetResponse save(UserMemberChargeSaveRequest request);

    void update(UserMemberChargeUpdateRequest request);
}
