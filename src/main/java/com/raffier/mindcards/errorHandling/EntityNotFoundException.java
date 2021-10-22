package com.raffier.mindcards.errorHandling;

public class EntityNotFoundException extends RuntimeException {

    public EntityNotFoundException(String tableName, Object id) {
        super(tableName+" with ID "+id.toString()+" cannot be found...");
    }

    public EntityNotFoundException(String tableName, String selector, Object id) {
        super(tableName+" with "+selector+" "+id.toString()+" cannot be found...");
    }

    public EntityNotFoundException(String message) { super(message); }

}
