package com.raffier.mindcards.errorHandling;

public class EntityNotFoundException extends RuntimeException {

    public EntityNotFoundException(String tableName, Object id) {
        super(tableName+" with ID "+id.toString()+" cannot be found...");
    }

    protected EntityNotFoundException(String message) { super(message); }

}
