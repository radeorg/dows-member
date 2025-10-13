package org.dows.member.service.impl;

import org.dows.member.entity.MemberInstanceEntity;
import org.dows.member.mapper.MemberInstanceMapper;
import org.dows.member.service.MemberInstanceService;
import org.dows.rade.crud.BaseServiceImpl;
import org.springframework.stereotype.Service;

@Service
public class MemberInstanceServiceImpl extends BaseServiceImpl<MemberInstanceMapper, MemberInstanceEntity> implements MemberInstanceService {

}