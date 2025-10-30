package org.dows.member.biz.user;

import org.dows.member.request.user.UserMemberChargeSaveRequest;
import org.dows.member.request.user.UserMemberChargeUpdateRequest;
import org.dows.member.response.MemberChargeGetResponse;

public interface UserMemberChargeBiz {

    MemberChargeGetResponse getByPaNo(String payNo);
    
    MemberChargeGetResponse save(UserMemberChargeSaveRequest request);

    void update(UserMemberChargeUpdateRequest request);

    void close(String payNo);

    MemberChargeGetResponse getWaitPayByAccountInstanceId(Long accountInstanceId);
}
