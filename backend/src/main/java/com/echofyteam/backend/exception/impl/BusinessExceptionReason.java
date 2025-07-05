package com.echofyteam.backend.exception.impl;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
public enum BusinessExceptionReason {
    USER_NOT_FOUND("User not found", HttpStatus.NOT_FOUND),
    USER_ALREADY_EXISTS("User already exists", HttpStatus.CONFLICT),

    USERNAME_IS_ALREADY_TAKEN("Username is already taken by another user", HttpStatus.CONFLICT),

    PASSWORDS_DO_NOT_MATCH("Passwords do not match", HttpStatus.BAD_REQUEST),

    ROLE_NOT_FOUND("Role not found", HttpStatus.NOT_FOUND),

    TOKEN_NOT_FOUND("Token not found", HttpStatus.NOT_FOUND),
    INVALID_TOKEN("Invalid token", HttpStatus.BAD_REQUEST),

    FORBIDDEN("Forbidden", HttpStatus.FORBIDDEN),

    FLASHCARD_NOT_FOUND("Flashcard not found", HttpStatus.NOT_FOUND),
    FLASHCARD_WITH_THIS_WORD_IS_ALREADY_EXISTS("Flashcard with this word is already exists", HttpStatus.CONFLICT),
    DUPLICATE_WORDS_IN_BATCH("Duplicate words in batch", HttpStatus.CONFLICT),

    FLASHCARD_SET_NOT_FOUND("Flashcard set not found", HttpStatus.NOT_FOUND)
    ;

    private final String message;
    private final HttpStatus status;

    public String getCode() {
        return name();
    }
}