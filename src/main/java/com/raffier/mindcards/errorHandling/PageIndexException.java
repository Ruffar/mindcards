package com.raffier.mindcards.errorHandling;

public class PageIndexException extends RuntimeException {

    public PageIndexException() {
        super("Page out of bounds...");
    }

    public PageIndexException(int pageNo) {
        super("Page "+pageNo+" is out of bounds...");
    }

}
