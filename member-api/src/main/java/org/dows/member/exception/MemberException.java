package org.dows.member.exception;

import org.dows.rade.exception.RadeException;
import org.dows.rade.status.CommonStatusCode;
import org.dows.rade.status.StatusCode;

public class MemberException extends RadeException {

    public MemberException() {
    }

    public MemberException(String msg) {
        super(Integer.valueOf(CommonStatusCode.FAILED.getCode()), msg);
    }

    public MemberException(Integer code, String msg) {
        super(msg);
    }

    public MemberException(Integer code, String msg, Throwable e) {
        super(msg, e);
    }

    public MemberException(Object data) {
        super(data);
    }

    public MemberException(Throwable throwable) {
        super(throwable);
    }

    public MemberException(String message, Throwable throwable) {
        super(message, throwable);
    }

    public MemberException(StatusCode statusCode) {
        super(statusCode.getDescribe());
        this.statusCode = statusCode;
    }

    public MemberException(StatusCode statusCode, Exception exception) {
        super(String.format(statusCode.getDescribe(), exception.getMessage()));
        this.statusCode = statusCode;
    }

    public MemberException(StatusCode statusCode, String msg) {
        super(String.format(statusCode.getDescribe(), msg));
        this.statusCode = statusCode;
    }

    public MemberException(StatusCode statusCode, Object[] args, String message) {
        super(message);
        this.statusCode = statusCode;
        this.args = args;
    }

    public MemberException(StatusCode statusCode, Object[] args, String message, Throwable cause) {
        super(message, cause);
        this.statusCode = statusCode;
        this.args = args;
    }
}
