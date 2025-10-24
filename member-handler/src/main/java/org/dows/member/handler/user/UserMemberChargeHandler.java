package org.dows.member.handler.user;

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

    /**
     * 修改未支付的订单状态
     */
    void updateNotPayMemberCharge(Long memberChargeId,String state);
}