package com.raffier.mindcards.controller;

import com.raffier.mindcards.errorHandling.*;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
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
        return buildResponse(request, new AppError(HttpStatus.UNAUTHORIZED, e.getMessage()) );
    }

    @ExceptionHandler(FormFieldException.class)
    private Object handleFormFieldException(FormFieldException e, HttpServletRequest request) {
        return buildResponse(request, new AppError(HttpStatus.NOT_ACCEPTABLE, e.getMessage()) );
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
        String requestedWith = request.getHeader("X-Requested-With");
        return requestedWith != null && requestedWith.equals("XMLHttpRequest");
    }

    private Object buildResponse(HttpServletRequest request, AppError error) {
        if (isXMLRequest(request)) {
            return new ResponseEntity<AppError>(error,error.getStatus());
        } else {
            ModelAndView mv = new ModelAndView();
            mv.setStatus(error.getStatus());
            mv.setViewName("error/generic");

            mv.addObject("error",error);

            return mv;
        }
    }

}
