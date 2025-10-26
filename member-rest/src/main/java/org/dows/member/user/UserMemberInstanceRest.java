package org.dows.member.user;

import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.dows.member.api.user.UserMemberInstanceApi;
import org.dows.member.exception.MemberException;
import org.dows.member.biz.user.UserMemberInstanceBiz;
import org.dows.member.biz.user.UserMemberInstanceHandler;
import org.dows.member.request.user.UserMemberInstanceSaveRequest;
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

//    @Override
//    public Boolean due(Long memberInstanceId) {
//        userMemberInstanceBiz.due(memberInstanceId);
//        return true;
//    }

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