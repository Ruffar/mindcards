package com.raffier.mindcards.model.table;

import com.raffier.mindcards.service.CardType;

import java.sql.Date;

public class Deck extends TitledCardTable {

    //Database columns
    private int ownerId;
    private boolean isPrivate;
    private Date timeCreated;

    public Deck(int deckId) {
        super("Deck", "deckId");
        this.primaryId = deckId;
    }

    public Deck(int deckId, int ownerId, String title, int imageId, String description) {
        this(deckId);
        this.ownerId = ownerId;
        this.title = title;
        this.imageId = imageId;
        this.description = description;
        this.isPrivate = false; // isPrivate is false by default
    }

    public Deck(int deckId, int ownerId, String title, int imageId, String description, boolean isPrivate, Date timeCreated) {
        this(deckId);
        this.ownerId = ownerId;
        this.title = title;
        this.imageId = imageId;
        this.description = description;
        this.timeCreated = timeCreated;
    }

    public int getDeckId() { return this.primaryId; }
    public int getOwnerId() { return ownerId; }
    public boolean isPrivate() { return isPrivate; }
    public Date getTimeCreated() { return this.timeCreated; }
    public CardType getCardType() { return CardType.DECK; }

    public void setDeckId(int packId) { this.primaryId = packId; }
    public void setOwnerId(int ownerId) { this.ownerId = ownerId; }
    public void setPrivate(boolean isPrivate) { this.isPrivate = isPrivate; }

}
