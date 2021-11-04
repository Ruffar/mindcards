package com.raffier.mindcards.repository;

import java.sql.SQLException;

@FunctionalInterface
public interface SQLFunction<T,S> {

    S apply(T t) throws SQLException;

}
