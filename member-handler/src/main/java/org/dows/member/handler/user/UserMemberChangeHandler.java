package org.dows.member.handler.user;

import org.dows.member.entity.MemberInstanceEntity;
import org.dows.member.entity.MemberInterestsEntity;

public interface UserMemberChangeHandler {

    Long save(MemberInstanceEntity oldInstance,
              MemberInstanceEntity newInstance,
              MemberInterestsEntity interests,
              String changeType);
}