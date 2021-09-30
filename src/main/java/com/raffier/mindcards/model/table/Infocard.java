package com.raffier.mindcards.model.table;

import com.raffier.mindcards.repository.AppDatabase;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class Infocard extends DatabaseTable {

    //Database columns
    private final int infocardId;
    private final int mindcardId;

    private Image image;
    private String description;

    private Infocard(AppDatabase database, int infocardId, ResultSet rawData) throws SQLException {
        super(database,"Infocard");

        this.infocardId = infocardId;
        this.mindcardId = rawData.getInt("mindcardId");

        this.description = rawData.getString("description");

        int imageId = rawData.getInt("imageId");
        if (imageId != 0) this.image = Image.getImage(database,imageId);

    }

    public int getInfocardId() { return this.infocardId; }
    public int getMindcardId() { return this.mindcardId; }
    public Image getImage() { return this.image; }
    public String getDescription() { return this.description; }

    public void updateImage(int newImageId) {
        try (PreparedStatement statement = database.getConnection().prepareStatement("UPDATE Infocard SET imageId=? WHERE infocardId=?")) {
            statement.setInt(2, infocardId);
            statement.setInt(1, newImageId);
            statement.executeUpdate();

            if (newImageId == 0) this.image = null;
            else this.image = Image.getImage(database,newImageId);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    public void updateDescription(String newDesc) {
        try (PreparedStatement statement = database.getConnection().prepareStatement("UPDATE Infocard SET description=? WHERE infocardId=?")) {
            statement.setInt(2, infocardId);
            statement.setString(1, newDesc);
            statement.executeUpdate();
            this.description = newDesc;
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }


    public void delete() {
        try (PreparedStatement stmnt = database.getConnection().prepareStatement("DELETE FROM Infocard WHERE infocardId=?")) {
            stmnt.setInt(1,infocardId);
            stmnt.executeUpdate();
            System.out.println("Infocard with ID "+infocardId+" successfully deleted.");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static Infocard addInfocard(AppDatabase database, int mindcardId) {
        try (PreparedStatement stmnt = database.getConnection().prepareStatement("INSERT INTO Infocard (mindcardId) VALUES (?)")) {
            stmnt.setInt(1,mindcardId);
            stmnt.executeUpdate();
            ResultSet generatedIds = stmnt.getGeneratedKeys();
            if (generatedIds.next()) {
                int newId = generatedIds.getInt(1);
                System.out.println("Infocard with ID "+newId+" successfully created.");
                return getInfocard(database, newId);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static Infocard getInfocard(AppDatabase database, int infocardId) {

        try (PreparedStatement stmnt = database.getConnection().prepareStatement("SELECT mindcardId, imageId, description FROM Infocard WHERE infocardId=?")) {
            stmnt.setInt(1,infocardId);
            ResultSet results = stmnt.executeQuery();
            if (results.next()) {
                return new Infocard(database, infocardId, results);
            } else {
                System.out.println("Infocard with ID "+infocardId+" cannot be found.");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return null;

    }
}
