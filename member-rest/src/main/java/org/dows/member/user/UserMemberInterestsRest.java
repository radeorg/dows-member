package org.dows.member.user;

import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.dows.member.handler.user.UserMemberInterestsHandler;
import org.dows.member.api.user.UserMemberInterestsApi;
import org.dows.member.response.MemberInterestsGetResponse;
import org.dows.member.request.MemberInterestsListRequest;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@Tag(name = "权益查询", description = "user/权益查询")
@RequiredArgsConstructor
public class UserMemberInterestsRest implements UserMemberInterestsApi {

    private final UserMemberInterestsHandler userMemberInterestsHandler;

    @Override
    public MemberInterestsGetResponse getById(Long memberInterestsId) {
        return userMemberInterestsHandler.getById(memberInterestsId);
    }

    @Override
    public List<MemberInterestsGetResponse> list(MemberInterestsListRequest request) {
        return userMemberInterestsHandler.list(request);
    }
}