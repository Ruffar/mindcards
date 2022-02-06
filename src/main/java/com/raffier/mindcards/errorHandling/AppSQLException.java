package com.raffier.mindcards.errorHandling;

import java.sql.SQLException;

public class AppSQLException extends SQLException {
    public AppSQLException() {
        super();
    }
    public AppSQLException(String message) {
        super(message);
    }
}
