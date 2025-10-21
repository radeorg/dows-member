package org.dows.member.handler.user;

import org.dows.member.entity.MemberChargeEntity;
import org.dows.member.request.user.UserMemberChargeSaveRequest;

import java.util.List;

public interface UserMemberChargeHandler {

    /**
     * 新增充值记录
     */
    MemberChargeEntity save(UserMemberChargeSaveRequest request);

    MemberChargeEntity getByPayNo(String payNo);

    /**
     * 查询未支付的订单
     */
    List<MemberChargeEntity> listNotPayMemberCharge();

    /**
     * 修改未支付的订单状态
     */
    boolean updateNotPayMemberCharge(Long memberChargeId,String state);
}