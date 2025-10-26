package org.dows.member.biz.user.impl;

import cn.hutool.core.bean.BeanUtil;
import com.mybatisflex.core.query.QueryWrapper;
import lombok.RequiredArgsConstructor;
import org.dows.member.entity.MemberChargeEntity;
import org.dows.member.enums.MemberChargeStateEnum;
import org.dows.member.biz.user.UserMemberChargeHandler;
import org.dows.member.request.user.UserMemberChargeSaveRequest;
import org.dows.member.response.MemberChargeGetResponse;
import org.dows.member.service.MemberChargeService;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.List;

@Component
@RequiredArgsConstructor
public class UserMemberChargeHandlerImpl implements UserMemberChargeHandler {

    private final MemberChargeService memberChargeService;

    @Override
    public MemberChargeGetResponse save(UserMemberChargeSaveRequest request) {
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

        return BeanUtil.copyProperties(entity, MemberChargeGetResponse.class);
    }

    @Override
    public List<MemberChargeEntity> listDuePayMemberCharge() {
        // 获取当前时间
        LocalDateTime now = LocalDateTime.now();
        // 减去N分钟
        LocalDateTime fifteenMinutesAgo = now.minusMinutes(2);

        // 查询小于等于15分钟还未支付的订单
        return memberChargeService.list(QueryWrapper.create()
                .eq(MemberChargeEntity::getState, MemberChargeStateEnum.WAIT_PAY.getCode())
                .le(MemberChargeEntity::getTs, fifteenMinutesAgo));
    }
}