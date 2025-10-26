package org.dows.member.biz.user.impl;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.dows.member.entity.MemberChangeEntity;
import org.dows.member.entity.MemberInstanceEntity;
import org.dows.member.entity.MemberInterestsEntity;
import org.dows.member.enums.MemberChangeTypeEnum;
import org.dows.member.biz.user.UserMemberChangeHandler;
import org.dows.member.service.MemberChangeService;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class UserMemberChangeHandlerImpl implements UserMemberChangeHandler {

    private final MemberChangeService memberChangeService;

    @Override
    public Long save(MemberInstanceEntity oldInstance,
                     MemberInstanceEntity newInstance,
                     MemberInterestsEntity interests,
                     String changeType) {
        MemberChangeEntity entity = new MemberChangeEntity();
        entity.setAppId(newInstance.getAppId());
        entity.setAccountInstanceId(newInstance.getAccountInstanceId());
        entity.setMemberInstanceId(newInstance.getMemberInstanceId());
        entity.setNewMemberInterestsId(newInstance.getMemberInterestsId());
        entity.setNewMemberType(newInstance.getMemberType());
        entity.setEffectiveDate(newInstance.getEffectiveDate());
        entity.setExpiryDate(newInstance.getExpiryDate());
        entity.setNote(MemberChangeTypeEnum.getDescByCode(changeType));
        entity.setChangeType(changeType);

        // 使用 ObjectMapper 进行 JSON 转换
        ObjectMapper objectMapper = new ObjectMapper();
        String interestsInfo;
        try {
            interestsInfo = objectMapper.writeValueAsString(interests);
        } catch (JsonProcessingException e) {
            interestsInfo = "{}";
        }
        entity.setNewInterestsInfo(interestsInfo);

        if (oldInstance != null) {
            entity.setPreviousMemberInterestsId(oldInstance.getMemberInterestsId());
            entity.setPreviousMemberType(oldInstance.getMemberType());
        }

        memberChangeService.save(entity);

        return entity.getMemberChangeId();
    }
}