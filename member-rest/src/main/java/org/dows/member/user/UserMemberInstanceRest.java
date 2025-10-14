package org.dows.member.user;

import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.dows.member.api.user.UserMemberInstanceApi;
import org.dows.member.handler.user.UserMemberInstanceBiz;
import org.dows.member.handler.user.UserMemberInstanceHandler;
import org.dows.member.request.user.UserMemberInstanceSaveRequest;
import org.dows.member.request.user.UserMemberInstanceRenewalRequest;
import org.dows.member.request.user.UserMemberInstanceUpGradeRequest;
import org.dows.member.response.MemberInstanceGetResponse;
import org.dows.rade.context.AppContext;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Tag(name = "会员实例", description = "user/会员实例")
@RequiredArgsConstructor
public class UserMemberInstanceRest implements UserMemberInstanceApi {

    private final UserMemberInstanceHandler userMemberInstanceHandler;
    private final UserMemberInstanceBiz userMemberInstanceBiz;

    @Override
    public Long save(UserMemberInstanceSaveRequest request) {
        return userMemberInstanceBiz.save(request);
    }

    @Override
    public Boolean upgrade(UserMemberInstanceUpGradeRequest request) {
        request.setAppId(AppContext.getAppId());
        return userMemberInstanceBiz.upgrade(request);
    }

    @Override
    public Boolean renewal(UserMemberInstanceRenewalRequest request) {
        request.setAppId(AppContext.getAppId());
        return userMemberInstanceBiz.renewal(request);
    }

    @Override
    public Boolean expiration(Long memberInstanceId) {
        return userMemberInstanceBiz.expiration(memberInstanceId);
    }

    @Override
    public MemberInstanceGetResponse getById(Long memberInstanceId) {
        return userMemberInstanceHandler.getByMemberInstanceIdAndAppId(memberInstanceId, AppContext.getAppId());
    }

    @Override
    public MemberInstanceGetResponse getByAccountInstanceId(Long accountInstanceId) {
        return userMemberInstanceHandler.getByAccountInstanceIdAndAppId(accountInstanceId, AppContext.getAppId());
    }
}