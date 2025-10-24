//package org.dows.member.handler.task;
//
//import lombok.extern.slf4j.Slf4j;
//import org.dows.member.handler.pay.AliPayBiz;
//import org.dows.member.response.pay.AliPayStatusResponse;
//import org.springframework.context.annotation.Configuration;
//
//import java.util.Map;
//import java.util.concurrent.CompletableFuture;
//import java.util.concurrent.TimeUnit;
//
///**
// * 支付宝支付轮询任务
// */
//@Slf4j
//public class AliPayPollingTask implements Runnable {
//    private final String outTradeNo;
//    private final int maxPollingTimes;
//    private final int intervalSeconds;
//    private final CompletableFuture<AliPayStatusResponse> future;
//    private int currentPollingCount = 0;
//    private AliPayBiz aliPayBiz;
//
//    public AliPayPollingTask(String outTradeNo,
//                             int maxPollingTimes,
//                             int intervalSeconds,
//                             CompletableFuture<Map<String, String>> future,
//                             AliPayBiz aliPayBiz) {
//        this.outTradeNo = outTradeNo;
//        this.maxPollingTimes = maxPollingTimes;
//        this.intervalSeconds = intervalSeconds;
//        this.future = future;
//        this.aliPayBiz = aliPayBiz;
//    }
//
//    @Override
//    public void run() {
//        currentPollingCount++;
//        log.info("开始第{}次轮询查询，订单号: {}", currentPollingCount, outTradeNo);
//
//        try {
//            AliPayStatusResponse response = aliPayBiz.aliPayStatus(outTradeNo);
//            String tradeStatus = response.getTradeState();
//
//            // 判断交易状态
//            if ("TRADE_SUCCESS".equals(tradeStatus)) {
//                // 支付成功，完成Future
//                future.complete(response);
//                log.info("轮询查询到支付成功，订单号: {}", outTradeNo);
//            } else if ("TRADE_CLOSED".equals(tradeStatus) || "TRADE_FINISHED".equals(tradeStatus)) {
//                // 交易已关闭或完成
//                future.complete(response);
//                log.info("轮询查询到交易已关闭/完成，订单号: {}", outTradeNo);
//            } else if ("WAIT_BUYER_PAY".equals(tradeStatus)) {
//                // 等待买家付款
//                if (currentPollingCount >= maxPollingTimes) {
//                    // 达到最大轮询次数，撤销交易
//                    log.warn("达到最大轮询次数，准备撤销交易，订单号: {}", outTradeNo);
//                    aliPayBiz.cancelPay(outTradeNo);
//                    AliPayStatusResponse result = new AliPayStatusResponse(orderStatus);
//                    result.put("tradeStatus", "TRADE_CANCELED");
//                    future.complete(result);
//                } else {
//                    // 继续轮询
//                    log.info("等待买家付款，继续轮询，订单号: {}", outTradeNo);
//                    pollingExecutor.schedule(this, intervalSeconds, TimeUnit.SECONDS);
//                }
//            } else {
//                // 其他状态
//                future.complete(orderStatus);
//                log.info("轮询查询到其他状态: {}，订单号: {}", tradeStatus, outTradeNo);
//            }
//        } catch (Exception e) {
//            log.error("轮询查询异常，订单号: {}", outTradeNo, e);
//            if (currentPollingCount >= maxPollingTimes) {
//                future.completeExceptionally(e);
//            } else {
//                // 异常情况下仍继续轮询
//                pollingExecutor.schedule(this, intervalSeconds, TimeUnit.SECONDS);
//            }
//        }
//    }
//}
