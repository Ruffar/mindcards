package com.raffier.mindcards.model.table;

import com.raffier.mindcards.repository.AppDatabase;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class CardGroup extends EntityTable {

    //Database columns
    private int cardGroupId;
    private int packId;

    private String title;
    private int imageId;
    private String description;

    public CardGroup(int cardGroupId) {
        super("CardGroup");
        this.cardGroupId = cardGroupId;
    }

    public CardGroup(int cardGroupId, int packId, String title, int imageId, String description) {
        this(cardGroupId);
        this.packId = packId;
        this.title = title;
        this.imageId = imageId;
        this.description = description;
    }

    public int getCardGroupId() { return cardGroupId; }
    public int getPackId() { return this.packId; }
    public String getTitle() { return this.title; }
    public int getImageId() { return this.imageId; }
    public String getDescription() { return this.description; }

    public void setCardGroupId(int cardGroupId) { this.cardGroupId = cardGroupId; }
    public void setPackId(int packId) { this.packId = packId; }

}
