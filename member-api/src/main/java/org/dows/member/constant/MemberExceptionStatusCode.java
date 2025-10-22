package org.dows.member.constant;

import lombok.Getter;
import org.dows.rade.status.StatusCode;

@Getter
public enum MemberExceptionStatusCode implements StatusCode {
    HAS_DISABLED("MEMBER000001", "已禁用"),
    REPEAT_DISABLED("MEMBER000002", "已禁用，不能重复操作"),
    REPEAT_ENABLED("MEMBER000003", "已启用，不能重复操作"),
    INTERESTS_NOT_FOUND("MEMBER001001", "会员权益不存在"),
    INTERESTS_TYPE_HAS_EXIST("MEMBER001002", "会员权益等级已存在"),
    MEMBER_INSTANCE_NOT_FOUND("MEMBER002001", "会员不存在"),
    MEMBER_INSTANCE_EXIST("MEMBER002002", "会员已存在"),
    METRICS_NOT_FOUND("MEMBER003001", "会员度量不存在"),
    METRICS_ACTIVE_INVITE_LOWER_LIMIT("MEMBER003004", "同时邀约次数已达下限");

    private final String code;
    private final String describe;

    MemberExceptionStatusCode(String code, String describe) {
        this.code = code;
        this.describe = describe;
    }
}
