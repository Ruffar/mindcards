package com.raffier.mindcards.controller;

import com.raffier.mindcards.errorHandling.*;
import com.raffier.mindcards.model.AppResponse;
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
    @ExceptionHandler(PageIndexException.class)
    private Object handlePageIndexException(PageIndexException e, HttpServletRequest request) {
        return buildResponse(request, new AppResponse(HttpStatus.NOT_ACCEPTABLE, e.getMessage()));
    }

    @ExceptionHandler(InvalidHyperlinkException.class)
    private Object handleInvalidHyperlink(InvalidHyperlinkException e, HttpServletRequest request) {
        return buildResponse(request, new AppResponse(HttpStatus.NOT_ACCEPTABLE, e.getMessage()));
    }

    @ExceptionHandler(UnauthorisedAccessException.class)
    private Object handleUnauthorisedAccess(UnauthorisedAccessException e, HttpServletRequest request) {
        return buildResponse(request, new AppResponse(HttpStatus.UNAUTHORIZED, e.getMessage()) );
    }

    @ExceptionHandler(FormFieldException.class)
    private Object handleFormFieldException(FormFieldException e, HttpServletRequest request) {
        return buildResponse(request, new AppResponse(HttpStatus.NOT_ACCEPTABLE, e.getMessage()) );
    }

    @ExceptionHandler(EntityNotFoundException.class)
    private Object handleEntityNotFoundException(EntityNotFoundException e, HttpServletRequest request) {
        return buildResponse(request, new AppResponse(HttpStatus.NOT_FOUND, e.getMessage()) );
    }

    @ExceptionHandler(RuntimeException.class)
    private Object handleRuntimeException(RuntimeException e, HttpServletRequest request) {
        return buildResponse(request, new AppResponse(HttpStatus.BAD_REQUEST, e.getMessage()) );
    }

    //Building responses
    private boolean isXMLRequest(HttpServletRequest request) {
        String requestedWith = request.getHeader("X-Requested-With");
        return requestedWith != null && requestedWith.equals("XMLHttpRequest");
    }

    private Object buildResponse(HttpServletRequest request, AppResponse error) {
        if (isXMLRequest(request)) {
            return new ResponseEntity<>(error,error.getStatus());
        } else {
            ModelAndView mv = new ModelAndView("error");
            mv.setStatus(error.getStatus());

            mv.addObject("error",error);

            return mv;
        }
    }

}
