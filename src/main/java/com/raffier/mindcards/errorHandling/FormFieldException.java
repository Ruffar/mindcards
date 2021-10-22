package com.raffier.mindcards.errorHandling;

public class FormFieldException extends RuntimeException {

    public FormFieldException() {
        super();
    }

    public FormFieldException(String message) {
        super(message);
    }

}
