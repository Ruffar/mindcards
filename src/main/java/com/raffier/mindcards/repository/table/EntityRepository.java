package com.raffier.mindcards.repository.table;

import com.raffier.mindcards.errorHandling.EntityNotFoundException;
import com.raffier.mindcards.model.table.EntityTable;
import com.raffier.mindcards.repository.AppDatabase;
import com.raffier.mindcards.repository.SQLStatementConsumer;
import com.raffier.mindcards.repository.SQLResultFunction;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

//T is the DTO class used which must be a sublcass of EntityTable using the same data type as its ID
public abstract class EntityRepository<T extends EntityTable<ID>, ID> {

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
    //SQL Queries - any statements that only receive data
    protected <S> S executeQuery(String statement, SQLStatementConsumer statementConsumer, SQLResultFunction<S> resultFunction) throws SQLException {
        PreparedStatement stmnt = connection.prepareStatement(statement);
        statementConsumer.accept(stmnt);
        return resultFunction.apply(stmnt.executeQuery());
    }

    //SQL Updates - any statements that change data
    protected int executeUpdate (String statement, SQLStatementConsumer statementConsumer) throws SQLException {
        PreparedStatement stmnt = connection.prepareStatement(statement);
        statementConsumer.accept(stmnt);
        stmnt.executeUpdate();
        ResultSet generatedIds = stmnt.getGeneratedKeys();
        if (generatedIds.next()) {
            return generatedIds.getInt(1);
        }
        return 0;
    }

    // SQL Statements //
    //Updates
    public abstract void save(T entity) throws SQLException; //Saves the entity

    public abstract T add(T entity) throws SQLException; //Adds the entity and returns a similar object with its new ID

    public abstract void deleteById(ID id) throws SQLException; //Deletes entity with the ID

    public void delete(T entity) throws SQLException { //Shortcut for deleting entity by passing in an entity object
        deleteById(entity.getPrimaryKey());
    }

    //Queries
    public abstract T getById(ID id) throws SQLException; //Returns entity object representing an entity of the ID from the database
}
