package com.echofyteam.backend.exception.impl;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
public enum BusinessExceptionReason {
    USER_NOT_FOUND("User not found", HttpStatus.NOT_FOUND),
    USER_ALREADY_EXISTS("User already exists", HttpStatus.BAD_REQUEST),

    TOKEN_NOT_FOUND("Token not found", HttpStatus.NOT_FOUND),
    INVALID_TOKEN("Invalid token", HttpStatus.BAD_REQUEST),

    FORBIDDEN("Forbidden", HttpStatus.FORBIDDEN),

    ;

    private final String message;
    private final HttpStatus status;

    public String getCode() {
        return name();
    }
}