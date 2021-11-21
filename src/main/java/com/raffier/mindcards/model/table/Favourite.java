package com.raffier.mindcards.model.table;

import java.sql.Date;
import java.time.Instant;

public class Favourite extends EntityTable<Favourite> {

    private final int deckId;
    private final int userId;
    private Date lastViewed;

    public Favourite(int deckId, int userId) {
        super("Favourite");
        this.deckId = deckId;
        this.userId = userId;
        this.lastViewed = new Date(Instant.now().getEpochSecond());
    }

    public Favourite(int deckId, int userId, Date lastViewed) {
        this(deckId, userId);
        this.lastViewed = lastViewed;
    }

    public Favourite getPrimaryKey() {
        return this;
    }

    public int getDeckId() { return this.deckId; }
    public int getUserId() { return this.userId; }
    public Date getLastViewed() { return this.lastViewed; }

    public String toString() {
        return "Favourite{" +
                "deckId=" + deckId +
                ", userId=" + userId +
                '}';
    }
}
