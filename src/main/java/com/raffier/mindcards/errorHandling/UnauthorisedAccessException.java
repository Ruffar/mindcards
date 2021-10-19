package com.raffier.mindcards.errorHandling;

public class UnauthorisedAccessException extends RuntimeException {

    public UnauthorisedAccessException() {
        super();
    }

    public UnauthorisedAccessException(String message) {
        super(message);
    }

}
