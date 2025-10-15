package org.dows.member.user;

import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.dows.member.api.user.UserMemberInstanceApi;
import org.dows.member.exception.MemberException;
import org.dows.member.handler.user.UserMemberInstanceBiz;
import org.dows.member.handler.user.UserMemberInstanceHandler;
import org.dows.member.request.user.UserMemberInstanceSaveRequest;
import org.dows.member.request.user.UserMemberInstanceRenewalRequest;
import org.dows.member.request.user.UserMemberInstanceUpGradeRequest;
import org.dows.member.response.MemberInstanceGetResponse;
import org.dows.rade.aac.AacContext;
import org.dows.rade.aac.AacUser;
import org.dows.rade.context.AppContext;
import org.springframework.web.bind.annotation.RestController;

import java.util.Objects;

@RestController
@Tag(name = "会员实例", description = "user/会员实例")
@RequiredArgsConstructor
public class UserMemberInstanceRest implements UserMemberInstanceApi {

    private final UserMemberInstanceHandler userMemberInstanceHandler;
    private final UserMemberInstanceBiz userMemberInstanceBiz;
    private final AacContext aacContext;

    @Override
    public Long save(UserMemberInstanceSaveRequest request) {
        return userMemberInstanceBiz.save(request);
    }

    @Override
    public Boolean upgrade(Long memberInterestsId) {
        UserMemberInstanceUpGradeRequest request = new UserMemberInstanceUpGradeRequest();
        request.setAppId(AppContext.getAppId());
        request.setAccountInstanceId(getAccountId());
        request.setMemberInterestsId(memberInterestsId);

        return userMemberInstanceBiz.upgrade(request);
    }

    @Override
    public Boolean renewal(Long memberInterestsId) {
        UserMemberInstanceRenewalRequest request = new UserMemberInstanceRenewalRequest();
        request.setAppId(AppContext.getAppId());
        request.setAccountInstanceId(getAccountId());
        request.setMemberInterestsId(memberInterestsId);

        return userMemberInstanceBiz.renewal(request);
    }

    @Override
    public Boolean due(Long memberInstanceId) {
        return userMemberInstanceBiz.due(memberInstanceId);
    }

    @Override
    public MemberInstanceGetResponse get() {
        return userMemberInstanceHandler.getByAccountInstanceIdAndAppId(getAccountId(), AppContext.getAppId());
    }

    private Long getAccountId() {
        AacUser aacUser = aacContext.getAacUser();
        if (Objects.isNull(aacUser) || Objects.isNull(aacUser.getAccountId())) {
            throw new MemberException("登录账号不存在");
        }
        return aacUser.getAccountId();
    }
}