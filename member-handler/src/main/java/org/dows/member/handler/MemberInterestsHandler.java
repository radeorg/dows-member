package org.dows.member.handler;

import lombok.RequiredArgsConstructor;
import org.dows.member.entity.MemberInterestsEntity;
import org.dows.member.service.MemberInterestsService;
import org.dows.member.interests.admin.dto.AdminMemberInterestsSaveRequest;
import org.dows.member.interests.admin.dto.AdminMemberInterestsUpdateRequest;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class MemberInterestsHandler {
    private final MemberInterestsService service;

    public Long save(AdminMemberInterestsSaveRequest req) {
        MemberInterestsEntity e = new MemberInterestsEntity();
        e.setMemberType(req.getMemberType());
        e.setAmount(req.getAmount());
        e.setExpiryDay(req.getExpiryDay());
        e.setSingleUploadCount(req.getSingleUploadCount());
        e.setDailyMatchCount(req.getDailyMatchCount());
        e.setCreationJdCount(req.getCreationJdCount());
        e.setActiveInterviewCount(req.getActiveInterviewCount());
        e.setEmailPushEnabled(Boolean.TRUE.equals(req.getEmailPushEnabled()) ? 1 : 0);
        e.setUseDate(req.getUseDate());
        e.setHolidayExclude(Boolean.TRUE.equals(req.getHolidayExclude()) ? 1 : 0);
        e.setMatchingPriority(req.getMatchingPriority());
        e.setDisabled(0);
        service.save(e);
        return e.getMemberInterestsId();
    }

    public Boolean update(AdminMemberInterestsUpdateRequest req) {
        MemberInterestsEntity e = new MemberInterestsEntity();
        e.setMemberInterestsId(req.getMemberInterestsId());
        e.setMemberType(req.getMemberType());
        e.setAmount(req.getAmount());
        e.setExpiryDay(req.getExpiryDay());
        e.setSingleUploadCount(req.getSingleUploadCount());
        e.setDailyMatchCount(req.getDailyMatchCount());
        e.setCreationJdCount(req.getCreationJdCount());
        e.setActiveInterviewCount(req.getActiveInterviewCount());
        e.setEmailPushEnabled(Boolean.TRUE.equals(req.getEmailPushEnabled()) ? 1 : 0);
        e.setUseDate(req.getUseDate());
        e.setHolidayExclude(Boolean.TRUE.equals(req.getHolidayExclude()) ? 1 : 0);
        e.setMatchingPriority(req.getMatchingPriority());
        return service.updateById(e);
    }

    public Boolean setDisabled(Long id, boolean disabled) {
        MemberInterestsEntity e = new MemberInterestsEntity();
        e.setMemberInterestsId(id);
        e.setDisabled(disabled ? 1 : 0);
        return service.updateById(e);
    }
}