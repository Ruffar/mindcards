package com.raffier.mindcards.model.table;

import com.raffier.mindcards.util.CardType;

public class Infocard extends CardTable {

    //Database columns
    private int mindcardId;

    public Infocard(int infocardId) {
        super("Infocard", "infocardId");
        this.primaryId = infocardId;
    }

    public Infocard(int infocardId, int mindcardId, int imageId, String description) {
        this(infocardId);
        this.mindcardId = mindcardId;
        this.imageId = imageId;
        this.description = description;
    }

    public int getInfocardId() { return this.primaryId; }
    public int getMindcardId() { return this.mindcardId; }
    public CardType getCardType() { return CardType.INFOCARD; }

}
