package com.raffier.mindcards.model.table;

import com.raffier.mindcards.util.CardType;

public abstract class CardTable extends EntityTable<Integer> {

    //Database columns
    protected Integer imageId;
    protected String description;

    //Other data
    protected final String primaryKeyName;

    protected CardTable(String tableName, String primaryKeyName) {
        super(tableName);
        this.primaryKeyName = primaryKeyName;
        this.description = "";
    }

    protected CardTable(String tableName, String primaryKeyName, int imageId, String description)
    {
        this(tableName,primaryKeyName);
        this.imageId = imageId;
        this.description = description;
    }

    public String getPrimaryKeyName() { return this.primaryKeyName; }
    public Integer getImageId() { return this.imageId; }
    public String getDescription() { return this.description; }
    public abstract CardType getCardType();

    public void setImageId(Integer imageId) { this.imageId = imageId; }
    public void setDescription(String description) { this.description = description; }
}
