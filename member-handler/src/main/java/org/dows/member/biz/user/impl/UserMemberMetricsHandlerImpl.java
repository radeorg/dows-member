package org.dows.member.biz.user.impl;

import cn.hutool.core.bean.BeanUtil;
import com.mybatisflex.core.query.QueryWrapper;
import lombok.RequiredArgsConstructor;
import org.dows.member.constant.MemberExceptionStatusCode;
import org.dows.member.entity.MemberInstanceEntity;
import org.dows.member.entity.MemberInterestsEntity;
import org.dows.member.entity.MemberMetricsEntity;
import org.dows.member.exception.MemberException;
import org.dows.member.biz.user.UserMemberMetricsHandler;
import org.dows.member.biz.util.CommonUtils;
import org.dows.member.response.MemberMetricsGetResponse;
import org.dows.member.service.MemberMetricsService;
import org.springframework.stereotype.Component;

import java.util.Objects;

@Component
@RequiredArgsConstructor
public class UserMemberMetricsHandlerImpl implements UserMemberMetricsHandler {

    public final MemberMetricsService memberMetricsService;

    @Override
    public void saveOrUpdate(MemberInstanceEntity instance, MemberInterestsEntity interests) {
        MemberMetricsEntity entity = new MemberMetricsEntity();
        entity.setAppId(instance.getAppId());
        entity.setAccountInstanceId(instance.getAccountInstanceId());
        entity.setMemberInstanceId(instance.getMemberInstanceId());
        entity.setMemberInterestsId(instance.getMemberInterestsId());
        entity.setDailyMatchCount(interests.getDailyMatchCount());
        entity.setActiveInterviewCount(interests.getActiveInterviewCount());
        entity.setCreationJdCount(interests.getCreationJdCount());
        entity.setSingleUploadCount(interests.getSingleUploadCount());
        entity.setEmailPushEnabled(interests.getEmailPushEnabled());
        entity.setUseDate(interests.getUseDate());
        entity.setHolidayExclude(interests.getHolidayExclude());

        // 查询最新一条会员度量数据
        MemberMetricsEntity metrics = getNewestByAccountInstanceId(instance.getAccountInstanceId());
        if (metrics == null) {
            memberMetricsService.save(entity);
        } else {
            if (!CommonUtils.isToday(metrics.getTs())) {
                // 如果今日未生成校验数据，将历史使用的次数进行赋值
                entity.setUsedMatchCount(metrics.getUsedMatchCount());
                entity.setUsedInterviewCount(metrics.getUsedInterviewCount());
                entity.setUsedCreationJdCount(metrics.getUsedCreationJdCount());

                memberMetricsService.save(entity);
            } else {
                // 如果今日已生成校验数据（比如在线程执行期间进行了续费等操作），进行更新
                metrics.setActiveInterviewCount(interests.getActiveInterviewCount());
                metrics.setDailyMatchCount(interests.getDailyMatchCount());
                metrics.setSingleUploadCount(interests.getSingleUploadCount());
                metrics.setCreationJdCount(interests.getCreationJdCount());
                metrics.setEmailPushEnabled(interests.getEmailPushEnabled());
                metrics.setUseDate(interests.getUseDate());
                metrics.setHolidayExclude(interests.getHolidayExclude());

                memberMetricsService.updateById(metrics);
            }
        }
    }

    @Override
    public void addUsedDailyMatchCount(Long accountInstanceId, int matchNum) {
        MemberMetricsEntity entity = getNewestByAccountInstanceId(accountInstanceId);

        isExist(entity);

        validateMatch(entity, matchNum);

        entity.setUsedMatchCount(entity.getUsedMatchCount() + matchNum);

        memberMetricsService.updateById(entity);
    }

    @Override
    public void addUsedActiveInviteCount(Long accountInstanceId) {
        MemberMetricsEntity entity = getNewestByAccountInstanceId(accountInstanceId);

        isExist(entity);

        validateInterview(entity);

        entity.setUsedInterviewCount(entity.getUsedInterviewCount() + 1);

        memberMetricsService.updateById(entity);
    }

    @Override
    public void subUsedActiveInviteCount(Long accountInstanceId) {
        MemberMetricsEntity entity = getNewestByAccountInstanceId(accountInstanceId);

        isExist(entity);

        if (entity.getUsedInterviewCount() > 0) {
            entity.setUsedInterviewCount(entity.getUsedInterviewCount() - 1);

            memberMetricsService.updateById(entity);
        }
    }

    @Override
    public void addUsedCreationJdCount(Long accountInstanceId) {
        MemberMetricsEntity entity = getNewestByAccountInstanceId(accountInstanceId);

        isExist(entity);

        validateCreateJd(entity);

        entity.setUsedCreationJdCount(entity.getUsedCreationJdCount() + 1);

        memberMetricsService.updateById(entity);
    }

    @Override
    public MemberMetricsGetResponse getNewest(Long accountInstanceId, String appId) {
        MemberMetricsEntity entity = memberMetricsService.getOne(QueryWrapper.create()
                .eq(MemberMetricsEntity::getAccountInstanceId, accountInstanceId)
                .eq(MemberMetricsEntity::getAppId, appId)
                .orderBy(MemberMetricsEntity::getTs).desc());

        isExist(entity);

        return BeanUtil.copyProperties(entity, MemberMetricsGetResponse.class);
    }

    @Override
    public void validateUploadPermission(Long accountInstanceId, int uploadCount) {
        MemberMetricsEntity entity = getNewestByAccountInstanceId(accountInstanceId);

        isExist(entity);

        if (entity.getSingleUploadCount() < uploadCount) {
            throw new MemberException("当前最多可上传 " + entity.getSingleUploadCount() + " 份简历");
        }
    }

    @Override
    public void validateMatchJdPermission(Long accountInstanceId, int matchNum) {
        MemberMetricsEntity entity = getNewestByAccountInstanceId(accountInstanceId);

        isExist(entity);

        validateMatch(entity, matchNum);
    }

    @Override
    public void validateCreationJdPermission(Long accountInstanceId) {
        MemberMetricsEntity entity = getNewestByAccountInstanceId(accountInstanceId);

        isExist(entity);

        validateCreateJd(entity);
    }

    @Override
    public void validateInterviewPermission(Long accountInstanceId) {
        MemberMetricsEntity entity = getNewestByAccountInstanceId(accountInstanceId);

        isExist(entity);

        validateInterview(entity);
    }

    @Override
    public void validatePushEmailPermission(Long accountInstanceId) {
        MemberMetricsEntity entity = getNewestByAccountInstanceId(accountInstanceId);

        isExist(entity);

        if (entity.getEmailPushEnabled() == 0) {
            throw new MemberException("邮件推送功能为 [具备该功能的会员等级，如白银及以上] 会员专属");
        }
    }

    private void validateMatch(MemberMetricsEntity entity, int matchNum){
        int usedNum = entity.getUsedMatchCount() + matchNum;
        int remain = entity.getDailyMatchCount() - entity.getUsedMatchCount();
        if (entity.getUsedMatchCount() >= entity.getDailyMatchCount()) {
            throw new MemberException("今日匹配次数已用完。会员每日可匹配 " + entity.getDailyMatchCount() + " 次");
        } else if (usedNum > entity.getDailyMatchCount()) {
            throw new MemberException("已使用匹配次数 " + entity.getUsedMatchCount() + " ，当前剩余可匹配 " + remain + " 次");
        }
    }

    private void validateCreateJd(MemberMetricsEntity entity){
        if (entity.getUsedCreationJdCount() >= entity.getCreationJdCount()) {
            throw new MemberException("当前会员最多可创建 " + entity.getCreationJdCount() + " 个职位描述");
        }
    }

    private void validateInterview(MemberMetricsEntity entity) {
        if (entity.getUsedInterviewCount() >= entity.getActiveInterviewCount()) {
            throw new MemberException("当前最多可同时进行 " + entity.getActiveInterviewCount() + " 个邀约");
        }
    }

    private MemberMetricsEntity getNewestByAccountInstanceId(Long accountInstanceId) {
        return memberMetricsService.getOne(QueryWrapper.create()
                .eq(MemberMetricsEntity::getAccountInstanceId, accountInstanceId)
                .orderBy(MemberMetricsEntity::getTs).desc());
    }

    private void isExist(MemberMetricsEntity entity) {
        if (Objects.isNull(entity)) {
            throw new MemberException(MemberExceptionStatusCode.INTERESTS_NOT_FOUND);
        }
    }
}