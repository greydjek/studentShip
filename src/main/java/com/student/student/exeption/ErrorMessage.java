package com.student.student.exeption;

import lombok.Getter;

@Getter
public enum ErrorMessage {

    USER_NOT_FOUND("user not found");

    private final String message;

    ErrorMessage(String message) {
        this.message = message;
    }
}
