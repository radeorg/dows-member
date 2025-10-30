package org.dows.member.biz.user.impl;

import com.alibaba.fastjson2.JSON;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.dows.member.entity.MemberChangeEntity;
import org.dows.member.entity.MemberInstanceEntity;
import org.dows.member.entity.MemberInterestsEntity;
import org.dows.member.enums.MemberChangeTypeEnum;
import org.dows.member.biz.user.UserMemberChangeHandler;
import org.dows.member.service.MemberChangeService;
import org.springframework.stereotype.Component;

@Slf4j
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

        String interestsInfo;
        try {
            interestsInfo = JSON.toJSONString(interests);
        } catch (Exception e) {
            interestsInfo = "{}";
            log.error("对象转为JSON失败：" + e.getMessage());
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