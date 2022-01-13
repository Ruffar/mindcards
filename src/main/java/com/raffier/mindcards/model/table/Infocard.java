package com.raffier.mindcards.model.table;

import com.raffier.mindcards.util.CardType;

public class Infocard extends CardTable {

    //Database columns
    private final int infocardId;
    private int mindcardId;

    public Infocard(int infocardId) {
        super("Infocard", "infocardId");
        this.infocardId = infocardId;
    }

    public Infocard(int infocardId, int mindcardId, int imageId, String description) {
        this(infocardId);
        this.mindcardId = mindcardId;
        this.imageId = imageId;
        this.description = description;
    }

    public Integer getPrimaryKey() { return this.infocardId; }
    public int getInfocardId() { return this.infocardId; }
    public int getMindcardId() { return this.mindcardId; }
    public CardType getCardType() { return CardType.INFOCARD; }
}
