package com.raffier.mindcards.model.table;

public class CardGroup extends TitledCardTable {

    //Database columns
    private int packId;

    public CardGroup(int cardGroupId) {
        super("CardGroup", "cardGroupId");
        this.primaryId = cardGroupId;
    }

    public CardGroup(int cardGroupId, int packId, String title, int imageId, String description) {
        this(cardGroupId);
        this.packId = packId;
        this.title = title;
        this.imageId = imageId;
        this.description = description;
    }

    public int getCardGroupId() { return this.primaryId; }
    public int getPackId() { return this.packId; }

    public void setCardGroupId(int cardGroupId) { this.primaryId = cardGroupId; }
    public void setPackId(int packId) { this.packId = packId; }

}
