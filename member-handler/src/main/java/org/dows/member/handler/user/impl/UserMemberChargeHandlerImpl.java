package org.dows.member.handler.user.impl;

import com.mybatisflex.core.query.QueryWrapper;
import lombok.RequiredArgsConstructor;
import org.dows.member.entity.MemberChargeEntity;
import org.dows.member.enums.MemberChargeStateEnum;
import org.dows.member.handler.user.UserMemberChargeHandler;
import org.dows.member.request.user.UserMemberChargeSaveRequest;
import org.dows.member.service.MemberChargeService;
import org.springframework.stereotype.Component;

import java.util.List;

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
        entity.setState(MemberChargeStateEnum.WAIT_PAY.getCode());

        memberChargeService.save(entity);

        return entity;
    }

    @Override
    public List<MemberChargeEntity> listNotPayMemberCharge() {
        return memberChargeService.list(QueryWrapper.create().eq(MemberChargeEntity::getState, MemberChargeStateEnum.WAIT_PAY.getCode()));
    }

    @Override
    public boolean updateNotPayMemberCharge(Long memberChargeId,String state) {
        MemberChargeEntity memberChargeEntity = new MemberChargeEntity();
        memberChargeEntity.setMemberChargeId(memberChargeId);
        memberChargeEntity.setState(state);
        return  memberChargeService.update(memberChargeEntity);
    }
}