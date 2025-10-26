package org.dows.member.biz.user;

public interface UserMemberMetricsBiz {

    /**
     * 线程执行：新增每日会员度量信息
     */
    void saveDailyMemberMetrics(Long memberInstanceId);
}