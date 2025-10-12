package org.dows.member.handler;

import lombok.RequiredArgsConstructor;
import org.dows.member.entity.MemberInterestsEntity;
import org.dows.member.service.MemberInterestsService;
import org.dows.member.interests.admin.dto.AdminMemberInterestsGetResponse;
import org.dows.member.interests.admin.dto.AdminMemberInterestsListRequest;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class MemberInterestsQueryHandler {
    private final MemberInterestsService service;

    public AdminMemberInterestsGetResponse get(Long id) {
        MemberInterestsEntity e = service.getById(id);
        if (e == null) return null;
        return toResp(e);
    }

    public List<AdminMemberInterestsGetResponse> list(AdminMemberInterestsListRequest req) {
        // 简化：全量查询再过滤（生产建议使用 mybatis-flex 条件查询）
        return service.list().stream()
                .filter(e -> req.getMemberType() == null || req.getMemberType().equals(e.getMemberType()))
                .filter(e -> req.getDisabled() == null || (req.getDisabled() ? (byte)1 : (byte)0) == e.getDisabled())
                .map(this::toResp)
                .collect(Collectors.toList());
    }

    private AdminMemberInterestsGetResponse toResp(MemberInterestsEntity e) {
        AdminMemberInterestsGetResponse r = new AdminMemberInterestsGetResponse();
        r.setMemberInterestsId(e.getMemberInterestsId());
        r.setMemberType(e.getMemberType());
        r.setAmount(e.getAmount());
        r.setExpiryDay(e.getExpiryDay());
        r.setSingleUploadCount(e.getSingleUploadCount());
        r.setDailyMatchCount(e.getDailyMatchCount());
        r.setCreationJdCount(e.getCreationJdCount());
        r.setActiveInterviewCount(e.getActiveInterviewCount());
        r.setEmailPushEnabled(e.getEmailPushEnabled() != null && e.getEmailPushEnabled() == 1);
        r.setUseDate(e.getUseDate());
        r.setHolidayExclude(e.getHolidayExclude() != null && e.getHolidayExclude() == 1);
        r.setMatchingPriority(e.getMatchingPriority());
        r.setDisabled(e.getDisabled() != null && e.getDisabled() == 1);
        r.setOperatorId(e.getOperatorId());
        r.setTs(e.getTs());
        r.setUt(e.getUt());
        return r;
    }
}