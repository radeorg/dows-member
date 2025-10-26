package org.dows.member.biz.util;

import java.time.LocalDate;
import java.time.LocalDateTime;

public class CommonUtils {

    /**
     * 判断Date对象是否表示今天（系统默认时区）
     * @param date 待判断的日期对象
     * @return true-是当天 false-不是当天
     */
    public static boolean isToday(LocalDateTime date) {
        if (date == null) return false;
        LocalDate inputDate = date.toLocalDate();
        return inputDate.equals(LocalDate.now());
    }
}
