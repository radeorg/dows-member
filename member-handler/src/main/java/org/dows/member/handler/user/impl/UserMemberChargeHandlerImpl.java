package org.dows.member.handler.user.impl;

import com.mybatisflex.core.query.QueryWrapper;
import lombok.RequiredArgsConstructor;
import org.dows.member.entity.MemberChargeEntity;
import org.dows.member.enums.MemberPayTradeState;
import org.dows.member.handler.user.UserMemberChargeHandler;
import org.dows.member.request.user.UserMemberChargeSaveRequest;
import org.dows.member.service.MemberChargeService;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class UserMemberChargeHandlerImpl implements UserMemberChargeHandler {

    private final MemberChargeService memberChargeService;

    @Override
    public MemberChargeEntity save(UserMemberChargeSaveRequest request) {
        MemberChargeEntity entity = new MemberChargeEntity();
        entity.setAppId(request.getAppId());
        entity.setAccountInstanceId(request.getAccountInstanceId());
        entity.setMemberInstanceId(request.getMemberInstanceId());
        entity.setMemberInterestsId(request.getMemberInterestsId());
        entity.setAmount(request.getAmount());
        // TODO 后面需要更改成新的订单规则
        entity.setPayNo(System.currentTimeMillis() + "");
        entity.setChannel(request.getChannel());
        entity.setNote(request.getNote());
        entity.setChargeType(request.getChargeType());
        entity.setState(MemberPayTradeState.USER_PAYING.getCode());

        memberChargeService.save(entity);

        return entity;
    }

    @Override
    public MemberChargeEntity getByPayNo(String payNo) {
        return memberChargeService.getOne(QueryWrapper.create().eq(MemberChargeEntity::getPayNo, payNo));
    }
}