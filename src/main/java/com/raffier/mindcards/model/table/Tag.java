package com.raffier.mindcards.model.table;

import com.raffier.mindcards.model.AppDatabase;

import java.sql.*;

public class Tag extends DatabaseTable {

    //Database columns
    private final int tagId;
    private final String tagName;

    private Tag(AppDatabase database, int tagId, ResultSet rawData) throws SQLException {
        super(database, "Image");

        this.tagId = tagId;
        this.tagName = rawData.getString("tagName");
    }

    public String getTagName() { return tagName; }

    public void delete() {
        try (PreparedStatement stmnt = database.getConnection().prepareStatement("DELETE FROM Tag WHERE tagId=?")) {
            stmnt.setInt(1,tagId);
            stmnt.executeUpdate();
            System.out.println("Tag with ID "+tagId+" successfully deleted.");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static Tag addTag(AppDatabase database, String tagName) {
        try (PreparedStatement stmnt = database.getConnection().prepareStatement("INSERT INTO Tag (tagName) VALUES (?)")) {
            stmnt.setString(1,tagName);
            stmnt.executeUpdate();
            ResultSet generatedIds = stmnt.getGeneratedKeys();
            if (generatedIds.next()) {
                int newId = generatedIds.getInt(1);
                System.out.println("Tag with ID "+newId+" successfully created.");
                return getTag(database, newId);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static Tag getTag(AppDatabase database, int tagId) {

        try (PreparedStatement stmnt = database.getConnection().prepareStatement("SELECT tagName FROM Tag WHERE tagId=?")) {
            stmnt.setInt(1,tagId);
            ResultSet results = stmnt.executeQuery();
            if (results.next()) {
                return new Tag(database, tagId, results);
            } else {
                System.out.println("Tag with ID "+tagId+" cannot be found.");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return null;

    }

}
