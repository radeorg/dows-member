package org.dows.member.biz.utils;

import org.apache.commons.lang3.StringUtils;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Date;

/**
 * 支付时间统一转换工具
 * 支持支付宝Date对象和微信RFC3339格式字符串的统一转换
 */
public class PaymentTimeConverter {

    /**
     * RFC3339 日期时间格式器
     */
    private static final DateTimeFormatter RFC3339_FORMATTER =
            DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ssXXX");

    /**
     * 默认日期时间格式器
     */
    private static final DateTimeFormatter DEFAULT_FORMATTER =
            DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    /**
     * 将支付宝Date对象或微信RFC3339字符串统一转换为LocalDateTime
     *
     * @param timeObject 时间对象，可以是Date或String
     * @return LocalDateTime对象，转换失败返回null
     */
    public static LocalDateTime toLocalDateTime(Object timeObject) {
        if (timeObject == null) {
            return null;
        }

        try {
            if (timeObject instanceof Date) {
                // 支付宝支付时间：Date对象转LocalDateTime
                return ((Date) timeObject).toInstant()
                        .atZone(ZoneId.systemDefault())
                        .toLocalDateTime();
            } else if (timeObject instanceof String timeStr) {
                // 微信支付时间：RFC3339格式字符串转LocalDateTime
                ZonedDateTime zonedDateTime = ZonedDateTime.parse(timeStr, RFC3339_FORMATTER);
                return zonedDateTime.toLocalDateTime();
            } else {
                throw new IllegalArgumentException("不支持的时间对象类型: " + timeObject.getClass().getName());
            }
        } catch (Exception e) {
            System.err.println("时间格式转换失败: " + e.getMessage());
            return null;
        }
    }

    /**
     * 将LocalDateTime转换为指定格式的字符串
     *
     * @param localDateTime LocalDateTime对象
     * @param formatter 目标格式器
     * @return 格式化后的字符串
     */
    public static String format(LocalDateTime localDateTime, DateTimeFormatter formatter) {
        if (localDateTime == null || formatter == null) {
            return null;
        }
        return localDateTime.format(formatter);
    }

    public static LocalDateTime format(String successTime) {
        if (StringUtils.isEmpty(successTime)) {
            return null;
        }
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ssXXX");
        ZonedDateTime zonedDateTime = ZonedDateTime.parse(successTime, formatter);
        return LocalDateTime.from(zonedDateTime.toInstant());
    }

    /**
     * 将LocalDateTime转换为默认格式的字符串
     *
     * @param localDateTime LocalDateTime对象
     * @return 默认格式的字符串
     */
    public static String formatDefault(LocalDateTime localDateTime) {
        return format(localDateTime, DEFAULT_FORMATTER);
    }

    /**
     * 将LocalDateTime转换为RFC3339格式的字符串
     *
     * @param localDateTime LocalDateTime对象
     * @return RFC3339格式字符串
     */
    public static String formatRFC3339(LocalDateTime localDateTime) {
        return format(localDateTime, RFC3339_FORMATTER);
    }

    /**
     * 获取当前时间的LocalDateTime
     *
     * @return 当前LocalDateTime
     */
    public static LocalDateTime now() {
        return LocalDateTime.now();
    }
}