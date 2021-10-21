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

    private boolean isXMLRequest(HttpServletRequest request) {
        return request.getHeader("X-Requested-With").equals("XMLHttpRequest");
    }

    @ExceptionHandler(InvalidHyperlinkException.class)
    private ModelAndView handleInvalidHyperlink(InvalidHyperlinkException e, HttpServletRequest request) {
        if (isXMLRequest(request)) {

        } else {
            return errorPage(new AppError(HttpStatus.NOT_ACCEPTABLE, e.getMessage()));
        }
    }

    @ExceptionHandler(UnauthorisedAccessException.class)
    private ModelAndView handleUnauthorisedAccess(UnauthorisedAccessException e) {
        return errorPage( new AppError(HttpStatus.FORBIDDEN, e.getMessage()) );
    }

    @ExceptionHandler(EntityNotFoundException.class)
    private ModelAndView handleEntityNotFoundException(EntityNotFoundException e) {
        return errorPage( new AppError(HttpStatus.NOT_FOUND, e.getMessage()) );
    }

    @ExceptionHandler(RuntimeException.class)
    private ModelAndView handleRuntimeException(RuntimeException e) {
        return errorPage( new AppError(HttpStatus.BAD_REQUEST, e.getMessage()) );
    }

    private ModelAndView errorPage(AppError error) {
        ModelAndView mv = new ModelAndView();
        mv.setStatus(error.getStatus());
        mv.setViewName("error/generic");

        mv.addObject("error",error);

        return mv;
    }

    private ResponseEntity<?> buildResponseEntity(AppError error) {
        return new ResponseEntity<>(error,error.getStatus());
    }

}
