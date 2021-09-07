package com.raffier.mindcards.data.table;

import com.raffier.mindcards.data.AppDatabase;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;

public abstract class DatabaseTable {

    protected final AppDatabase database;
    protected final String tableName;

    protected DatabaseTable(AppDatabase database, String tableName) {
        this.database = database;
        this.tableName = tableName;
    }

    public abstract void delete();

}
