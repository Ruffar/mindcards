package com.raffier.mindcards.repository;

import java.sql.ResultSet;
import java.sql.SQLException;

@FunctionalInterface
public interface SQLResultFunction<S> {
    S apply(ResultSet t) throws SQLException;
}
