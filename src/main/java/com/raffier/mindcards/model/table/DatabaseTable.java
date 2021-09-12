package com.raffier.mindcards.model.table;

import com.raffier.mindcards.model.AppDatabase;

public abstract class DatabaseTable {

    protected final AppDatabase database;
    protected final String tableName;

    protected DatabaseTable(AppDatabase database, String tableName) {
        this.database = database;
        this.tableName = tableName;
    }

    public abstract void delete();

}
