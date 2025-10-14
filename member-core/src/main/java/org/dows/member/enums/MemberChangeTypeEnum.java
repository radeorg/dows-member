package org.dows.member.enums;

import lombok.Getter;

import java.util.Objects;

/**
 * 会员变更类型枚举
 */
@Getter
public enum MemberChangeTypeEnum {

    REGISTER("REGISTER", "会员注册"),
    UP_GRADE("UP_GRADE", "会员升级"),
    RENEWAL("RENEWAL", "会员续费"),
    EXPIRATION("EXPIRATION", "会员到期");

    private final String code;
    private final String description;

    MemberChangeTypeEnum(String code, String description) {
        this.code = code;
        this.description = description;
    }

    public static MemberChangeTypeEnum getByCode(String code, boolean throwException) {
        for (MemberChangeTypeEnum type : MemberChangeTypeEnum.values()) {
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
        for (MemberChangeTypeEnum type : MemberChangeTypeEnum.values()) {
            if (Objects.equals(type.getCode(), code)) {
                return type.getDescription();
            }
        }
        return null;
    }
}
