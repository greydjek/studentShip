package com.student.student.controller.exceptionController;

import com.student.student.exeption.ExceptionData;
import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.boot.web.servlet.error.ErrorController;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@ControllerAdvice
public class GlobalExceptionController implements ErrorController {

    @RequestMapping("/error")
    public String handleError(HttpServletRequest request) {
        Object status = request.getAttribute(RequestDispatcher.ERROR_STATUS_CODE);

        if (status != null) {
                int statusCode = Integer.parseInt(status.toString());
                if (statusCode == HttpStatus.NOT_FOUND.value()) {
                    return "error404";
                } else if (statusCode == HttpStatus.INTERNAL_SERVER_ERROR.value()) {
                    return "error500";
                }
            }

        return "error";
    }

    @ExceptionHandler(ExceptionData.class)
    public String getExceptionData
(ExceptionData ex){
        return "error404";//todo рассмотреть вариант перенесения кастомной логики в hendlerError с помощью MyExeption
    }

    @ExceptionHandler(Throwable.class)
    public String handlerThrowable(Throwable throwable) {
        return "redirect:/error";

    }

}
