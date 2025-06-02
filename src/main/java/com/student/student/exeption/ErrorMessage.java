package com.student.student.exeption;

import lombok.Getter;

@Getter
public enum ErrorMessage {

    STUDENT_NOT_FOUND("Студент не найден"),
    DATA_COMPANY_NOT_NULL("название компании и адрес необходимо заполнить"),
    DATA_STUDENT_NOT_NULL("Имя, фамилия, курс, специальность не могут быть пропущены, заполните и попробуйте снова"),
    COMPANY_NOT_FOUND("компания с таким названием не найдена"),
    NOT_FOUND_LIKE_NAME("студентов с похожими именами не найдено"),
    INTERN_SHIP_NOT_FOUND("практика не найдена уточняйте данные");

    private final String message;

    ErrorMessage(String message) {
        this.message = message;
    }
}
