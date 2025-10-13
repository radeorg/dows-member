package org.dows.member.member.user.impl;

import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.dows.member.handler.MemberInstanceHandler;
import org.dows.member.member.user.UserMemberInstanceApi;
import org.dows.member.member.user.dto.*;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Tag(name = "会员实例", description = "user/会员实例")
@RequiredArgsConstructor
public class UserMemberInstanceRest implements UserMemberInstanceApi {

    private final MemberInstanceHandler instanceHandler;

    @Override
    public Long save(MemberInstanceSaveRequest request) {
        return instanceHandler.save(request);
    }

    @Override
    public Boolean upgrade(MemberInstancerUpGradeRequest request) {
        return instanceHandler.upgrade(request);
    }

    @Override
    public Boolean renewal(MemberInstancerRenewalRequest request) {
        return instanceHandler.renewal(request);
    }

    @Override
    public MemberInstanceIdGetResponse get(Long memberInstanceId) {
        return instanceHandler.get(memberInstanceId);
    }

    @Override
    public MemberMetricsGetResponse metrics(Long memberInstanceId) {
        return instanceHandler.metrics(memberInstanceId);
    }
}