package org.dows.member.biz.util;

import org.dows.member.enums.PayChannelEnum;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashSet;
import java.util.Random;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;

/**
 * 订单号生成器
 * 规则：两位年 + 一年天数 + 时 + 分 + 平台标识 + 6位单位内时间序列
 */
public class OrderNumberGenerator {
    private static final int RANDOM_LENGTH = 5;
    private static final long MAX_RANDOM = 99999L;
    private static final AtomicLong sequence = new AtomicLong(0);
    private static final ConcurrentHashMap<String, Set<Long>> usedRandoms = new ConcurrentHashMap<>();
    private static final Random random = new Random();

    /**
     * 生成订单号
     * @param platformCode 平台标识：01-支付宝，02-微信
     * @return 生成的订单号
     */
    public static String generateOrderNo(String platformCode) {
        String timestamp = getCurrentTimestamp();
        long randomNum = generateUniqueRandom(timestamp);
        return platformCode + timestamp + String.format("%05d", randomNum);
    }

    /**
     * 获取当前时间戳（yyyyMMddHHmmss格式）
     */
    private static String getCurrentTimestamp() {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");
        return sdf.format(new Date());
    }

    /**
     * 生成唯一随机数
     * @param timestamp 时间戳
     * @return 唯一随机数
     */
    private static long generateUniqueRandom(String timestamp) {
        Set<Long> timestampRandoms = usedRandoms.computeIfAbsent(timestamp, k -> new HashSet<>());

        long randomNum;
        int retryCount = 0;
        final int MAX_RETRY = 100;

        synchronized (timestampRandoms) {
            do {
                randomNum = random.nextInt((int) (MAX_RANDOM + 1));

                if (!timestampRandoms.contains(randomNum)) {
                    timestampRandoms.add(randomNum);
                    break;
                }

                retryCount++;
                if (retryCount >= MAX_RETRY) {
                    // 如果当前时间戳的随机数已用完，使用序列号确保唯一性
                    randomNum = sequence.getAndIncrement() % (MAX_RANDOM + 1);
                    timestampRandoms.add(randomNum);
                    break;
                }
            } while (true);
        }

        // 清理过期的时间戳数据（避免内存泄漏）
        cleanupOldTimestamps();

        return randomNum;
    }

    /**
     * 清理过期的时间戳数据
     */
    private static void cleanupOldTimestamps() {
        if (usedRandoms.size() > 100) { // 限制内存使用
            String currentTimestamp = getCurrentTimestamp();
            usedRandoms.keySet().removeIf(key -> key.compareTo(currentTimestamp) < 0);
        }
    }

    /**
     * 批量生成订单号
     * @param count 生成数量
     * @return 订单号数组
     */
    public static String[] generateBatchOrderNos(int count, String platformCode) {
        if (count <= 0) {
            throw new IllegalArgumentException("生成数量必须大于0");
        }

        String[] orderNos = new String[count];
        for (int i = 0; i < count; i++) {
            orderNos[i] = generateOrderNo(platformCode);
        }
        return orderNos;
    }

    /**
     * 验证订单号是否唯一
     * @param orderNo 订单号
     * @return 是否唯一
     */
    public static boolean isOrderNoUnique(String orderNo) {
        if (orderNo == null || orderNo.length() != (14 + RANDOM_LENGTH)) {
            return false;
        }

        String timestamp = orderNo.substring(0, 14);
        long randomNum = Long.parseLong(orderNo.substring(14));

        Set<Long> timestampRandoms = usedRandoms.get(timestamp);
        return timestampRandoms == null || !timestampRandoms.contains(randomNum);
    }

    /**
     * 主方法 - 测试订单号生成
     */
    public static void main(String[] args) {
        System.out.println("=== 订单号生成器测试 ===");

        // 单次生成测试
        String orderNo1 = generateOrderNo(PayChannelEnum.ALI.getCode());
        String orderNo2 = generateOrderNo(PayChannelEnum.WX.getCode());
        System.out.println("订单号1: " + orderNo1);
        System.out.println("订单号2: " + orderNo2);

        // 批量生成测试
        System.out.println("\n=== 批量生成测试 ===");
        String[] batchOrders = generateBatchOrderNos(10, PayChannelEnum.ALI.getDescription());
        for (int i = 0; i < batchOrders.length; i++) {
            System.out.println("订单号 " + (i + 1) + ": " + batchOrders[i]);
        }

        // 唯一性验证测试
        System.out.println("\n=== 唯一性验证测试 ===");
        System.out.println("订单号1是否唯一: " + isOrderNoUnique(orderNo1));
        System.out.println("订单号2是否唯一: " + isOrderNoUnique(orderNo2));

        // 性能测试
        System.out.println("\n=== 性能测试 ===");
        long startTime = System.currentTimeMillis();
        for (int i = 0; i < 1000; i++) {
            generateOrderNo(PayChannelEnum.ALI.getDescription());
        }
        long endTime = System.currentTimeMillis();
        System.out.println("生成1000个订单号耗时: " + (endTime - startTime) + "ms");

        // 并发测试
        System.out.println("\n=== 并发测试 ===");
        Thread[] threads = new Thread[5];
        for (int i = 0; i < threads.length; i++) {
            threads[i] = new Thread(() -> {
                for (int j = 0; j < 3; j++) {
                    String concurrentOrderNo = generateOrderNo(PayChannelEnum.ALI.getCode());
                    System.out.println(Thread.currentThread().getName() + " - 订单号: " + concurrentOrderNo);
                }
            });
            threads[i].start();
        }

        for (Thread thread : threads) {
            try {
                thread.join();
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }
    }
}