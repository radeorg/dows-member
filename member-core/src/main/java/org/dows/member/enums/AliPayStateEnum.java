package org.dows.member.enums;

import lombok.Getter;

import java.util.Objects;

/**
 * 支付宝支付状态
 */
@Getter
public enum AliPayStateEnum {

    WAIT_BUYER_PAY("WAIT_BUYER_PAY", "待付款"),
    TRADE_SUCCESS("TRADE_SUCCESS", "支付成功"),
    TRADE_CLOSED("TRADE_CLOSED", "交易关闭"),
    TRADE_FINISHED("TRADE_FINISHED", "交易成功");

    private final String code;
    private final String description;

    AliPayStateEnum(String code, String description) {
        this.code = code;
        this.description = description;
    }

    public static AliPayStateEnum getByCode(String code, boolean throwException) {
        for (AliPayStateEnum type : AliPayStateEnum.values()) {
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
        for (AliPayStateEnum type : AliPayStateEnum.values()) {
            if (Objects.equals(type.getCode(), code)) {
                return type.getDescription();
            }
        }
        return null;
    }
}
