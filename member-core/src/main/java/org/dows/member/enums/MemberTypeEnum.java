package org.dows.member.enums;

import lombok.Getter;

import java.util.Objects;

/**
 * 会员等级枚举
 */
@Getter
public enum MemberTypeEnum {

    FREE("FREE", "免费会员"),
    SILVER("SILVER", "白银会员"),
    GOLD("GOLD", "黄金会员"),
    DIAMOND("DIAMOND", "钻石会员");

    private final String code;
    private final String description;

    MemberTypeEnum(String code, String description) {
        this.code = code;
        this.description = description;
    }

    public static MemberTypeEnum getByCode(String code, boolean throwException) {
        for (MemberTypeEnum type : MemberTypeEnum.values()) {
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
        for (MemberTypeEnum type : MemberTypeEnum.values()) {
            if (Objects.equals(type.getCode(), code)) {
                return type.getDescription();
            }
        }
        return null;
    }
}
