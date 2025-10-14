package org.dows.member.handler.user;

import org.dows.member.request.user.UserMemberInstanceRenewalRequest;
import org.dows.member.request.user.UserMemberInstanceSaveRequest;
import org.dows.member.request.user.UserMemberInstanceUpGradeRequest;

public interface UserMemberInstanceBiz {

    /**
     * 初始化免费会员
     */
    Long save(UserMemberInstanceSaveRequest request);

    /**
     * 会员升级
     */
    Boolean upgrade(UserMemberInstanceUpGradeRequest request);

    /**
     * 会员续费
     */
    Boolean renewal(UserMemberInstanceRenewalRequest request);

    /**
     * 每日线程执行：会员到期
     */
    Boolean expiration(Long memberInstanceId);
}