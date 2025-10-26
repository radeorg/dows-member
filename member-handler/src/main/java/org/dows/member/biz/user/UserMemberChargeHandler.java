package org.dows.member.biz.user;

import org.dows.member.entity.MemberChargeEntity;
import org.dows.member.request.user.UserMemberChargeSaveRequest;
import org.dows.member.response.MemberChargeGetResponse;

import java.util.List;

public interface UserMemberChargeHandler {

    /**
     * 新增充值记录
     */
    MemberChargeGetResponse save(UserMemberChargeSaveRequest request);

    /**
     * 查询未支付的订单
     */
    List<MemberChargeEntity> listDuePayMemberCharge();
}