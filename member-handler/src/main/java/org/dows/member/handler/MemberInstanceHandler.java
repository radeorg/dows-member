package org.dows.member.handler;

import lombok.RequiredArgsConstructor;
import org.dows.member.entity.MemberChangeEntity;
import org.dows.member.entity.MemberInstanceEntity;
import org.dows.member.entity.MemberMetricsEntity;
import org.dows.member.service.MemberChangeService;
import org.dows.member.service.MemberInstanceService;
import org.dows.member.service.MemberMetricsService;
import org.dows.member.member.user.dto.*;
import org.springframework.stereotype.Component;

import java.util.Date;

@Component
@RequiredArgsConstructor
public class MemberInstanceHandler {
    private final MemberInstanceService instanceService;
    private final MemberChangeService changeService;
    private final MemberMetricsService metricsService;

    public Long save(MemberInstanceSaveRequest req) {
        MemberInstanceEntity e = new MemberInstanceEntity();
        e.setAccountInstanceId(req.getAccountInstanceId());
        e.setMemberInterestsId(req.getMemberInterestsId());
        e.setMembershipType(req.getMembershipType());
        e.setMembershipEffectiveDate(req.getMembershipEffectiveDate());
        e.setMembershipExpiryDate(req.getMembershipExpiryDate());
        instanceService.save(e);
        return e.getMemberInstanceId();
    }

    public Boolean upgrade(MemberInstancerUpGradeRequest req) {
        // 简化：记录一条变更
        MemberChangeEntity change = new MemberChangeEntity();
        change.setMemberInstanceId(req.getMemberInstanceId());
        change.setNewMemberInterestsId(req.getMemberInterestsId());
        change.setChangeType("upgrade");
        change.setEffectiveDate(new Date());
        changeService.save(change);
        return true;
    }

    public Boolean renewal(MemberInstancerRenewalRequest req) {
        MemberChangeEntity change = new MemberChangeEntity();
        change.setMemberInstanceId(req.getMemberInstanceId());
        change.setNewMemberInterestsId(req.getMemberInterestsId());
        change.setChangeType("renewal");
        change.setEffectiveDate(new Date());
        changeService.save(change);
        return true;
    }

    public MemberInstanceIdGetResponse get(Long id) {
        MemberInstanceEntity e = instanceService.getById(id);
        if (e == null) return null;
        MemberInstanceIdGetResponse r = new MemberInstanceIdGetResponse();
        r.setMemberInstanceId(e.getMemberInstanceId());
        r.setAccountInstanceId(e.getAccountInstanceId());
        r.setMemberInterestsId(e.getMemberInterestsId());
        r.setMembershipType(e.getMembershipType());
        r.setMembershipEffectiveDate(e.getMembershipEffectiveDate());
        r.setMembershipExpiryDate(e.getMembershipExpiryDate());
        return r;
    }

    public MemberMetricsGetResponse metrics(Long memberInstanceId) {
        // 简化：取最新一条（此处可按时间倒序查询）
        MemberMetricsEntity m = metricsService.list().stream()
                .filter(x -> memberInstanceId.equals(x.getMemberInstanceId()))
                .reduce((first, second) -> second).orElse(null);
        if (m == null) return null;
        MemberMetricsGetResponse r = new MemberMetricsGetResponse();
        r.setMemberMetricsId(m.getMemberMetricsId());
        r.setMemberInstanceId(m.getMemberInstanceId());
        r.setAccountInstanceId(m.getAccountInstanceId());
        r.setMemberInterestsId(m.getMemberInterestsId());
        r.setMatchCount(m.getMatchCount());
        r.setUsedMatchCount(m.getUsedMatchCount());
        r.setInviteCount(m.getInviteCount());
        r.setUsedInviteCount(m.getUsedInviteCount());
        r.setCreationJdCount(m.getCreationJdCount());
        r.setUsedCreationJdCount(m.getUsedCreationJdCount());
        r.setSingleUploadCount(m.getSingleUploadCount());
        return r;
    }
}