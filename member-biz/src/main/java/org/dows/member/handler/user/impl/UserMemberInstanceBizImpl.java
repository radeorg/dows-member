package org.dows.member.handler.user.impl;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.mybatisflex.core.query.QueryWrapper;
import com.mybatisflex.core.update.UpdateChain;
import lombok.RequiredArgsConstructor;
import org.dows.member.constant.MemberExceptionStatusCode;
import org.dows.member.entity.MemberChangeEntity;
import org.dows.member.entity.MemberInstanceEntity;
import org.dows.member.entity.MemberInterestsEntity;
import org.dows.member.entity.MemberMetricsEntity;
import org.dows.member.enums.MemberChangeTypeEnum;
import org.dows.member.enums.MemberTypeEnum;
import org.dows.member.exception.MemberException;
import org.dows.member.handler.user.UserMemberInstanceBiz;
import org.dows.member.handler.util.CommonUtils;
import org.dows.member.request.user.UserMemberInstanceRenewalRequest;
import org.dows.member.request.user.UserMemberInstanceSaveRequest;
import org.dows.member.request.user.UserMemberInstanceUpGradeRequest;
import org.dows.member.service.MemberChangeService;
import org.dows.member.service.MemberInstanceService;
import org.dows.member.service.MemberInterestsService;
import org.dows.member.service.MemberMetricsService;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.time.ZoneId;
import java.util.Date;
import java.util.Objects;

@Component
@RequiredArgsConstructor
public class UserMemberInstanceBizImpl implements UserMemberInstanceBiz {

    private final MemberInstanceService memberInstanceService;
    private final MemberMetricsService memberMetricsService;
    private final MemberChangeService memberChangeService;
    private final MemberInterestsService memberInterestsService;

    @Transactional
    @Override
    public Long save(UserMemberInstanceSaveRequest request) {
        // 会员实例是否存在
        MemberInstanceEntity instanceEntity = getMemberInterestsByAccountInstanceIdAndAppId(request.getAccountInstanceId(), request.getAppId());
        if (instanceEntity != null) {
            throw new MemberException(MemberExceptionStatusCode.MEMBER_INSTANCE_EXIST);
        }

        // 查询未禁用的免费会员权益
        MemberInterestsEntity interests = getMemberInterestsByMemberType(MemberTypeEnum.FREE.getCode());

        // 新增会员账号实例
        instanceEntity = saveMemberInstance(request, interests);

        // 新增会员变更记录表
        saveMemberChange(null, instanceEntity, interests, MemberChangeTypeEnum.REGISTER.getCode());

        // 新增会员度量表
        saveMemberMetrics(instanceEntity, interests);

        return instanceEntity.getMemberInstanceId();
    }

    @Transactional
    @Override
    public Boolean upgrade(UserMemberInstanceUpGradeRequest request) {
        // 查询会员实例
        MemberInstanceEntity oldInstance = getMemberInterestsByAccountInstanceIdAndAppId(
                request.getAccountInstanceId(),
                request.getAppId()
        );

        // 校验会员实例是否存在
        isMemberInstanceExist(oldInstance);

        // 校验升级的等级跟当前等级是否一致
        if (oldInstance.getMemberInterestsId().equals(request.getMemberInterestsId())) {
            throw new MemberException("当前已是" + MemberTypeEnum.getDescByCode(oldInstance.getMemberType()) + ",不可重复操作");
        }

        // 查询当前缴费的会员等级，并验证是否存在及状态是否正常
        MemberInterestsEntity interests = getMemberInterestsById(request.getMemberInterestsId());

        // 更新会员等级信息
        MemberInstanceEntity newInstance = updateMemberInstance(oldInstance, interests);

        // 新增会员变更记录表
        saveMemberChange(oldInstance, newInstance, interests, MemberChangeTypeEnum.UP_GRADE.getCode());

        // 更新或新增会员度量表
        updateMemberMetrics(oldInstance, interests);

        return true;
    }

    @Transactional
    @Override
    public Boolean renewal(UserMemberInstanceRenewalRequest request) {
        // 查询会员实例
        MemberInstanceEntity oldInstance = getMemberInterestsByAccountInstanceIdAndAppId(
                request.getAccountInstanceId(),
                request.getAppId()
        );

        // 校验会员实例是否存在
        isMemberInstanceExist(oldInstance);

        // 查询当前缴费的会员等级，并验证是否存在及状态是否正常
        MemberInterestsEntity interests = getMemberInterestsById(request.getMemberInterestsId());

        // 更新会员等级信息
        MemberInstanceEntity newInstance = updateMemberInstance(oldInstance, interests);

        // 新增会员变更记录表
        saveMemberChange(oldInstance, newInstance, interests, MemberChangeTypeEnum.RENEWAL.getCode());

        // 更新或新增会员度量表
        updateMemberMetrics(oldInstance, interests);

        return true;
    }

    @Transactional
    @Override
    public Boolean expiration(Long memberInstanceId) {
        // 查询会员实例
        MemberInstanceEntity oldInstance = getMemberInstanceById(memberInstanceId);

        // 校验会员实例是否存在
        isMemberInstanceExist(oldInstance);

        // 校验是否已到期
        if (oldInstance.getExpiryDate() == null || (new Date()).after(oldInstance.getExpiryDate())) {
            // 查询免费会员权益并校验是否存在及状态是否正常
            MemberInterestsEntity interests = getMemberInterestsByMemberType(MemberTypeEnum.FREE.getCode());

            // 到期后需降级到免费会员
            MemberInstanceEntity newInstance = updateMemberInstance(oldInstance, interests);

            // 新增会员变更记录表
            saveMemberChange(oldInstance, newInstance, interests, MemberChangeTypeEnum.EXPIRATION.getCode());

            // 更新或新增会员度量表
            updateMemberMetrics(oldInstance, interests);
        }

        return true;
    }

    private MemberInstanceEntity saveMemberInstance(UserMemberInstanceSaveRequest request, MemberInterestsEntity interests){
        MemberInstanceEntity instanceEntity = new MemberInstanceEntity();
        instanceEntity.setAccountInstanceId(request.getAccountInstanceId());
        instanceEntity.setAppId(request.getAppId());
        instanceEntity.setMemberInterestsId(interests.getMemberInterestsId());
        instanceEntity.setMemberType(interests.getMemberType());
        instanceEntity.setEffectiveDate(new Date());
        memberInstanceService.save(instanceEntity);

        return instanceEntity;
    }

    private MemberInstanceEntity updateMemberInstance(MemberInstanceEntity oldInstance, MemberInterestsEntity interests){
        MemberInstanceEntity newInstance = new MemberInstanceEntity();
        newInstance.setAppId(oldInstance.getAppId());
        newInstance.setAccountInstanceId(oldInstance.getAccountInstanceId());
        newInstance.setMemberInstanceId(oldInstance.getMemberInstanceId());
        newInstance.setMemberType(interests.getMemberType());
        newInstance.setMemberInterestsId(interests.getMemberInterestsId());
        newInstance.setEffectiveDate(new Date());
        newInstance.setExpiryDate(addDate(oldInstance.getExpiryDate(), interests.getExpiryDay()));
        
        // 显式更新 expiryDate 字段，即使为 null
        UpdateChain.of(MemberInstanceEntity.class)
                .eq(MemberInstanceEntity::getMemberInstanceId, oldInstance.getMemberInstanceId())
                .set(MemberInstanceEntity::getMemberType, oldInstance.getMemberType())
                .set(MemberInstanceEntity::getMemberInterestsId, oldInstance.getMemberInterestsId())
                .set(MemberInstanceEntity::getEffectiveDate, new Date())
                .set(MemberInstanceEntity::getExpiryDate, addDate(oldInstance.getExpiryDate(), interests.getExpiryDay()))
                .update();

        return newInstance;
    }

    private void saveMemberChange(MemberInstanceEntity oldInstance,
                                  MemberInstanceEntity newInstance,
                                  MemberInterestsEntity interests,
                                  String changeType){
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
    }

    private void saveMemberMetrics(MemberInstanceEntity instance, MemberInterestsEntity interests){
        MemberMetricsEntity entity = new MemberMetricsEntity();
        entity.setAppId(instance.getAppId());
        entity.setAccountInstanceId(instance.getAccountInstanceId());
        entity.setMemberInstanceId(instance.getMemberInstanceId());
        entity.setMemberInterestsId(instance.getMemberInterestsId());
        entity.setDailyMatchCount(interests.getDailyMatchCount());
        entity.setActiveInterviewCount(interests.getActiveInterviewCount());
        entity.setCreationJdCount(interests.getCreationJdCount());
        entity.setSingleUploadCount(interests.getSingleUploadCount());
        memberMetricsService.save(entity);
    }

    private void updateMemberMetrics(MemberInstanceEntity instance, MemberInterestsEntity interests){
        MemberMetricsEntity entity = memberMetricsService.getOne(QueryWrapper.create()
                .eq(MemberMetricsEntity::getAccountInstanceId, instance.getAccountInstanceId())
                .eq(MemberMetricsEntity::getAppId, instance.getAppId())
                .orderBy(MemberMetricsEntity::getTs).desc());
        if (entity != null && CommonUtils.isToday(entity.getTs())) {
            // 等级一致的不需要进行更新
            if (!entity.getMemberInterestsId().equals(interests.getMemberInterestsId())) {
                entity.setActiveInterviewCount(interests.getActiveInterviewCount());
                entity.setDailyMatchCount(interests.getDailyMatchCount());
                entity.setSingleUploadCount(interests.getSingleUploadCount());
                entity.setCreationJdCount(interests.getCreationJdCount());
                memberMetricsService.updateById(entity);
            }
        } else {
            // 为空或当天还没生成新的会员度量数据时进行新增
            saveMemberMetrics(instance, interests);
        }
    }

    private MemberInterestsEntity getMemberInterestsById(Long memberInterestsId) {
        MemberInterestsEntity interests = memberInterestsService.getById(memberInterestsId);

        isMemberInterestsExist(interests);

        checkMemberInterestsState(interests);

        return interests;
    }

    private MemberInterestsEntity getMemberInterestsByMemberType(String memberType) {
        MemberInterestsEntity interests = memberInterestsService.getOne(QueryWrapper.create()
                .eq(MemberInterestsEntity::getMemberType, memberType));

        isMemberInterestsExist(interests);

        checkMemberInterestsState(interests);

        return interests;
    }

    private MemberInstanceEntity getMemberInterestsByAccountInstanceIdAndAppId(Long accountInstanceId, String appId) {
        return memberInstanceService.getOne(QueryWrapper.create()
                .eq(MemberInstanceEntity::getAccountInstanceId, accountInstanceId)
                .eq(MemberInstanceEntity::getAppId, appId));
    }

    private MemberInstanceEntity getMemberInstanceById(Long memberInstanceId) {
        return memberInstanceService.getById(memberInstanceId);
    }

    private void isMemberInstanceExist(MemberInstanceEntity entity) {
        if (Objects.isNull(entity)) {
            throw new MemberException(MemberExceptionStatusCode.MEMBER_INSTANCE_NOT_FOUND);
        }
    }

    protected void isMemberInterestsExist(MemberInterestsEntity entity) {
        if (Objects.isNull(entity)) {
            throw new MemberException(MemberExceptionStatusCode.INTERESTS_NOT_FOUND);
        }
    }

    private void checkMemberInterestsState(MemberInterestsEntity interests){
        if (interests.getState() == 1) {
            throw new MemberException(MemberExceptionStatusCode.HAS_DISABLED);
        }
    }

    private Date addDate(Date currentDate, Integer addDays){
        if (addDays == null) {
            return null;
        }
        currentDate = currentDate == null ? new Date() : currentDate;
        return Date.from(
                currentDate.toInstant()
                        .atZone(ZoneId.systemDefault())
                        .plusDays(addDays)
                        .toInstant()
        );
    }
}