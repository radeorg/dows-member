package org.dows.member.handler.admin.impl;

import cn.hutool.core.bean.BeanUtil;
import com.mybatisflex.core.query.QueryWrapper;
import lombok.RequiredArgsConstructor;
import org.dows.member.handler.admin.AdminMemberInterestsHandler;
import org.dows.member.constant.MemberExceptionStatusCode;
import org.dows.member.entity.MemberInterestsEntity;
import org.dows.member.exception.MemberException;
import org.dows.member.request.MemberInterestsListRequest;
import org.dows.member.response.MemberInterestsGetResponse;
import org.dows.member.service.MemberInterestsService;
import org.dows.member.request.admin.AdminMemberInterestsSaveRequest;
import org.dows.member.request.admin.AdminMemberInterestsUpdateRequest;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Objects;

@Component
@RequiredArgsConstructor
public class AdminMemberInterestsHandlerImpl implements AdminMemberInterestsHandler {

    private final MemberInterestsService memberInterestsService;

    public Long save(AdminMemberInterestsSaveRequest req) {
        // 检查是否重复
        isDuplicate(req.getMemberType());

        MemberInterestsEntity e = new MemberInterestsEntity();
        e.setMemberType(req.getMemberType());
        e.setAmount(req.getAmount());
        e.setExpiryDay(req.getExpiryDay());
        e.setSingleUploadCount(req.getSingleUploadCount());
        e.setDailyMatchCount(req.getDailyMatchCount());
        e.setCreationJdCount(req.getCreationJdCount());
        e.setActiveInterviewCount(req.getActiveInterviewCount());
        e.setEmailPushEnabled(req.getEmailPushEnabled());
        e.setUseDate(req.getUseDate());
        e.setHolidayExclude(req.getHolidayExclude());
        e.setMatchingPriority(req.getMatchingPriority());
        e.setState(0);
        memberInterestsService.save(e);
        return e.getMemberInterestsId();
    }

    public Boolean update(AdminMemberInterestsUpdateRequest req) {
        MemberInterestsEntity e = getByMemberInterestsId(req.getMemberInterestsId());

        // 检查是否存在
        isExist(e);

        // 检查是否重复
        isDuplicate(e.getMemberInterestsId(), req.getMemberType());

        e.setMemberType(req.getMemberType());
        e.setAmount(req.getAmount());
        e.setExpiryDay(req.getExpiryDay());
        e.setSingleUploadCount(req.getSingleUploadCount());
        e.setDailyMatchCount(req.getDailyMatchCount());
        e.setCreationJdCount(req.getCreationJdCount());
        e.setActiveInterviewCount(req.getActiveInterviewCount());
        e.setEmailPushEnabled(req.getEmailPushEnabled());
        e.setUseDate(req.getUseDate());
        e.setHolidayExclude(req.getHolidayExclude());
        e.setMatchingPriority(req.getMatchingPriority());
        return memberInterestsService.updateById(e);
    }

    public Boolean enable(Long memberInterestsId) {
        MemberInterestsEntity e = getByMemberInterestsId(memberInterestsId);

        // 检查是否存在
        isExist(e);

        if (e.getState() == 0) {
            throw new MemberException(MemberExceptionStatusCode.REPEAT_ENABLED);
        }
        e.setState(0);

        return memberInterestsService.updateById(e);
    }

    public Boolean disable(Long memberInterestsId) {
        MemberInterestsEntity e = getByMemberInterestsId(memberInterestsId);

        // 检查是否存在
        isExist(e);

        if (e.getState() == 1) {
            throw new MemberException(MemberExceptionStatusCode.REPEAT_DISABLED);
        }
        e.setState(1);

        return memberInterestsService.updateById(e);
    }

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

    private void isDuplicate(String memberType) {
        MemberInterestsEntity entity = getByMemberType(memberType);
        if (entity != null) {
            throw new MemberException(MemberExceptionStatusCode.INTERESTS_TYPE_HAS_EXIST);
        }
    }

    private void isDuplicate(Long memberInterestsId, String memberType) {
        MemberInterestsEntity entity = getByMemberType(memberType);
        if (Objects.nonNull(entity) && !entity.getMemberInterestsId().equals(memberInterestsId)) {
            throw new MemberException(MemberExceptionStatusCode.INTERESTS_TYPE_HAS_EXIST);
        }
    }

    private MemberInterestsEntity getByMemberInterestsId(Long memberInterestsId) {
        return memberInterestsService.getById(memberInterestsId);
    }

    private MemberInterestsEntity getByMemberType(String memberType) {
        return memberInterestsService.getOne(QueryWrapper.create()
                .eq(MemberInterestsEntity::getMemberType, memberType));
    }
}