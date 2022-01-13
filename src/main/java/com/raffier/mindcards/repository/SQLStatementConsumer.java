package com.raffier.mindcards.repository;

import java.sql.PreparedStatement;
import java.sql.SQLException;

@FunctionalInterface
public interface SQLStatementConsumer {
    void accept(PreparedStatement t) throws SQLException;
}
