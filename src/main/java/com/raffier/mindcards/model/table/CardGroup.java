package com.raffier.mindcards.model.table;

import com.raffier.mindcards.service.CardType;

public class CardGroup extends TitledCardTable {

    //Database columns
    private int deckId;

    public CardGroup(int cardGroupId) {
        super("CardGroup", "cardGroupId");
        this.primaryId = cardGroupId;
    }

    public CardGroup(int cardGroupId, int deckId, String title, int imageId, String description) {
        this(cardGroupId);
        this.deckId = deckId;
        this.title = title;
        this.imageId = imageId;
        this.description = description;
    }

    public int getCardGroupId() { return this.primaryId; }
    public int getDeckId() { return this.deckId; }
    public CardType getCardType() { return CardType.CARDGROUP; }

    public void setCardGroupId(int cardGroupId) { this.primaryId = cardGroupId; }
    public void setDeckId(int deckId) { this.deckId = deckId; }

}
