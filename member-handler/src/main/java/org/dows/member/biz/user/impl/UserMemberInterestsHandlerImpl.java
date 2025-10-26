package org.dows.member.biz.user.impl;

import cn.hutool.core.bean.BeanUtil;
import com.mybatisflex.core.query.QueryWrapper;
import lombok.RequiredArgsConstructor;
import org.dows.member.constant.MemberExceptionStatusCode;
import org.dows.member.entity.MemberInterestsEntity;
import org.dows.member.exception.MemberException;
import org.dows.member.biz.user.UserMemberInterestsHandler;
import org.dows.member.request.MemberInterestsListRequest;
import org.dows.member.response.MemberInterestsGetResponse;
import org.dows.member.service.MemberInterestsService;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Objects;

@Component
@RequiredArgsConstructor
public class UserMemberInterestsHandlerImpl implements UserMemberInterestsHandler {

    private final MemberInterestsService memberInterestsService;

    @Override
    public MemberInterestsGetResponse getById(Long id) {
        MemberInterestsEntity entity = memberInterestsService.getById(id);

        return BeanUtil.copyProperties(entity, MemberInterestsGetResponse.class);
    }

    @Override
    public List<MemberInterestsGetResponse> list(MemberInterestsListRequest request) {
        List<MemberInterestsEntity> entities = memberInterestsService.list(QueryWrapper.create()
                .eq(MemberInterestsEntity::getState, request.getState())
                .eq(MemberInterestsEntity::getMemberType, request.getMemberType()));
        return BeanUtil.copyToList(entities, MemberInterestsGetResponse.class);
    }

    private void isExist(MemberInterestsEntity entity) {
        if (Objects.isNull(entity)) {
            throw new MemberException(MemberExceptionStatusCode.INTERESTS_NOT_FOUND);
        }
    }
}