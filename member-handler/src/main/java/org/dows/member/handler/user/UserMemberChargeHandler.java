package org.dows.member.handler.user;

import org.dows.member.entity.MemberChargeEntity;
import org.dows.member.request.user.UserMemberChargeSaveRequest;

public interface UserMemberChargeHandler {

    /**
     * 新增充值记录
     */
    MemberChargeEntity save(UserMemberChargeSaveRequest request);

    MemberChargeEntity getByPayNo(String payNo);
}