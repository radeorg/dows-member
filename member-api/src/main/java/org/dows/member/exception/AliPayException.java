package org.dows.member.exception;

import org.dows.rade.exception.RadeException;
import org.dows.rade.status.CommonStatusCode;
import org.dows.rade.status.StatusCode;

public class AliPayException extends RadeException {

    public AliPayException() {
    }

    public AliPayException(String msg) {
        super(Integer.valueOf(CommonStatusCode.FAILED.getCode()), msg);
    }

    public AliPayException(Integer code, String msg) {
        super(msg);
    }

    public AliPayException(Integer code, String msg, Throwable e) {
        super(msg, e);
    }

    public AliPayException(Object data) {
        super(data);
    }

    public AliPayException(Throwable throwable) {
        super(throwable);
    }

    public AliPayException(String message, Throwable throwable) {
        super(message, throwable);
    }

    public AliPayException(StatusCode statusCode) {
        super(statusCode.getDescribe());
        this.statusCode = statusCode;
    }

    public AliPayException(StatusCode statusCode, Exception exception) {
        super(String.format(statusCode.getDescribe(), exception.getMessage()));
        this.statusCode = statusCode;
    }

    public AliPayException(StatusCode statusCode, String msg) {
        super(String.format(statusCode.getDescribe(), msg));
        this.statusCode = statusCode;
    }

    public AliPayException(StatusCode statusCode, Object[] args, String message) {
        super(message);
        this.statusCode = statusCode;
        this.args = args;
    }

    public AliPayException(StatusCode statusCode, Object[] args, String message, Throwable cause) {
        super(message, cause);
        this.statusCode = statusCode;
        this.args = args;
    }
}
