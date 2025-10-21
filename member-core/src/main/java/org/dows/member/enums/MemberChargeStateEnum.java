package org.dows.member.enums;

import lombok.Getter;

import java.util.Objects;

/**
 * 会员充值状态
 */
@Getter
public enum MemberChargeStateEnum {

    PENGDING("0", "支付中"),
    COMPLETETED("1", "已完成"),
    FAILED("2", "失败"),
    REFUNDED("3", "已退款");

    private final String code;
    private final String description;

    MemberChargeStateEnum(String code, String description) {
        this.code = code;
        this.description = description;
    }

    public static MemberChargeStateEnum getByCode(String code, boolean throwException) {
        for (MemberChargeStateEnum type : MemberChargeStateEnum.values()) {
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
        for (MemberChargeStateEnum type : MemberChargeStateEnum.values()) {
            if (Objects.equals(type.getCode(), code)) {
                return type.getDescription();
            }
        }
        return null;
    }
}
