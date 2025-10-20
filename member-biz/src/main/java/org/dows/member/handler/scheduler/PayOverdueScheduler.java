package org.dows.member.handler.scheduler;

import com.wechat.pay.java.service.payments.model.Transaction;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.dows.member.handler.pay.AliPayHandler;
import org.dows.member.handler.pay.WechatPayHandler;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;

import java.util.ArrayList;
import java.util.List;

/**
 * 支付预期
 */
@RequiredArgsConstructor
@Configuration
@EnableScheduling
@Slf4j
public class PayOverdueScheduler {
    private final WechatPayHandler wechatPayHandler;

    private final AliPayHandler aliPayHandler;

    // 每天01:00:00执行（CRON表达式）
    @Scheduled(cron = "0 0 1 * * ?")
    public void PayOverdueTask() {
        log.info("PayOverdueScheduler: {}", System.currentTimeMillis());
        //查询所有待支付的订单 下面list代表订单表未支付的 s是订单号
        List<String> list = new ArrayList<>();
        for (String s : list) {
            String wechatPayStatus= wechatPayHandler.wechatPayStatus(s).getTradeState();
            //当时支付宝时候 CLOSED‌过期 REVOKED‌撤销 PAYERROR‌支付失败
          if (Transaction.TradeStateEnum.CLOSED.equals(wechatPayStatus)||Transaction.TradeStateEnum.REVOKED.equals(wechatPayStatus)||Transaction.TradeStateEnum.PAYERROR.equals(wechatPayStatus)){
                //修改订单状态
          }

            //当时支付宝时候
            String aliPayStatus=aliPayHandler.aliPayStatus(s).getTradeState();
          //TRADE_CLOSED 过期  FAIL失败
            if ("TRADE_CLOSED".equals(aliPayStatus)||"FAIL".equals(aliPayHandler.aliPayStatus(aliPayStatus))){
                //修改订单状态
            }
        }

        log.info("DueMemberScheduler定时任务执行结束: {}", System.currentTimeMillis());
    }
}
