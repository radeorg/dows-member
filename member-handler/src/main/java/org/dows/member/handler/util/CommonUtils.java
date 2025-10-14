package org.dows.member.handler.util;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;

public class CommonUtils {

    /**
     * 判断Date对象是否表示今天（系统默认时区）
     * @param date 待判断的日期对象
     * @return true-是当天 false-不是当天
     */
    public static boolean isToday(Date date) {
        if (date == null) return false;
        LocalDate inputDate = date.toInstant()
                .atZone(ZoneId.systemDefault())
                .toLocalDate();
        return inputDate.equals(LocalDate.now());
    }
}
