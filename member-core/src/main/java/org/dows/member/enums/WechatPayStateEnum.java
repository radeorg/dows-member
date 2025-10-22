package org.dows.member.enums;

import lombok.Getter;

import java.util.Objects;

/**
 * 微信支付状态
 */
@Getter
public enum WechatPayStateEnum {

    SUCCESS("SUCCESS", "支付成功"),
    REFUND("REFUND", "转入退款"),
    NOT_PAY("NOTPAY", "未付款"),
    CLOSED("CLOSED", "已关闭"),
    REVOKED("REVOKED", "已撤销"),
    USER_PAYING("USERPAYING", "用户支付中"),
    PAY_ERROR("REVOKED", "支付失败");

    private final String code;
    private final String description;

    WechatPayStateEnum(String code, String description) {
        this.code = code;
        this.description = description;
    }

    public static WechatPayStateEnum getByCode(String code, boolean throwException) {
        for (WechatPayStateEnum type : WechatPayStateEnum.values()) {
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
        for (WechatPayStateEnum type : WechatPayStateEnum.values()) {
            if (Objects.equals(type.getCode(), code)) {
                return type.getDescription();
            }
        }
        return null;
    }
}
