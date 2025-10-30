package org.dows.member.biz.pay;

import lombok.extern.slf4j.Slf4j;
import org.dows.member.enums.MemberChargeStateEnum;
import org.dows.member.response.pay.AliPayStatusResponse;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

/**
 * 支付宝支付轮询任务
 */
@Slf4j
public class AliPayPollingTask implements Runnable {
    private final String outTradeNo;
    private final int maxPollingTimes;
    private final int intervalSeconds;
    private final CompletableFuture<AliPayStatusResponse> future;
    private int currentPollingCount = 0;
    private final ScheduledExecutorService pollingExecutor;
    private final PaymentBiz paymentBiz;

    public AliPayPollingTask(String outTradeNo,
                             int maxPollingTimes,
                             int intervalSeconds,
                             CompletableFuture<AliPayStatusResponse> future,
                             ScheduledExecutorService pollingExecutor,
                             PaymentBiz paymentBiz) {
        this.outTradeNo = outTradeNo;
        this.maxPollingTimes = maxPollingTimes;
        this.intervalSeconds = intervalSeconds;
        this.future = future;
        this.pollingExecutor = pollingExecutor;
        this.paymentBiz = paymentBiz;
    }

    @Override
    public void run() {
        currentPollingCount++;
        log.info("开始第{}次轮询查询，订单号: {}", currentPollingCount, outTradeNo);

        try {
            AliPayStatusResponse response = paymentBiz.aliPayStatus(outTradeNo);
            String tradeStatus = response.getTradeState();

            // 判断交易状态
            if (MemberChargeStateEnum.SUCCESS.getCode().equals(tradeStatus)) {
                // 支付成功，完成Future
                future.complete(response);

                log.info("轮询查询到支付成功，订单号: {}", outTradeNo);
            } else if (MemberChargeStateEnum.CLOSED.getCode().equals(tradeStatus)
                    || MemberChargeStateEnum.FINISHED.getCode().equals(tradeStatus)) {
                // 交易已关闭或完成
                future.complete(response);

                log.info("轮询查询到交易已关闭/完成，订单号: {}", outTradeNo);
            } else if (MemberChargeStateEnum.WAIT_PAY.getCode().equals(tradeStatus)) {
                // 等待买家付款
                if (currentPollingCount >= maxPollingTimes) {
                    // 达到最大轮询次数，撤销交易
                    log.warn("达到最大轮询次数，准备撤销交易，订单号: {}", outTradeNo);

                    // 达到最大轮询数撤销交易
                    paymentBiz.cancelAliPay(outTradeNo);

                    response.setTradeState(MemberChargeStateEnum.CANCEL.getCode());

                    future.complete(response);
                } else {
                    // 继续轮询
                    log.info("等待买家付款，继续轮询，订单号: {}", outTradeNo);

                    pollingExecutor.schedule(this, intervalSeconds, TimeUnit.SECONDS);
                }
            } else {
                // 其他状态
                future.complete(response);

                log.info("轮询查询到其他状态: {}，订单号: {}", tradeStatus, outTradeNo);
            }
        } catch (Exception e) {
            log.error("轮询查询异常，订单号: {}", outTradeNo, e);

            if (currentPollingCount >= maxPollingTimes) {
                future.completeExceptionally(e);
            } else {
                // 异常情况下仍继续轮询
                pollingExecutor.schedule(this, intervalSeconds, TimeUnit.SECONDS);
            }
        }
    }
}

