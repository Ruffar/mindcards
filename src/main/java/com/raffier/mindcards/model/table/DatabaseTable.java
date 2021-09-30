package com.raffier.mindcards.model.table;

public abstract class DatabaseTable {

    //protected final AppDatabase database;
    protected final String tableName;

    protected DatabaseTable(String tableName) {
        //this.database = database;
        this.tableName = tableName;
    }

    public abstract void delete();

}
