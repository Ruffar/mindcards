package com.raffier.mindcards.repository.table;

import com.raffier.mindcards.model.table.Image;
import com.raffier.mindcards.model.table.Infocard;
import com.raffier.mindcards.repository.AppDatabase;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class ImageRepository extends EntityRepository<Image, Integer> {

    public ImageRepository(AppDatabase database) {
        super(database);
    }

    public <S extends Image> void save(S entity) {
        try (PreparedStatement statement = database.getConnection().prepareStatement("UPDATE Image SET imagePath=? WHERE imageId=?")) {
            statement.setString(1, entity.getImagePath());
            statement.setInt(2,entity.getImageId());
            statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public Image getById(Integer id) {
        try (PreparedStatement stmnt = database.getConnection().prepareStatement("SELECT imagePath FROM Image WHERE imageId=?")) {
            stmnt.setInt(1,id);
            ResultSet results = stmnt.executeQuery();
            if (results.next()) {
                return new Image(id,results.getString("imagePath"));
            } else {
                System.out.println("Image with ID "+id+" cannot be found.");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return null;
    }

    public <S extends Image> Image add(S entity) {
        try (PreparedStatement stmnt = database.getConnection().prepareStatement("INSERT INTO Image (imagePath) VALUES (?)")) {
            stmnt.setString(1,entity.getImagePath());
            stmnt.executeUpdate();
            ResultSet generatedIds = stmnt.getGeneratedKeys();
            if (generatedIds.next()) {
                int newId = generatedIds.getInt(1);
                System.out.println("Image with ID "+newId+" successfully created.");
                return getById(newId);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public <S extends Image> void delete(S entity) {
        deleteById(entity.getImageId());
    }

    public void deleteById(Integer id) {
        try (PreparedStatement stmnt = database.getConnection().prepareStatement("DELETE FROM Image WHERE imageId=?")) {
            stmnt.setInt(1,id);
            stmnt.executeUpdate();
            System.out.println("Image with ID "+id+" successfully deleted.");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

}
