package com.raffier.mindcards.model.table;

import com.raffier.mindcards.service.RepositoryService;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class Mindcard extends EntityTable {

    //Database columns
    private int mindcardId;
    private int packId;

    private String title;
    private int imageId;
    private String description;

    public Mindcard(int mindcardId) {
        super("Mindcard");
        this.mindcardId = mindcardId;
    }

    public Mindcard(int mindcardId, int packId, String title, int imageId, String description) {
        this(mindcardId);
        this.packId = packId;
        this.title = title;
        this.description = description;
        this.imageId = imageId;
    }

    public int getMindcardId() { return this.mindcardId; }
    public int getPackId() { return this.packId; }
    public CardPack getPack() { return RepositoryService.getCardPackRepository().getById(this.packId); }
    public String getTitle() { return this.title; }
    public int getImageId() { return this.imageId; }
    public Image getImage() { return return RepositoryService.getImageRepository().getById(this.imageId); }
    public String getDescription() { return this.description; }

    public void setMindcardId(int mindcardId) { this.mindcardId = mindcardId; }
    public void setPack(CardPack pack) { this.packId = pack.getPackId(); }
    public void setPackId(int packId) { this.packId = packId; }
    public void setTitle(String title) { this.title = title; }
    public void setImage(Image image) { this.imageId = image.getImageId(); }
    public void setImageId(int imageId) { this.imageId = imageId; }
    public void setDescription(String description) { this.description = description; }

    public List<Infocard> getInfocards() {
        List<Infocard> outList = new ArrayList<>();
        try {
            PreparedStatement statement = database.getConnection().prepareStatement("SELECT infocardId FROM Infocard WHERE mindcardId = ?");
            statement.setInt(1,mindcardId);
            ResultSet result = statement.executeQuery();
            while (result.next()) {
                int infoId = result.getInt("infocardId");
                Infocard infoObj = Infocard.getInfocard(database,infoId);
                if (infoObj != null) outList.add(infoObj);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return outList;
    }

}
