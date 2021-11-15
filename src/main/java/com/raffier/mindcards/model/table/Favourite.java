package com.raffier.mindcards.model.table;

public class Favourite extends EntityTable<Favourite> {

    private final int deckId;
    private final int userId;

    public Favourite(int deckId, int userId) {
        super("Favourite");
        this.deckId = deckId;
        this.userId = userId;
    }

    public Favourite getPrimaryKey() {
        return this;
    }

    public int getDeckId() { return this.deckId; }
    public int getUserId() { return this.userId; }

    public String toString() {
        return "Favourite{" +
                "deckId=" + deckId +
                ", userId=" + userId +
                '}';
    }
}
