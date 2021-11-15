package com.raffier.mindcards.model.table;

public abstract class EntityTable<ID> {

    protected final String tableName;

    protected EntityTable(String tableName) {
        this.tableName = tableName;
    }

    public abstract ID getPrimaryKey();
    public String getTableName() { return this.tableName; }

    //Primary key should only be set at construction and couldn't be changed

}
