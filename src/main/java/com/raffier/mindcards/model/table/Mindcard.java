package com.raffier.mindcards.model.table;

import com.raffier.mindcards.util.CardType;

public class Mindcard extends TitledCardTable {

    //Database columns
    private final int mindcardId;
    private int deckId;

    public Mindcard(int mindcardId) {
        super("Mindcard", "mindcardId");
        this.mindcardId = mindcardId;
    }

    public Mindcard(int mindcardId, int deckId, String title, int imageId, String description) {
        this(mindcardId);
        this.deckId = deckId;
        this.title = title;
        this.description = description;
        this.imageId = imageId;
    }

    public Integer getPrimaryKey() { return this.mindcardId; }
    public int getMindcardId() { return this.mindcardId; }
    public int getDeckId() { return this.deckId; }
    public CardType getCardType() { return CardType.MINDCARD; }

}
