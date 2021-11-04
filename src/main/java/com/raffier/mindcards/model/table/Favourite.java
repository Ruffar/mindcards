package com.raffier.mindcards.model.table;

public class Favourite extends EntityTable<Favourite> {

    private int deckId;
    private int userId;

    public Favourite(int deckId, int userId) {
        super("Favourite");
        this.deckId = deckId;
        this.userId = userId;
    }

    public Favourite getPrimaryKey() {
        return this;
    }
    public void setPrimaryKey(Favourite newId) {
        this.deckId = newId.deckId;
        this.userId = newId.userId;
    }

    public int getDeckId() { return this.deckId; }
    public int getUserId() { return this.userId; }

    public void setDeckId(int deckId) { this.deckId = deckId; }
    public void setUserId(int userId) { this.userId = userId; }

    public String toString() {
        return "Favourite{" +
                "deckId=" + deckId +
                ", userId=" + userId +
                '}';
    }
}
