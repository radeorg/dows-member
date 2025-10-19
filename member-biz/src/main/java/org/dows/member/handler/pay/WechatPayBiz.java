package org.dows.member.handler.pay;

public interface WechatPayBiz {

    /**
     * 线程执行：新增每日会员度量信息
     */
    void saveDailyMemberMetrics(Long memberInstanceId);
}