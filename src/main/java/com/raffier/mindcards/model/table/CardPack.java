package com.raffier.mindcards.model.table;

import com.raffier.mindcards.model.AppDatabase;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class CardPack extends DatabaseTable {

    //Database columns
    private final int packId;
    private final int ownerId;

    private String title;
    private int imageId;
    private String description;

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
        try (PreparedStatement statement = database.getConnection().prepareStatement("UPDATE CardPack SET title=? WHERE packId=?")) {
                statement.setInt(2, packId);
            statement.setString(1, newTitle);
            statement.executeUpdate();
            this.title = newTitle;
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    public void updateImage(int newImageId) {
        try (PreparedStatement statement = database.getConnection().prepareStatement("UPDATE CardPack SET imageId=? WHERE packId=?")) {
            statement.setInt(2, packId);
            statement.setInt(1, newImageId);
            statement.executeUpdate();
            this.imageId = newImageId;
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    public void updateDescription(String newDesc) {
        try (PreparedStatement statement = database.getConnection().prepareStatement("UPDATE CardPack SET description=? WHERE packId=?")) {
            statement.setInt(2, packId);
            statement.setString(1, newDesc);
            statement.executeUpdate();
            this.description = newDesc;
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }


    public List<Tag> getTags() {
        List<Tag> outList = new ArrayList<>();
        try {
            PreparedStatement statement = database.getConnection().prepareStatement("SELECT Tag.tagId FROM Tag, PackTag WHERE PackTag.packId=? AND Tag.tagId=PackTag.packId");
            statement.setInt(1,packId);
            ResultSet result = statement.executeQuery();
            while (result.next()) {
                int tagId = result.getInt("tagId");
                Tag tagObj = Tag.getTag(database,tagId);
                if (tagObj != null) outList.add(tagObj);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return outList;
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
