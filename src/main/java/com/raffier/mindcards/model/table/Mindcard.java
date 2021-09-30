package com.raffier.mindcards.model.table;

import com.raffier.mindcards.repository.AppDatabase;
import com.raffier.mindcards.service.RepositoryService;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class Mindcard extends DatabaseTable {

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

    public Mindcard(int mindcardId, CardPack pack, String title, Image image, String description) {
        this(mindcardId);
        this.packId = pack.getPackId();
        this.title = title;
        this.description = description;
        this.imageId = image.getImageId();
    }

    public Mindcard(int mindcardId, int packId, String title, int imageId, String description) {
        this(mindcardId);
        this.packId = packId;
        this.title = title;
        this.description = description;
        this.imageId = imageId;
    }

    public int getMindcardId() { return this.mindcardId; }
    public CardPack getPack() { return RepositoryService.getMindcardRepository(); }
    public String getTitle() { return this.title; }
    public Image getImage() { return this.image; }
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


    public void delete() {
        try (PreparedStatement stmnt = database.getConnection().prepareStatement("DELETE FROM Mindcard WHERE mindcardId=?")) {
            stmnt.setInt(1,mindcardId);
            stmnt.executeUpdate();
            System.out.println("Mindcard with ID "+mindcardId+" successfully deleted.");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static Mindcard addMindcard(AppDatabase database, int packId, String title) {
        try (PreparedStatement stmnt = database.getConnection().prepareStatement("INSERT INTO Mindcard (packId, title) VALUES (?,?)")) {
            stmnt.setInt(1,packId);
            stmnt.setString(2,title);
            stmnt.executeUpdate();
            ResultSet generatedIds = stmnt.getGeneratedKeys();
            if (generatedIds.next()) {
                int newId = generatedIds.getInt(1);
                System.out.println("Mindcard with ID "+newId+" successfully created.");
                return getMindcard(database, newId);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static Mindcard getMindcard(AppDatabase database, int mindcardId) {
        try (PreparedStatement stmnt = database.getConnection().prepareStatement("SELECT packId, title, imageId, description FROM Mindcard WHERE mindcardId=?")) {
            stmnt.setInt(1,mindcardId);
            ResultSet results = stmnt.executeQuery();
            if (results.next()) {
                return new Mindcard(database, mindcardId, results);
            } else {
                //throw new RuntimeException("Mindcard with ID "+mindcardId+" cannot be found...");
                System.out.println("Mindcard with ID "+mindcardId+" cannot be found.");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

}
