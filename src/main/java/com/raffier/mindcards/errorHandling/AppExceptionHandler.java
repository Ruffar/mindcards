package com.raffier.mindcards.errorHandling;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@ControllerAdvice
public class AppExceptionHandler extends ResponseEntityExceptionHandler {

    @ExceptionHandler(EntityNotFoundException.class)
    private ModelAndView handleEntityNotFoundException(EntityNotFoundException e) {
        return errorPage( new AppError(HttpStatus.NOT_FOUND, e.getMessage()) );
    }

    @ExceptionHandler(RuntimeException.class)
    private ModelAndView handleRuntimeException(RuntimeException e) {
        return errorPage( new AppError(HttpStatus.BAD_REQUEST, e) );
    }

    private ModelAndView errorPage(AppError error) {
        ModelAndView mv = new ModelAndView();
        mv.setStatus(error.getStatus());
        mv.setViewName("error/generic");

        mv.addObject("error",error);

        return mv;
    }

}
