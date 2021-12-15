package com.raffier.mindcards.model.table;

import com.raffier.mindcards.util.CardType;

import java.sql.Date;
import java.time.Instant;

public class Deck extends TitledCardTable {

    //Database columns
    private final int deckId;
    private int ownerId;
    private boolean isPrivate;
    private Date timeCreated;

    public Deck(int deckId) {
        super("Deck", "deckId");
        this.deckId = deckId;
    }

    public Deck(int deckId, int ownerId, String title, int imageId, String description) {
        this(deckId);
        this.ownerId = ownerId;
        this.title = title;
        this.imageId = imageId;
        this.description = description;
        this.isPrivate = false; // isPrivate is false by default
        this.timeCreated = new Date(Instant.now().getEpochSecond()); //set the timeCreated to "right now" by default
    }

    public Deck(int deckId, int ownerId, String title, int imageId, String description, boolean isPrivate, Date timeCreated) {
        this(deckId,ownerId,title,imageId,description);
        this.isPrivate = isPrivate;
        this.timeCreated = timeCreated;
    }

    public Integer getPrimaryKey() {return this.deckId; }
    public int getDeckId() { return this.deckId; }
    public int getOwnerId() { return ownerId; }
    public boolean isPrivate() { return isPrivate; }
    public Date getTimeCreated() { return this.timeCreated; }
    public CardType getCardType() { return CardType.DECK; }

    public void setPrivate(boolean isPrivate) { this.isPrivate = isPrivate; }

}
