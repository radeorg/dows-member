package org.dows.member.handler.user;

public interface UserMemberMetricsBiz {

    /**
     * 线程执行：新增每日会员度量信息
     */
    void saveDailyMemberMetrics(Long memberInstanceId);
}