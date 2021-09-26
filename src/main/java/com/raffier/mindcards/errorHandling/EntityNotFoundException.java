package com.raffier.mindcards.errorHandling;

import com.raffier.mindcards.model.table.DatabaseTable;

public class EntityNotFoundException extends RuntimeException {

    public EntityNotFoundException(String tableName, int id) {
        super(tableName+" with ID "+id+" cannot be found...");
    }

    protected EntityNotFoundException(String message) { super(message); }

}
