package com.raffier.mindcards.model.table;

import com.raffier.mindcards.repository.AppDatabase;
import com.raffier.mindcards.service.RepositoryService;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class CardPack extends EntityTable {

    //Database columns
    private int packId;
    private int ownerId;

    private String title;
    private int imageId;
    private String description;

    public CardPack(int packId) {
        super("CardPack");
        this.packId = packId;
    }

    public CardPack(int packId, int ownerId, String title, int imageId, String description) {
        this(packId);
        this.ownerId = ownerId;
        this.title = title;
        this.imageId = imageId;
        this.description = description;
    }

    public int getPackId() { return this.packId; }
    public int getOwnerId() { return ownerId; }
    public String getTitle() { return this.title; }
    public int getImageId() { return this.imageId; }
    public String getDescription() { return this.description; }

}
