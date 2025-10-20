package org.dows.member.exception;

import org.dows.rade.exception.RadeException;
import org.dows.rade.status.CommonStatusCode;
import org.dows.rade.status.StatusCode;

public class WechatPayException extends RadeException {

    public WechatPayException() {
    }

    public WechatPayException(String msg) {
        super(Integer.valueOf(CommonStatusCode.FAILED.getCode()), msg);
    }

    public WechatPayException(Integer code, String msg) {
        super(msg);
    }

    public WechatPayException(Integer code, String msg, Throwable e) {
        super(msg, e);
    }

    public WechatPayException(Object data) {
        super(data);
    }

    public WechatPayException(Throwable throwable) {
        super(throwable);
    }

    public WechatPayException(String message, Throwable throwable) {
        super(message, throwable);
    }

    public WechatPayException(StatusCode statusCode) {
        super(statusCode.getDescribe());
        this.statusCode = statusCode;
    }

    public WechatPayException(StatusCode statusCode, Exception exception) {
        super(String.format(statusCode.getDescribe(), exception.getMessage()));
        this.statusCode = statusCode;
    }

    public WechatPayException(StatusCode statusCode, String msg) {
        super(String.format(statusCode.getDescribe(), msg));
        this.statusCode = statusCode;
    }

    public WechatPayException(StatusCode statusCode, Object[] args, String message) {
        super(message);
        this.statusCode = statusCode;
        this.args = args;
    }

    public WechatPayException(StatusCode statusCode, Object[] args, String message, Throwable cause) {
        super(message, cause);
        this.statusCode = statusCode;
        this.args = args;
    }
}
