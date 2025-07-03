package com.echofyteam.backend.exception.impl;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public class BusinessException extends RuntimeException {
    private final String code;
    private final String message;
    private final HttpStatus status;

    public BusinessException(BusinessExceptionReason reason) {
        this(reason, null, (Object[]) null);
    }

    public BusinessException(BusinessExceptionReason reason, HttpStatus overridingStatus) {
        this(reason, overridingStatus, (Object[]) null);
    }

    public BusinessException(BusinessExceptionReason reason, Object... args) {
        this(reason, null, args);
    }

    public BusinessException(BusinessExceptionReason reason, HttpStatus overridingStatus, Object... args) {
        this.code = reason.getCode();

        this.status = overridingStatus == null ? reason.getStatus() : overridingStatus;

        this.message = (args == null || args.length == 0) ?
                reason.getMessage() : String.format(reason.getMessage(), args);
    }

    @Override
    public String toString() {
        return "BusinessException{" +
                "code='" + code + '\'' +
                ", message='" + message + '\'' +
                ", status=" + status +
                '}';
    }

}