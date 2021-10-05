package com.raffier.mindcards.model.table;

public class CardPack extends TitledCardTable {

    //Database columns
    private int ownerId;

    public CardPack(int packId) {
        super("CardPack", "packId");
        this.primaryId = packId;
    }

    public CardPack(int packId, int ownerId, String title, int imageId, String description) {
        this(packId);
        this.ownerId = ownerId;
        this.title = title;
        this.imageId = imageId;
        this.description = description;
    }

    public int getPackId() { return this.primaryId; }
    public int getOwnerId() { return ownerId; }

    public void setPackId(int packId) { this.primaryId = packId; }
    public void setOwnerId(int ownerId) { this.ownerId = ownerId; }

}
