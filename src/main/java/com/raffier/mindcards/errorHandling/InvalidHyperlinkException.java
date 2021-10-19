package com.raffier.mindcards.errorHandling;

public class InvalidHyperlinkException extends RuntimeException {

    public InvalidHyperlinkException(String link) {
        super("\""+link+"\" is an invalid hyperlink");
    }

}
