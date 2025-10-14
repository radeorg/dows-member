package org.dows.member.user;

import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.dows.member.api.user.UserMemberMetricsApi;
import org.dows.member.handler.user.UserMemberMetricsHandler;
import org.dows.member.response.MemberMetricsGetResponse;
import org.dows.rade.context.AppContext;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Tag(name = "权益查询", description = "user/权益查询")
@RequiredArgsConstructor
public class UserMemberMetricsRest implements UserMemberMetricsApi {

    private final UserMemberMetricsHandler userMemberMetricsHandler;

    @Override
    public Long dailySave(Long accountInstanceId) {
        return userMemberMetricsHandler.dailySave(accountInstanceId);
    }

    @Override
    public Boolean addUsedDailyMatchCount(Long accountInstanceId) {
        return userMemberMetricsHandler.addUsedDailyMatchCount(accountInstanceId);
    }

    @Override
    public Boolean addUsedActiveInviteCount(Long accountInstanceId) {
        return userMemberMetricsHandler.addUsedActiveInviteCount(accountInstanceId);
    }

    @Override
    public Boolean subUsedActiveInviteCount(Long accountInstanceId) {
        return userMemberMetricsHandler.subUsedActiveInviteCount(accountInstanceId);
    }

    @Override
    public Boolean addUsedCreationJdCount(Long accountInstanceId) {
        return userMemberMetricsHandler.addUsedCreationJdCount(accountInstanceId);
    }

    @Override
    public MemberMetricsGetResponse getNewestByAccountInstanceId(Long accountInstanceId) {
        return userMemberMetricsHandler.getNewest(accountInstanceId, AppContext.getAppId());
    }
}