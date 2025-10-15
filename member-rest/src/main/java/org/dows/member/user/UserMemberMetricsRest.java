package org.dows.member.user;

import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.dows.member.api.user.UserMemberMetricsApi;
import org.dows.member.exception.MemberException;
import org.dows.member.handler.user.UserMemberMetricsHandler;
import org.dows.member.response.MemberMetricsGetResponse;
import org.dows.rade.aac.AacContext;
import org.dows.rade.aac.AacUser;
import org.dows.rade.context.AppContext;
import org.springframework.web.bind.annotation.RestController;

import java.util.Objects;

@RestController
@Tag(name = "权益查询", description = "user/权益查询")
@RequiredArgsConstructor
public class UserMemberMetricsRest implements UserMemberMetricsApi {

    private final UserMemberMetricsHandler userMemberMetricsHandler;
    private final AacContext aacContext;

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
    public MemberMetricsGetResponse getNewest() {
        return userMemberMetricsHandler.getNewest(getAccountId(), AppContext.getAppId());
    }

    private Long getAccountId() {
        AacUser aacUser = aacContext.getAacUser();
        if (Objects.isNull(aacUser) || Objects.isNull(aacUser.getAccountId())) {
            throw new MemberException("登录账号不存在");
        }
        return aacUser.getAccountId();
    }
}