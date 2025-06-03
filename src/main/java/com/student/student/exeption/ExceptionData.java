package com.student.student.exeption;


import lombok.Getter;
import lombok.Setter;
import org.springframework.http.HttpStatus;

@Getter
@Setter
public class ExceptionData extends RuntimeException {

    private HttpStatus status;

    public ExceptionData(ErrorMessage errorMessage) {
        super(errorMessage.getMessage());
    }
    public ExceptionData(HttpStatus httpStatus, ErrorMessage errorMessage) {
        super(errorMessage.getMessage());
        this.status = httpStatus;
    }
}
