package com.raffier.mindcards.repository.table;

import com.raffier.mindcards.errorHandling.EntityNotFoundException;
import com.raffier.mindcards.repository.AppDatabase;
import com.raffier.mindcards.repository.SQLConsumer;
import com.raffier.mindcards.repository.SQLFunction;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public abstract class EntityRepository<T, ID> {

    protected Connection connection;
    protected EntityRepository(AppDatabase database) {
        this.connection = database.getConnection();
    }

    protected void throwEntityNotFound(ID id) {
        throw new EntityNotFoundException("Unknown table",id);
    }

    // SQL Utility //
    /*
    Statement consumer should include any changes to the prepared statement's parameters (e.g. statement.setInt(1,323))
    Result function includes anything that should be done to the results of the query
     */
    protected <S> S executeQuery(String statement, SQLConsumer<PreparedStatement> statementConsumer, SQLFunction<ResultSet,S> resultFunction) {
        try {
            PreparedStatement stmnt = connection.prepareStatement(statement);
            statementConsumer.accept(stmnt);
            return resultFunction.apply(stmnt.executeQuery());
        } catch (SQLException e) {
            System.out.println(e.getMessage());
            return null;
        }
    }

    protected int executeUpdate (String statement, SQLConsumer<PreparedStatement> statementConsumer) {
        try {
            PreparedStatement stmnt = connection.prepareStatement(statement);
            statementConsumer.accept(stmnt);
            stmnt.executeUpdate();
            ResultSet generatedIds = stmnt.getGeneratedKeys();
            if (generatedIds.next()) {
                return generatedIds.getInt(1);
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return 0;
    }

    // SQL Statements //
    //Updates
    public abstract void save(T entity);

    public abstract T add(T entity);

    public abstract void delete(T entity);

    public abstract void deleteById(ID id);

    //Queries
    public abstract T getById(ID id);

}
