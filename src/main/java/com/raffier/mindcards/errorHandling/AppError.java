package com.raffier.mindcards.errorHandling;

import org.springframework.http.HttpStatus;

public class AppError {

    private HttpStatus status;
    private String message;

    public AppError(HttpStatus status) {
        this.status = status;
    }

    public AppError(HttpStatus status, String message) {
        this(status);
        this.message = message;
    }

    public AppError(HttpStatus status, Throwable e) {
        this(status);
        this.message = "Unexpected Error...";
    }

    public AppError(HttpStatus status, Throwable e, String message) {
        this(status, message);
    }


    public HttpStatus getStatus() { return status; }
    public String getMessage() { return message; }

}
