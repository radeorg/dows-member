package org.dows.member.exception;

import org.dows.rade.exception.RadeException;
import org.dows.rade.status.CommonStatusCode;
import org.dows.rade.status.StatusCode;

public class PayException extends RadeException {

    public PayException() {
    }

    public PayException(String msg) {
        super(Integer.valueOf(CommonStatusCode.FAILED.getCode()), msg);
    }

    public PayException(Integer code, String msg) {
        super(msg);
    }

    public PayException(Integer code, String msg, Throwable e) {
        super(msg, e);
    }

    public PayException(Object data) {
        super(data);
    }

    public PayException(Throwable throwable) {
        super(throwable);
    }

    public PayException(String message, Throwable throwable) {
        super(message, throwable);
    }

    public PayException(StatusCode statusCode) {
        super(statusCode.getDescribe());
        this.statusCode = statusCode;
    }

    public PayException(StatusCode statusCode, Exception exception) {
        super(String.format(statusCode.getDescribe(), exception.getMessage()));
        this.statusCode = statusCode;
    }

    public PayException(StatusCode statusCode, String msg) {
        super(String.format(statusCode.getDescribe(), msg));
        this.statusCode = statusCode;
    }

    public PayException(StatusCode statusCode, Object[] args, String message) {
        super(message);
        this.statusCode = statusCode;
        this.args = args;
    }

    public PayException(StatusCode statusCode, Object[] args, String message, Throwable cause) {
        super(message, cause);
        this.statusCode = statusCode;
        this.args = args;
    }
}
