package com.raffier.mindcards.model.table;

public abstract class TitledCardTable extends CardTable {

    protected String title;

    protected TitledCardTable(String tableName, String primaryKeyName) {
        super(tableName,primaryKeyName);
        this.title = "";
    }

    public String getTitle() { return this.title; }
    public void setTitle(String title) { this.title = title; }
}
