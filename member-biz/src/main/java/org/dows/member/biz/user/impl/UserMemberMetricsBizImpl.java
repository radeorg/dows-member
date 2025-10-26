package org.dows.member.biz.user.impl;

import lombok.RequiredArgsConstructor;
import org.dows.member.entity.MemberInstanceEntity;
import org.dows.member.entity.MemberInterestsEntity;
import org.dows.member.biz.user.UserMemberMetricsBiz;
import org.dows.member.biz.user.UserMemberMetricsHandler;
import org.dows.member.service.MemberInstanceService;
import org.dows.member.service.MemberInterestsService;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
@RequiredArgsConstructor
public class UserMemberMetricsBizImpl implements UserMemberMetricsBiz {

    private final MemberInstanceService memberInstanceService;
    private final MemberInterestsService memberInterestsService;
    private final UserMemberMetricsHandler userMemberMetricsHandler;

    @Transactional
    @Override
    public void saveDailyMemberMetrics(Long memberInstanceId) {
        // 查询会员
        MemberInstanceEntity instance = getMemberInstanceById(memberInstanceId);
        if (instance != null) {
            // 查询会员权益
            MemberInterestsEntity interests = getMemberInterestsById(instance.getMemberInterestsId());
            if (interests != null && interests.getState() == 0) {
                // 新增会员度量表
                userMemberMetricsHandler.saveOrUpdate(instance, interests);
            }
        }
    }

    private MemberInterestsEntity getMemberInterestsById(Long memberInterestsId) {
        return memberInterestsService.getById(memberInterestsId);
    }

    private MemberInstanceEntity getMemberInstanceById(Long memberInstanceId) {
        return memberInstanceService.getById(memberInstanceId);
    }
}