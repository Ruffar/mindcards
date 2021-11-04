package com.raffier.mindcards.repository.table;

import com.raffier.mindcards.errorHandling.EntityNotFoundException;
import com.raffier.mindcards.repository.AppDatabase;
import com.raffier.mindcards.repository.SQLConsumer;
import com.raffier.mindcards.repository.SQLFunction;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public abstract class EntityRepository<T, ID> {

    protected AppDatabase database;
    public EntityRepository(AppDatabase database) {
        this.database = database;
    }

    protected void throwEntityNotFound(ID id) {
        throw new EntityNotFoundException("Unknown table",id);
    }

    /*
    Statement consumer should include any changes to the prepared statement's parameters (e.g. statement.setInt(1,323))
    Result function includes anything that should be done to the results of the query
     */
    protected <S> S executeQuery(String statement, SQLConsumer<PreparedStatement> statementConsumer, SQLFunction<ResultSet,S> resultFunction) {
        try {
            PreparedStatement stmnt = database.getConnection().prepareStatement(statement);
            statementConsumer.accept(stmnt);
            return resultFunction.apply(stmnt.executeQuery());
        } catch (SQLException e) {
            System.out.println(e.getMessage());
            return null;
        }
    }

    protected int executeUpdate (String statement, SQLConsumer<PreparedStatement> statementConsumer) {
        try {
            PreparedStatement stmnt = database.getConnection().prepareStatement(statement);
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

    public abstract <S extends T> void save(S entity);

    public abstract T getById(ID id);

    public abstract <S extends T> T add(S entity);

    public abstract <S extends T> void delete(S entity);

    public abstract void deleteById(ID id);

}
