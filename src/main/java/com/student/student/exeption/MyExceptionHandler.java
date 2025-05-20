package com.student.student.exeption;


public class MyExceptionHandler extends RuntimeException {

    public MyExceptionHandler(ErrorMessage errorMessage) {
        super(errorMessage.getMessage());
    }
}
