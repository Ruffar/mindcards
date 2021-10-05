package com.raffier.mindcards.model.table;

import com.raffier.mindcards.repository.AppDatabase;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class Infocard extends CardTable {

    //Database columns
    private int mindcardId;

    public Infocard(int infocardId) {
        super("Infocard", "infocardId");
        this.primaryId = infocardId;
    }

    public Infocard(int infocardId, int mindcardId, int imageId, String description) {
        this(infocardId);
        this.mindcardId = mindcardId;
        this.imageId = imageId;
        this.description = description;
    }

    public int getInfocardId() { return this.primaryId; }
    public int getMindcardId() { return this.mindcardId; }

    public void setInfocardId(int infocardId) { this.primaryId = infocardId; }
    public void setMindcardId(int mindcardId) { this.mindcardId = mindcardId; }

}
