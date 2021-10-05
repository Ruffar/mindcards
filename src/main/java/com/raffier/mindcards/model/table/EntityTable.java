package com.raffier.mindcards.model.table;

public abstract class EntityTable<ID> {

    protected final String tableName;

    protected EntityTable(String tableName) {
        this.tableName = tableName;
    }

    public abstract ID getPrimaryKey();
    public String getTableName() { return this.tableName; }

    public abstract void setPrimaryKey(ID newId);

}
