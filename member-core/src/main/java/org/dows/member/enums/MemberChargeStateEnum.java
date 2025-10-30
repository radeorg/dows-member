package org.dows.member.enums;

import lombok.Getter;

import java.util.Objects;

/**
 * 会员充值状态
 */
@Getter
public enum MemberChargeStateEnum {

    WAIT_PAY("WAIT_PAY", "待支付"),
    SUCCESS("SUCCESS", "支付完成"),
    FAILED("FAILED", "支付失败"),
    REFUND("REFUND", "已退款"),
    CLOSED("CLOSED", "已关闭"),
    FINISHED("FINISHED", "交易结束"),;

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
