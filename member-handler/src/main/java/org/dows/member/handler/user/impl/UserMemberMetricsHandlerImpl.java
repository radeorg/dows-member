package org.dows.member.handler.user.impl;

import cn.hutool.core.bean.BeanUtil;
import com.mybatisflex.core.query.QueryWrapper;
import lombok.RequiredArgsConstructor;
import org.dows.member.constant.MemberExceptionStatusCode;
import org.dows.member.entity.MemberInstanceEntity;
import org.dows.member.entity.MemberInterestsEntity;
import org.dows.member.entity.MemberMetricsEntity;
import org.dows.member.exception.MemberException;
import org.dows.member.handler.user.UserMemberMetricsHandler;
import org.dows.member.handler.util.CommonUtils;
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

                memberMetricsService.updateById(entity);
            }
        }
    }

    @Override
    public Boolean addUsedDailyMatchCount(Long accountInstanceId) {
        MemberMetricsEntity entity = getNewestByAccountInstanceId(accountInstanceId);

        isExist(entity);

        if (Objects.equals(entity.getDailyMatchCount(), entity.getUsedMatchCount())) {
            throw new MemberException(MemberExceptionStatusCode.METRICS_MATCH_OVER_LIMIT);
        }
        entity.setUsedMatchCount(entity.getUsedMatchCount() + 1);

        return memberMetricsService.updateById(entity);
    }

    @Override
    public Boolean addUsedActiveInviteCount(Long accountInstanceId) {
        MemberMetricsEntity entity = getNewestByAccountInstanceId(accountInstanceId);

        isExist(entity);

        if (Objects.equals(entity.getActiveInterviewCount(), entity.getUsedInterviewCount())) {
            throw new MemberException(MemberExceptionStatusCode.METRICS_ACTIVE_INVITE_OVER_LIMIT);
        }
        entity.setUsedInterviewCount(entity.getUsedInterviewCount() + 1);

        return memberMetricsService.updateById(entity);
    }

    @Override
    public Boolean subUsedActiveInviteCount(Long accountInstanceId) {
        MemberMetricsEntity entity = getNewestByAccountInstanceId(accountInstanceId);

        isExist(entity);

        if (entity.getUsedInterviewCount() == 0) {
            throw new MemberException(MemberExceptionStatusCode.METRICS_ACTIVE_INVITE_LOWER_LIMIT);
        }
        entity.setUsedInterviewCount(entity.getUsedInterviewCount() - 1);

        return memberMetricsService.updateById(entity);
    }

    @Override
    public Boolean addUsedCreationJdCount(Long accountInstanceId) {
        MemberMetricsEntity entity = getNewestByAccountInstanceId(accountInstanceId);

        isExist(entity);

        if (Objects.equals(entity.getCreationJdCount(), entity.getUsedCreationJdCount())) {
            throw new MemberException(MemberExceptionStatusCode.METRICS_CREATION_JD_OVER_LIMIT);
        }
        entity.setUsedCreationJdCount(entity.getUsedCreationJdCount() + 1);

        return memberMetricsService.updateById(entity);
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