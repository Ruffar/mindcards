package com.raffier.mindcards.model.table;

public abstract class CardTable extends EntityTable<Integer> {

    //Database columns
    protected int primaryId;

    protected Integer imageId;
    protected String description;

    //Other data
    protected final String primaryKeyName;

    protected CardTable(String tableName, String primaryKeyName) {
        super(tableName);
        this.primaryKeyName = primaryKeyName;
    }

    protected CardTable(String tableName, String primaryKeyName, int imageId, String description) {
        this(tableName,primaryKeyName);
        this.imageId = imageId;
        this.description = description;
    }

    public String getPrimaryKeyName() { return this.primaryKeyName; }
    public Integer getPrimaryKey() { return this.primaryId; }
    public int getImageId() { return this.imageId; }
    public String getDescription() { return this.description; }

    public void setPrimaryKey(Integer primaryKey) { this.primaryId = primaryKey; }
    public void setImageId(int imageId) { this.imageId = imageId; }
    public void setDescription(String description) { this.description = description; }

}
