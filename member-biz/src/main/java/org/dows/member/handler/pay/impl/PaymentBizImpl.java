package org.dows.member.handler.pay.impl;

import com.mybatisflex.core.query.QueryWrapper;
import lombok.RequiredArgsConstructor;
import org.dows.member.constant.MemberExceptionStatusCode;
import org.dows.member.entity.MemberChargeEntity;
import org.dows.member.entity.MemberInstanceEntity;
import org.dows.member.entity.MemberInterestsEntity;
import org.dows.member.enums.MemberChargeTypeEnum;
import org.dows.member.enums.MemberTypeEnum;
import org.dows.member.exception.MemberException;
import org.dows.member.handler.pay.PaymentBiz;
import org.dows.member.handler.pay.WechatPayBiz;
import org.dows.member.handler.user.UserMemberChargeHandler;
import org.dows.member.request.pay.CreateNativePayQrCodeRequest;
import org.dows.member.request.pay.WechatPayQrCodeRequest;
import org.dows.member.request.user.UserMemberChargeSaveRequest;
import org.dows.member.response.pay.PayQrCodeResponse;
import org.dows.member.service.MemberInstanceService;
import org.dows.member.service.MemberInterestsService;
import org.springframework.stereotype.Component;

import java.util.Objects;

@Component
@RequiredArgsConstructor
public class PaymentBizImpl implements PaymentBiz {

    private final MemberInstanceService memberInstanceService;
    private final MemberInterestsService memberInterestsService;
    private final UserMemberChargeHandler userMemberChargeHandler;
    private final WechatPayBiz wechatPayBiz;

    @Override
    public PayQrCodeResponse createNativePayment(CreateNativePayQrCodeRequest request) {
        // 查询会员实例
        MemberInstanceEntity oldInstance = getMemberInterestsByAccountInstanceIdAndAppId(
                request.getAccountInstanceId(),
                request.getAppId()
        );

        // 校验会员实例是否存在
        isMemberInstanceExist(oldInstance);

        // 查询当前缴费的会员等级，并验证是否存在及状态是否正常
        MemberInterestsEntity interests = getMemberInterestsById(request.getMemberInterestsId());

        // 校验是否可执行当前操作，并返回会员变更类型
        String chargeType = validateMemberInterest(oldInstance, interests, request);

        // 保存充值记录（充值订单）
        MemberChargeEntity chargeEntity = saveMemberCharge(oldInstance,
                interests,
                request.getPayChannel(),
                chargeType,
                MemberChargeTypeEnum.getDescByCode(chargeType));

        // 调用第三方支付
        if (request.getPayChannel().equals("Wechat")) {
            WechatPayQrCodeRequest payQrCodeRequest = new WechatPayQrCodeRequest();
            payQrCodeRequest.setOutTradeNo(chargeEntity.getPayNo());
            payQrCodeRequest.setTotalAmount(interests.getAmount());
            payQrCodeRequest.setDescription(chargeEntity.getNote());

            return wechatPayBiz.wechatPayQrCode(payQrCodeRequest);
        }
        return null;
    }

    private MemberChargeEntity saveMemberCharge(MemberInstanceEntity instance,
                                                MemberInterestsEntity interests,
                                                String channel,
                                                String chargeType,
                                                String note) {
        UserMemberChargeSaveRequest request = new UserMemberChargeSaveRequest();
        request.setAppId(instance.getAppId());
        request.setAccountInstanceId(instance.getAccountInstanceId());
        request.setMemberInstanceId(instance.getMemberInstanceId());
        request.setMemberInterestsId(interests.getMemberInterestsId());
        request.setAmount(interests.getAmount());
        request.setChannel(channel);
        request.setNote(note);
        request.setChargeType(chargeType);

        return userMemberChargeHandler.save(request);
    }

    private String validateMemberInterest(MemberInstanceEntity oldInstance,
                                          MemberInterestsEntity interests,
                                          CreateNativePayQrCodeRequest request){
        if (request.getChargeType().equals(MemberChargeTypeEnum.RENEWAL.getCode())) {
            // 校验续费的等级跟当前等级是否一致
            if (!oldInstance.getMemberInterestsId().equals(request.getMemberInterestsId())) {
                throw new MemberException("当前不是【" + MemberTypeEnum.getDescByCode(interests.getMemberType()) + "】，不能进行续费操作");
            }
            return MemberChargeTypeEnum.RENEWAL.getCode();
        } else {
            // 校验升级的等级跟当前等级是否一致
            if (oldInstance.getMemberInterestsId().equals(request.getMemberInterestsId())) {
                throw new MemberException("当前已是【" + MemberTypeEnum.getDescByCode(oldInstance.getMemberType()) + "】，不可重复操作");
            }
            return MemberChargeTypeEnum.UP_GRADE.getCode();
        }
    }

    private MemberInterestsEntity getMemberInterestsById(Long memberInterestsId) {
        MemberInterestsEntity interests = memberInterestsService.getById(memberInterestsId);

        isMemberInterestsExist(interests);

        checkMemberInterestsState(interests);

        return interests;
    }

    private MemberInstanceEntity getMemberInterestsByAccountInstanceIdAndAppId(Long accountInstanceId, String appId) {
        return memberInstanceService.getOne(QueryWrapper.create()
                .eq(MemberInstanceEntity::getAccountInstanceId, accountInstanceId)
                .eq(MemberInstanceEntity::getAppId, appId));
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
}
