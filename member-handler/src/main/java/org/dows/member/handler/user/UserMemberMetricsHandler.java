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
     * 增加已使用每日匹配次数
     * @param matchNum 当前匹配个数
     */
    Boolean addUsedDailyMatchCount(Long accountInstanceId, int matchNum);

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

    /**
     * 文件上传校验
     * @param uploadNum 上传文件个数
     */
    void validateUploadPermission(Long accountInstanceId, int uploadNum);

    /**
     * 人岗匹配权限校验
     * @param matchNum 本次匹配条数
     */
    void validateMatchJdPermission(Long accountInstanceId, int matchNum);

    /**
     * 创建JD权限校验
     */
    void validateCreationJdPermission(Long accountInstanceId);

    /**
     * 面试邀约权限校验
     */
    void validateInterviewPermission(Long accountInstanceId);

    /**
     * 邮箱推送权限校验
     */
    void validatePushEmailPermission(Long accountInstanceId);
}