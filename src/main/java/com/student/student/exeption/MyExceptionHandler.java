package com.student.student.exeption;


import lombok.Getter;
import lombok.Setter;
import org.springframework.http.HttpStatus;

@Getter
@Setter
public class MyExceptionHandler extends RuntimeException {

    private HttpStatus status;

    public MyExceptionHandler(ErrorMessage errorMessage) {
        super(errorMessage.getMessage());
    }
    public MyExceptionHandler(HttpStatus httpStatus, ErrorMessage errorMessage) {
        super(errorMessage.getMessage());
        this.status = httpStatus;
    }
}
