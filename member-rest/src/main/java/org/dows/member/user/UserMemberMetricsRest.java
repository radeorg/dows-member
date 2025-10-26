package org.dows.member.user;

import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.dows.member.api.user.UserMemberMetricsApi;
import org.dows.member.exception.MemberException;
import org.dows.member.biz.user.UserMemberMetricsHandler;
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
    public void addUsedDailyMatchCount(int matchNum) {
        userMemberMetricsHandler.addUsedDailyMatchCount(getAccountId(), matchNum);
    }

    @Override
    public void addUsedActiveInviteCount() {
        userMemberMetricsHandler.addUsedActiveInviteCount(getAccountId());
    }

    @Override
    public void subUsedActiveInviteCountByGiveUp() {
        userMemberMetricsHandler.subUsedActiveInviteCount(getAccountId());
    }

    @Override
    public void subUsedActiveInviteCount(Long accountInstanceId) {
        userMemberMetricsHandler.subUsedActiveInviteCount(getAccountId());
    }

    @Override
    public void addUsedCreationJdCount() {
        userMemberMetricsHandler.addUsedCreationJdCount(getAccountId());
    }

    @Override
    public MemberMetricsGetResponse getNewest() {
        return userMemberMetricsHandler.getNewest(getAccountId(), AppContext.getAppId());
    }

    @Override
    public void validateUploadPermission(int uploadNum) {
        userMemberMetricsHandler.validateUploadPermission(getAccountId(), uploadNum);
    }

    @Override
    public void validateMatchJdPermission(int matchNum) {
        userMemberMetricsHandler.validateMatchJdPermission(getAccountId(), matchNum);
    }

    @Override
    public void validateCreationJdPermission() {
        userMemberMetricsHandler.validateCreationJdPermission(getAccountId());
    }

    @Override
    public void validateInterviewPermission() {
        userMemberMetricsHandler.validateInterviewPermission(getAccountId());
    }

    @Override
    public void validatePushEmailPermission() {
        userMemberMetricsHandler.validatePushEmailPermission(getAccountId());
    }

    private Long getAccountId() {
        AacUser aacUser = aacContext.getAacUser();
        if (Objects.isNull(aacUser) || Objects.isNull(aacUser.getAccountId())) {
            throw new MemberException("登录账号不存在");
        }
        return aacUser.getAccountId();
    }
}