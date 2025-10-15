package org.dows.member.enums;

import lombok.Getter;

import java.util.Objects;

/**
 * 会员使用时间段枚举
 */
@Getter
public enum MemberInterestsUsedDateEnum {

    TUE_FRI("TUE,FRI", "周二、周五"),
    MON_FRI("MON-FRI", "周一至周五"),
    MON_SUN("MON-SUN", "周一至周日"),
    DAILY("DAILY", "每天");

    private final String code;
    private final String description;

    MemberInterestsUsedDateEnum(String code, String description) {
        this.code = code;
        this.description = description;
    }

    public static MemberInterestsUsedDateEnum getByCode(String code, boolean throwException) {
        for (MemberInterestsUsedDateEnum type : MemberInterestsUsedDateEnum.values()) {
            if (Objects.equals(type.getCode(), code)) {
                return type;
            }
        }
        if (throwException) {
            throw new IllegalArgumentException("Invalid ResumeProcessedEnum code: " + code);
        }
        return null;
    }

    public static String getDescByCode(String code) {
        for (MemberInterestsUsedDateEnum type : MemberInterestsUsedDateEnum.values()) {
            if (Objects.equals(type.getCode(), code)) {
                return type.getDescription();
            }
        }
        return null;
    }
}
