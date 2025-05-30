package com.student.student.exeption;

import lombok.Getter;

@Getter
public enum ErrorMessage {

    USER_NOT_FOUND("user not found"),
    DATA_NOT_NULL("Имя, фамилия, курс, специальность не могут быть пропущены, заполните и попробуйте снова"),
    COMPANY_NOT_FOUND("компания с таким названием не найдена");

    private final String message;

    ErrorMessage(String message) {
        this.message = message;
    }
}
