package org.dows.member.enums;

import lombok.Getter;

import java.util.Objects;

/**
 * 充值类型枚举
 */
@Getter
public enum MemberChargeTypeEnum {

    UP_GRADE("UP_GRADE", "会员升级"),
    RENEWAL("RENEWAL", "会员续费");

    private final String code;
    private final String description;

    MemberChargeTypeEnum(String code, String description) {
        this.code = code;
        this.description = description;
    }

    public static MemberChargeTypeEnum getByCode(String code, boolean throwException) {
        for (MemberChargeTypeEnum type : MemberChargeTypeEnum.values()) {
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
        for (MemberChargeTypeEnum type : MemberChargeTypeEnum.values()) {
            if (Objects.equals(type.getCode(), code)) {
                return type.getDescription();
            }
        }
        return null;
    }
}
