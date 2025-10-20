package org.dows.member.enums;

import lombok.Getter;

import java.util.Objects;

/**
 * 充值渠道枚举
 */
@Getter
public enum PayChannelEnum {

    WECHAT("WECHAT", "微信"),
    ALI("ALI", "支付宝");

    private final String code;
    private final String description;

    PayChannelEnum(String code, String description) {
        this.code = code;
        this.description = description;
    }

    public static PayChannelEnum getByCode(String code, boolean throwException) {
        for (PayChannelEnum type : PayChannelEnum.values()) {
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
        for (PayChannelEnum type : PayChannelEnum.values()) {
            if (Objects.equals(type.getCode(), code)) {
                return type.getDescription();
            }
        }
        return null;
    }
}
