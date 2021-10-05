package com.raffier.mindcards.model.table;

public class Mindcard extends TitledCardTable {

    //Database columns
    private int packId;

    public Mindcard(int mindcardId) {
        super("Mindcard", "mindcardId");
        this.primaryId = mindcardId;
    }

    public Mindcard(int mindcardId, int packId, String title, int imageId, String description) {
        this(mindcardId);
        this.packId = packId;
        this.title = title;
        this.description = description;
        this.imageId = imageId;
    }

    public int getMindcardId() { return this.primaryId; }
    public int getPackId() { return this.packId; }

    public void setMindcardId(int mindcardId) { this.primaryId = mindcardId; }
    public void setPackId(int packId) { this.packId = packId; }

}
