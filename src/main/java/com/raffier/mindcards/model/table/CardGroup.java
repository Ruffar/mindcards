package com.raffier.mindcards.model.table;

import com.raffier.mindcards.repository.AppDatabase;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class CardGroup extends DatabaseTable {

    //Database columns
    private final int cardGroupId;
    private final int packId;

    private String title;
    private int imageId;
    private String description;

    private CardGroup(AppDatabase database, int cardGroupId, ResultSet rawData) throws SQLException {
        super(database,"Card Group");

        this.cardGroupId = cardGroupId;
        this.packId = rawData.getInt("packId");

        this.title = rawData.getString("title");
        this.imageId = rawData.getInt("imageId");
        this.description = rawData.getString("description");

    }

    public int getCardGroupId() { return cardGroupId; }
    public int getPackId() { return this.packId; }
    public String getTitle() { return this.title; }
    public int getImageId() { return this.imageId; }
    public String getDescription() { return this.description; }

    public void updateTitle(String newTitle) {
        try (PreparedStatement statement = database.getConnection().prepareStatement("UPDATE CardGroup SET title=? WHERE cardGroupId=?")) {
            statement.setInt(2, cardGroupId);
            statement.setString(1, newTitle);
            statement.executeUpdate();
            this.title = newTitle;
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    public void updateImage(int newImageId) {
        try (PreparedStatement statement = database.getConnection().prepareStatement("UPDATE CardGroup SET imageId=? WHERE cardGroupId=?")) {
            statement.setInt(2, cardGroupId);
            statement.setInt(1, newImageId);
            statement.executeUpdate();
            this.imageId = newImageId;
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    public void updateDescription(String newDesc) {
        try (PreparedStatement statement = database.getConnection().prepareStatement("UPDATE CardGroup SET description=? WHERE cardGroupId=?")) {
            statement.setInt(2, cardGroupId);
            statement.setString(1, newDesc);
            statement.executeUpdate();
            this.description = newDesc;
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }


    public void delete() {
        try (PreparedStatement stmnt = database.getConnection().prepareStatement("DELETE FROM CardGroup WHERE cardGroupId=?")) {
            stmnt.setInt(1,cardGroupId);
            stmnt.executeUpdate();
            System.out.println("Card Group with ID "+cardGroupId+" successfully deleted.");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static CardGroup addCardGroup(AppDatabase database, int packId, String title) {
        try (PreparedStatement stmnt = database.getConnection().prepareStatement("INSERT INTO CardGroup (packId, title) VALUES (?,?)")) {
            stmnt.setInt(1,packId);
            stmnt.setString(2,title);
            stmnt.executeUpdate();
            ResultSet generatedIds = stmnt.getGeneratedKeys();
            if (generatedIds.next()) {
                int newId = generatedIds.getInt(1);
                System.out.println("Card Group with ID "+newId+" successfully created.");
                return getCardGroup(database, newId);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static CardGroup getCardGroup(AppDatabase database, int cardGroupId) {
        try (PreparedStatement stmnt = database.getConnection().prepareStatement("SELECT packId, title, imageId, description FROM CardGroup WHERE cardGroupId=?")) {
            stmnt.setInt(1,cardGroupId);
            ResultSet results = stmnt.executeQuery();
            if (results.next()) {
                return new CardGroup(database, cardGroupId, results);
            } else {
                System.out.println("Card Group with ID "+cardGroupId+" cannot be found.");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }
}
