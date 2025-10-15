package org.dows.member.handler.user;

import org.dows.member.entity.MemberInstanceEntity;
import org.dows.member.entity.MemberInterestsEntity;
import org.dows.member.response.MemberMetricsGetResponse;

public interface UserMemberMetricsHandler {

    /**
     * 新增或更新会员度量数据
     */
    void saveOrUpdate(MemberInstanceEntity instance, MemberInterestsEntity interests);

    /**
     * 已使用每日匹配次数加一
     */
    Boolean addUsedDailyMatchCount(Long accountInstanceId);

    /**
     * 已使用同时面试邀约次数加一
     */
    Boolean addUsedActiveInviteCount(Long accountInstanceId);

    /**
     * 已使用同时面试邀约次数减一
     */
    Boolean subUsedActiveInviteCount(Long accountInstanceId);

    /**
     * 已创建JD次数加一
     */
    Boolean addUsedCreationJdCount(Long accountInstanceId);

    /**
     * 获取账号最新一条会员度量数据
     */
    MemberMetricsGetResponse getNewest(Long accountInstanceId, String appId);
}