package org.dows.member.handler.user.impl;

import cn.hutool.core.util.ObjectUtil;
import lombok.RequiredArgsConstructor;
import org.dows.member.entity.MemberChargeEntity;
import org.dows.member.enums.*;
import org.dows.member.exception.MemberException;
import org.dows.member.handler.user.UserMemberChargeBiz;
import org.dows.member.handler.user.UserMemberChargeHandler;
import org.dows.member.handler.user.UserMemberInstanceBiz;
import org.dows.member.request.user.UserMemberChargeSaveRequest;
import org.dows.member.request.user.UserMemberChargeUpdateRequest;
import org.dows.member.response.MemberChargeGetResponse;
import org.dows.member.service.MemberChargeService;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Date;

@Component
@RequiredArgsConstructor
public class UserMemberChargeBizImpl implements UserMemberChargeBiz {

    private final MemberChargeService memberChargeService;
    private final UserMemberInstanceBiz userMemberInstanceBiz;
    private final UserMemberChargeHandler userMemberChargeHandler;

    @Override
    public MemberChargeGetResponse save(UserMemberChargeSaveRequest request) {
        return userMemberChargeHandler.save(request);
    }

    @Transactional
    @Override
    public void update(UserMemberChargeUpdateRequest request) {
        MemberChargeEntity memberCharge = memberChargeService.getById(request.getMemberChargeId());
        if (memberCharge == null) {
            throw new MemberException("未查询到对应充值记录");
        }

        String state = getChargeState(memberCharge, request.getState());

        // 更新充值状态
        memberCharge.setTransactionId(request.getTransactionId());
        memberCharge.setChargeTime(payTimeToPareDate(request.getPayTime()));
        memberCharge.setState(state);
        memberChargeService.updateById(memberCharge);

        if (state.equals(MemberChargeStateEnum.SUCCESS.getCode())) {
            if (memberCharge.getChargeType().equals(MemberChargeTypeEnum.RENEWAL.getCode())) {
                userMemberInstanceBiz.renewal(memberCharge);
            } else if (memberCharge.getChargeType().equals(MemberChargeTypeEnum.UP_GRADE.getCode())) {
                userMemberInstanceBiz.upgrade(memberCharge);
            }
        }
    }

    private Date payTimeToPareDate(String successTime) {
        if (ObjectUtil.isNotEmpty(successTime)) {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ssXXX");
            ZonedDateTime zonedDateTime = ZonedDateTime.parse(successTime, formatter);
            return Date.from(zonedDateTime.toInstant());
        }
        return null;
    }

    private String getChargeState(MemberChargeEntity entity, String state) {
        if (entity.getChannel().equals(PayChannelEnum.ALI.getCode())) {
            if (state.equals(AliPayStateEnum.TRADE_SUCCESS.getCode())) {
                return MemberChargeStateEnum.SUCCESS.getCode();
            } else if (state.equals(AliPayStateEnum.TRADE_CLOSED.getCode())) {
                return MemberChargeStateEnum.CLOSED.getCode();
            }
        } else if (entity.getChannel().equals(PayChannelEnum.WECHAT.getCode())) {
            if (state.equals(WechatPayStateEnum.SUCCESS.getCode())) {
                return MemberChargeStateEnum.SUCCESS.getCode();
            } else if (state.equals(WechatPayStateEnum.CLOSED.getCode())) {
                return MemberChargeStateEnum.CLOSED.getCode();
            }
        }
        return entity.getState();
    }
}
