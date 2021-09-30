package com.raffier.mindcards.model.table;

public abstract class EntityTable {

    //protected final AppDatabase database;
    protected final String tableName;

    protected EntityTable(String tableName) {
        //this.database = database;
        this.tableName = tableName;
    }

}
