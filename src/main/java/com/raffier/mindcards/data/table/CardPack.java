package com.raffier.mindcards.data.table;

import com.raffier.mindcards.data.AppDatabase;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class CardPack extends DatabaseTable {

    //Database columns
    private final int packId;
    private final int ownerId;

    private String title;
    private int imageId;
    private String description;

    //Prepared statements
    private PreparedStatement titleStatement;
    private PreparedStatement imageStatement;
    private PreparedStatement descStatement;

    private CardPack(AppDatabase database, int packId, ResultSet rawData) throws SQLException {
        super(database,"CardPack");

        this.packId = packId;
        this.ownerId = rawData.getInt("ownerId");

        this.title = rawData.getString("title");
        this.imageId = rawData.getInt("imageId");
        this.description = rawData.getString("description");

    }

    public int getPackId() { return this.packId; }
    public int getOwnerId() { return ownerId; }
    public String getTitle() { return this.title; }
    public int getImageId() { return this.imageId; }
    public String getDescription() { return this.description; }

    public void updateTitle(String newTitle) {
        try {
            if (titleStatement == null) {
                titleStatement = database.getConnection().prepareStatement("UPDATE CardPack SET title=? WHERE packId=?");
                titleStatement.setInt(2, packId);
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
                imageStatement = database.getConnection().prepareStatement("UPDATE CardPack SET imageId=? WHERE packId=?");
                imageStatement.setInt(2, packId);
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
                descStatement = database.getConnection().prepareStatement("UPDATE CardPack SET description=? WHERE packId=?");
                descStatement.setInt(2, packId);
            }
            descStatement.setString(1, newDesc);
            descStatement.executeUpdate();
            this.description = newDesc;
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }


    public void delete() {
        try (PreparedStatement stmnt = database.getConnection().prepareStatement("DELETE FROM CardPack WHERE packId=?")) {
            stmnt.setInt(1,packId);
            stmnt.executeUpdate();
            System.out.println("Card Pack with ID "+packId+" successfully deleted.");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static CardPack cardPack(AppDatabase database, int ownerId, String title) {
        try (PreparedStatement stmnt = database.getConnection().prepareStatement("INSERT INTO CardPack (ownerId, title) VALUES (?,?)")) {
            stmnt.setInt(1,ownerId);
            stmnt.setString(2,title);
            stmnt.executeUpdate();
            ResultSet generatedIds = stmnt.getGeneratedKeys();
            if (generatedIds.next()) {
                int newId = generatedIds.getInt(1);
                System.out.println("Card Pack with ID "+newId+" successfully created.");
                return getCardPack(database, newId);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static CardPack getCardPack(AppDatabase database, int cardPackId) {
        try (PreparedStatement stmnt = database.getConnection().prepareStatement("SELECT ownerId, title, imageId, description FROM CardPack WHERE packId=?")) {
            stmnt.setInt(1,cardPackId);
            ResultSet results = stmnt.executeQuery();
            if (results.next()) {
                return new CardPack(database, cardPackId, results);
            } else {
                System.out.println("Card Pack with ID "+cardPackId+" cannot be found.");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

}
