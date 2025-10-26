package org.dows.member.biz.admin.impl;

import com.mybatisflex.core.paginate.Page;
import com.mybatisflex.core.query.QueryWrapper;
import lombok.RequiredArgsConstructor;
import org.dows.member.entity.MemberInstanceEntity;
import org.dows.member.form.PageQuery;
import org.dows.member.biz.admin.AdminMemberInstanceHandler;
import org.dows.member.request.admin.AdminMemberInstanceQueryRequest;
import org.dows.member.response.MemberInstanceGetResponse;
import org.dows.member.service.MemberInstanceService;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class AdminMemberInstanceHandlerImpl implements AdminMemberInstanceHandler {

    private final MemberInstanceService memberInstanceService;

    @Override
    public Page<MemberInstanceGetResponse> query(PageQuery pageQuery, AdminMemberInstanceQueryRequest request) {
        pageQuery.build();
        Page<MemberInstanceEntity> page = memberInstanceService.page(
                Page.of(pageQuery.getPageNum(), pageQuery.getPageSize()), QueryWrapper.create()
                        .eq(MemberInstanceEntity::getMemberInstanceId, request.getMemberInstanceId(), Objects.nonNull(request.getMemberInstanceId()))
                        .eq(MemberInstanceEntity::getAccountInstanceId, request.getAccountInstanceId(), Objects.nonNull(request.getAccountInstanceId()))
                        .orderBy(MemberInstanceEntity::getTs, true));
        if (page.getRecords().isEmpty()) {
            return Page.of(pageQuery.getPageNum(), pageQuery.getPageSize(), 0);
        }
        List<MemberInstanceGetResponse> voList = page.getRecords().stream()
                .map(entity -> {
                    MemberInstanceGetResponse vo = new MemberInstanceGetResponse();
                    BeanUtils.copyProperties(entity, vo);
                    return vo;
                })
                .collect(Collectors.toList());

        return new Page<>(voList,pageQuery.getPageNum(), pageQuery.getPageSize(), page.getTotalRow());
    }
}