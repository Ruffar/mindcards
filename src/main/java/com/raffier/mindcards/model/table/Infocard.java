package com.raffier.mindcards.model.table;

import com.raffier.mindcards.repository.AppDatabase;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class Infocard extends EntityTable {

    //Database columns
    private int infocardId;
    private int mindcardId;

    private int imageId;
    private String description;

    public Infocard(int infocardId) {
        super("Infocard");
        this.infocardId = infocardId;
    }

    public Infocard(int infocardId, int mindcardId, int imageId, String description) {
        this(infocardId);
        this.mindcardId = mindcardId;
        this.imageId = imageId;
        this.description = description;
    }

    public int getInfocardId() { return this.infocardId; }
    public int getMindcardId() { return this.mindcardId; }
    public int getImageId() { return this.imageId; }
    public String getDescription() { return this.description; }

    public void setInfocardId(int infocardId) { this.infocardId = infocardId; }
    public void setMindcardId(int mindcardId) { this.mindcardId = mindcardId; }
    public void setImageId(int imageId) { this.imageId = imageId; }
    public void setDescription(String description) { this.description = description; }

}
