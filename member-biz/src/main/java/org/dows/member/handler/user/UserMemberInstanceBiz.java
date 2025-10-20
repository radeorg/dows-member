package org.dows.member.handler.user;

import org.dows.member.entity.MemberChargeEntity;
import org.dows.member.request.user.UserMemberInstanceSaveRequest;

public interface UserMemberInstanceBiz {

    /**
     * 初始化免费会员
     */
    Long save(UserMemberInstanceSaveRequest request);

    /**
     * 会员升级
     */
    void upgrade(MemberChargeEntity memberCharge);

    /**
     * 会员续费
     */
    void renewal(MemberChargeEntity memberCharge);

    /**
     * 每日线程执行：会员到期
     */
    void due(Long memberInstanceId);
}