package com.raffier.mindcards.model.table;

import com.raffier.mindcards.model.AppDatabase;

import java.sql.*;

public class Image extends DatabaseTable {

    //Database columns
    private final int imageId;
    private String imagePath;

    private Image(AppDatabase database, int imageId, ResultSet rawData) throws SQLException {
        super(database, "Image");

        this.imageId = imageId;
        this.imagePath = rawData.getString("imagePath");
    }

    public String getImagePath() { return imagePath; }

    public void updatePath(String newPath) {
        try (PreparedStatement statement = database.getConnection().prepareStatement("UPDATE Image SET imagePath=? WHERE imageId=?")) {
            statement.setInt(2, imageId);
            statement.setString(1, newPath);
            statement.executeUpdate();
            this.imagePath = newPath;
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void delete() {
        try (PreparedStatement stmnt = database.getConnection().prepareStatement("DELETE FROM Image WHERE imageId=?")) {
            stmnt.setInt(1,imageId);
            stmnt.executeUpdate();
            System.out.println("Image with ID "+imageId+" successfully deleted.");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static Image addImage(AppDatabase database, String imagePath) {
        try (PreparedStatement stmnt = database.getConnection().prepareStatement("INSERT INTO Image (imagePath) VALUES (?)")) {
            stmnt.setString(1,imagePath);
            stmnt.executeUpdate();
            ResultSet generatedIds = stmnt.getGeneratedKeys();
            if (generatedIds.next()) {
                int newId = generatedIds.getInt(1);
                System.out.println("Image with ID "+newId+" successfully created.");
                return getImage(database, newId);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static Image getImage(AppDatabase database, int imageId) {

        try (PreparedStatement stmnt = database.getConnection().prepareStatement("SELECT imagePath FROM Image WHERE imageId=?")) {
            stmnt.setInt(1,imageId);
            ResultSet results = stmnt.executeQuery();
            if (results.next()) {
                return new Image(database, imageId, results);
            } else {
                System.out.println("Image with ID "+imageId+" cannot be found.");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return null;

    }

}
