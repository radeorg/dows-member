package org.dows.member.handler.scheduler;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.dows.member.entity.MemberChargeEntity;
import org.dows.member.enums.MemberChargeStateEnum;
import org.dows.member.handler.pay.AliPayBiz;
import org.dows.member.handler.pay.WechatPayBiz;
import org.dows.member.handler.user.UserMemberChargeHandler;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;

import java.util.List;

/**
 * 支付逾期
 */
@RequiredArgsConstructor
@Configuration
@EnableScheduling
@Slf4j
public class PayOverdueScheduler {

    private final UserMemberChargeHandler userMemberChargeHandler;
    private final AliPayBiz aliPayBiz;
   // private final WechatPayBiz wechatPayBiz;

    // 每天01:00:00执行（CRON表达式）
    @Scheduled(cron = "0 */5 * * * ?")
    public void PayOverdueTask() {
        log.info("PayOverdueScheduler: {}", System.currentTimeMillis());
        //查询所有待支付的订单
        List<MemberChargeEntity> list = userMemberChargeHandler.listNotPayMemberCharge();
        for (MemberChargeEntity memberChargeEntity : list) {
//            if ("Wechat".equals(memberChargeEntity.getChannel())){
//               String status=  wechatPayBiz.wechatPayStatus(memberChargeEntity.getPayNo()).getTradeState();
//                if ("PAY_FAIL".equals(status)||"FAIL".equals(status)){
//                    //修改订单状态
//                    userMemberChargeHandler.updateNotPayMemberCharge(memberChargeEntity.getMemberInterestsId(),MemberChargeStateEnum.FAILED.getCode());
//                }
//            }
            if ("ALI".equals(memberChargeEntity.getChannel())){
                String aliPayStatus= aliPayBiz.aliPayStatus(memberChargeEntity.getPayNo()).getTradeState();
                //TRADE_CLOSED 过期  FAIL失败
                if (MemberChargeStateEnum.CLOSED.getCode().equals(aliPayStatus)){
                    //修改订单状态
                    userMemberChargeHandler.updateNotPayMemberCharge(memberChargeEntity.getMemberChargeId(),MemberChargeStateEnum.CLOSED.getCode());
                }
            }
        }
        log.info("PayOverdueScheduler定时任务执行结束: {}", System.currentTimeMillis());
    }
}
