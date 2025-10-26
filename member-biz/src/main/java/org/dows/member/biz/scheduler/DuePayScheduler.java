package org.dows.member.biz.scheduler;

import com.alipay.api.AlipayApiException;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.dows.member.entity.MemberChargeEntity;
import org.dows.member.enums.PayChannelEnum;
import org.dows.member.biz.pay.PaymentBiz;
import org.dows.member.biz.user.UserMemberChargeHandler;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler;
import org.springframework.scheduling.support.CronTrigger;

import java.util.List;

/**
 * 支付逾期
 */
@RequiredArgsConstructor
@Configuration
@Slf4j
public class DuePayScheduler {

    private final UserMemberChargeHandler userMemberChargeHandler;
    private final ThreadPoolTaskScheduler duePayTaskScheduler;
    private final PaymentBiz paymentBiz;
   // private final WechatPayBiz wechatPayBiz;

    /**
     * 初始化时注册定时任务
     */
    @PostConstruct
    public void init() {
        duePayTaskScheduler.schedule(this::processDuePay, new CronTrigger("0 */1 * * * ?"));
        log.info("DuePayScheduler，执行频率：每隔5分钟");
    }

    private void processDuePay() {
        try {
            // 两分钟内未支付的订单进行撤销支付
            List<MemberChargeEntity> list = userMemberChargeHandler.listDuePayMemberCharge();
            if (list.isEmpty()) return;

            list.forEach(m -> {
                if (m.getChannel().equals(PayChannelEnum.ALI.getCode())) {
                    try {
                        paymentBiz.cancelAliPay(m.getPayNo());
                    } catch (AlipayApiException e) {
                        log.error("支付宝订单关闭失败：" + e.getErrMsg());
                    }
                } else if (m.getChannel().equals(PayChannelEnum.WECHAT.getCode())) {

                }
            });
            log.info("处理完成过期未支付充值记录：{}条记录", list.size());
        } catch (Exception e) {
            log.error("处理异常", e);
        }
    }
}
