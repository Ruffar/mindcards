package com.raffier.mindcards.controller;

import com.raffier.mindcards.errorHandling.*;
import com.raffier.mindcards.model.web.AppResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;
import javax.servlet.http.HttpServletRequest;

//Controller Advice makes it so any controller logic not handled will be redirected to this class
//Any errors thrown within a controller's call stack will be handled here
@ControllerAdvice
public class AppExceptionHandler extends ResponseEntityExceptionHandler {

    //Exceptions
    @ExceptionHandler(ImageChangeException.class)
    private Object handleImageChangeException(ImageChangeException e, HttpServletRequest request) {
        return buildResponse(request, new AppResponse(HttpStatus.NOT_ACCEPTABLE, e.getMessage()));
    }

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
        String requestedWith = request.getHeader("X-Requested-With"); //Get the "Request with" header of the request
        return requestedWith != null && requestedWith.equals("XMLHttpRequest"); //Returns true if the header is found and it is request by an XML request
    }

    private Object buildResponse(HttpServletRequest request, AppResponse error) {
        if (isXMLRequest(request)) { //If the request was an XML request, return only the error response
            return new ResponseEntity<>(error,error.getStatus());
        } else { //If the request was a page request, redirect to error page instead
            ModelAndView mv = new ModelAndView("error"); //Redirect page
            mv.setStatus(error.getStatus()); //Set status of the page
            mv.addObject("error",error); //Attach error data such as message and code to Thymeleaf page
            return mv;
        }
    }
}
