package com.raffier.mindcards.model.web;

import org.springframework.http.HttpStatus;

import java.io.Serializable;

public class AppResponse implements Serializable {

    private final HttpStatus status;
    private String message;

    public AppResponse(HttpStatus status) {
        this.status = status;
    }

    public AppResponse(HttpStatus status, String message) {
        this(status);
        this.message = message;
    }

    public AppResponse(HttpStatus status, Throwable e) {
        this(status);
        this.message = "Unexpected Error...";
    }

    public AppResponse(HttpStatus status, Throwable e, String message) {
        this(status, message);
    }


    public HttpStatus getStatus() { return status; }
    public String getMessage() { return message; }

}
