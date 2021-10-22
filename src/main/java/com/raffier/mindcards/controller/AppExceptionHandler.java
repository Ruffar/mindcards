package com.raffier.mindcards.controller;

import com.raffier.mindcards.errorHandling.AppError;
import com.raffier.mindcards.errorHandling.EntityNotFoundException;
import com.raffier.mindcards.errorHandling.InvalidHyperlinkException;
import com.raffier.mindcards.errorHandling.UnauthorisedAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import javax.servlet.http.HttpServletRequest;

@ControllerAdvice
public class AppExceptionHandler extends ResponseEntityExceptionHandler {

    //Exceptions
    @ExceptionHandler(InvalidHyperlinkException.class)
    private Object handleInvalidHyperlink(InvalidHyperlinkException e, HttpServletRequest request) {
        return buildResponse(request, new AppError(HttpStatus.NOT_ACCEPTABLE, e.getMessage()));
    }

    @ExceptionHandler(UnauthorisedAccessException.class)
    private Object handleUnauthorisedAccess(UnauthorisedAccessException e, HttpServletRequest request) {
        return buildResponse(request, new AppError(HttpStatus.FORBIDDEN, e.getMessage()) );
    }

    @ExceptionHandler(EntityNotFoundException.class)
    private Object handleEntityNotFoundException(EntityNotFoundException e, HttpServletRequest request) {
        return buildResponse(request, new AppError(HttpStatus.NOT_FOUND, e.getMessage()) );
    }

    @ExceptionHandler(RuntimeException.class)
    private Object handleRuntimeException(RuntimeException e, HttpServletRequest request) {
        return buildResponse(request, new AppError(HttpStatus.BAD_REQUEST, e.getMessage()) );
    }

    //Building responses
    private boolean isXMLRequest(HttpServletRequest request) {
        return request.getHeader("X-Requested-With").equals("XMLHttpRequest");
    }

    private Object buildResponse(HttpServletRequest request, AppError error) {
        if (isXMLRequest(request)) {
            return new ResponseEntity<>(error,error.getStatus());
        } else {
            ModelAndView mv = new ModelAndView();
            mv.setStatus(error.getStatus());
            mv.setViewName("error/generic");

            mv.addObject("error",error);

            return mv;
        }
    }

}
