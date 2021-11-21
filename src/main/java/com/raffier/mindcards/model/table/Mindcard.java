package com.raffier.mindcards.model.table;

import com.raffier.mindcards.util.CardType;

public class Mindcard extends TitledCardTable {

    //Database columns
    private int deckId;

    public Mindcard(int mindcardId) {
        super("Mindcard", "mindcardId");
        this.primaryId = mindcardId;
    }

    public Mindcard(int mindcardId, int deckId, String title, int imageId, String description) {
        this(mindcardId);
        this.deckId = deckId;
        this.title = title;
        this.description = description;
        this.imageId = imageId;
    }

    public int getMindcardId() { return this.primaryId; }
    public int getDeckId() { return this.deckId; }
    public CardType getCardType() { return CardType.MINDCARD; }

    public void setDeckId(int packId) { this.deckId = deckId; }

}
