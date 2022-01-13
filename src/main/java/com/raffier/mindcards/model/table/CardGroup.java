package com.raffier.mindcards.model.table;

import com.raffier.mindcards.util.CardType;

public class CardGroup extends TitledCardTable {

    //Database columns
    private final int cardGroupId;
    private int deckId;

    public CardGroup(int cardGroupId) {
        super("CardGroup", "cardGroupId");
        this.cardGroupId = cardGroupId;
    }

    public CardGroup(int cardGroupId, int deckId, String title, int imageId, String description) {
        this(cardGroupId);
        this.deckId = deckId;
        this.title = title;
        this.imageId = imageId;
        this.description = description;
    }

    public Integer getPrimaryKey() { return this.cardGroupId; }
    public int getCardGroupId() { return this.cardGroupId; }
    public int getDeckId() { return this.deckId; }
    public CardType getCardType() { return CardType.CARDGROUP; }
}
