package com.raffier.mindcards.data.table;

import com.raffier.mindcards.data.AppDatabase;

import java.io.Serializable;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class Mindcard extends DatabaseTable {

    //Database columns
    private final int mindcardId;
    private final int packId;

    private String title;
    private int imageId;
    private String description;

    //Prepared statements
    private PreparedStatement titleStatement;
    private PreparedStatement imageStatement;
    private PreparedStatement descStatement;

    private Mindcard(AppDatabase database, int mindcardId, ResultSet rawData) throws SQLException {
        super(database,"Mindcard");

        this.mindcardId = mindcardId;
        this.packId = rawData.getInt("packId");

        this.title = rawData.getString("title");
        this.imageId = rawData.getInt("imageId");
        this.description = rawData.getString("description");

    }

    public int getMindcardId() { return this.mindcardId; }
    public int getPackId() { return this.packId; }
    public String getTitle() { return this.title; }
    public int getImageId() { return this.imageId; }
    public String getDescription() { return this.description; }

    public void updateTitle(String newTitle) {
        try {
            if (titleStatement == null) {
                titleStatement = database.getConnection().prepareStatement("UPDATE Mindcard SET title=? WHERE mindcardId=?");
                titleStatement.setInt(2, mindcardId);
            }
            titleStatement.setString(1, newTitle);
            titleStatement.executeUpdate();
            this.title = newTitle;
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    public void updateImage(int newImageId) {
        try {
            if (imageStatement == null) {
                imageStatement = database.getConnection().prepareStatement("UPDATE Mindcard SET imageId=? WHERE mindcardId=?");
                imageStatement.setInt(2, mindcardId);
            }
            imageStatement.setInt(1, newImageId);
            imageStatement.executeUpdate();
            this.imageId = newImageId;
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    public void updateDescription(String newDesc) {
        try {
            if (descStatement == null) {
                descStatement = database.getConnection().prepareStatement("UPDATE Mindcard SET description=? WHERE mindcardId=?");
                descStatement.setInt(2, mindcardId);
            }
            descStatement.setString(1, newDesc);
            descStatement.executeUpdate();
            this.description = newDesc;
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }


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
                System.out.println("Mindcard with ID "+mindcardId+" cannot be found.");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

}
