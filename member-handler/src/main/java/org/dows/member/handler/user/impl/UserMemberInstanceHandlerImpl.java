package org.dows.member.handler.user.impl;

import cn.hutool.core.bean.BeanUtil;
import com.mybatisflex.core.query.QueryWrapper;
import lombok.RequiredArgsConstructor;
import org.dows.member.constant.MemberExceptionStatusCode;
import org.dows.member.entity.MemberInstanceEntity;
import org.dows.member.exception.MemberException;
import org.dows.member.handler.user.UserMemberInstanceHandler;
import org.dows.member.response.MemberInstanceGetResponse;
import org.dows.member.service.MemberInstanceService;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.List;
import java.util.Objects;

@Component
@RequiredArgsConstructor
public class UserMemberInstanceHandlerImpl implements UserMemberInstanceHandler {

    protected final MemberInstanceService memberInstanceService;

    @Override
    public MemberInstanceGetResponse getByAccountInstanceIdAndAppId(Long accountInstanceId, String appId) {
        MemberInstanceEntity entity = memberInstanceService.getOne(QueryWrapper.create()
                .eq(MemberInstanceEntity::getAccountInstanceId, accountInstanceId)
                .eq(MemberInstanceEntity::getAppId, appId));

        isExist(entity);

        return BeanUtil.copyProperties(entity, MemberInstanceGetResponse.class);
    }

    @Override
    public List<MemberInstanceGetResponse> listDueMemberInstance() {
        List<MemberInstanceEntity> entities = memberInstanceService.list(QueryWrapper.create()
                .le(MemberInstanceEntity::getExpiryDate, new Date()));

        return BeanUtil.copyToList(entities, MemberInstanceGetResponse.class);
    }

    private void isExist(MemberInstanceEntity entity) {
        if (Objects.isNull(entity)) {
            throw new MemberException(MemberExceptionStatusCode.MEMBER_INSTANCE_NOT_FOUND);
        }
    }
}