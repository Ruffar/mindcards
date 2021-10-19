package com.raffier.mindcards.repository.table;

import com.raffier.mindcards.errorHandling.EntityNotFoundException;
import com.raffier.mindcards.model.table.CardTable;
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

    private void throwEntityNotFound(Integer id) { throw new EntityNotFoundException("Image", id); }
    private void throwEntityNotFound(String path) { throw new EntityNotFoundException("Image", "path", "\""+path+"\""); }

    public <S extends Image> void save(S entity) {
        try (PreparedStatement statement = database.getConnection().prepareStatement("UPDATE Image SET name=?, imagePath=? WHERE imageId=?")) {
            statement.setString(1, entity.getName());
            statement.setString(2, entity.getImagePath());
            statement.setInt(2,entity.getImageId());
            statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public Image getById(Integer id) {
        try (PreparedStatement stmnt = database.getConnection().prepareStatement("SELECT name, imagePath FROM Image WHERE imageId=?")) {
            stmnt.setInt(1,id);
            ResultSet results = stmnt.executeQuery();
            if (results.next()) {
                return new Image(id,results.getString("name"),results.getString("imagePath"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        throwEntityNotFound(id);
        return null;
    }

    public Image getByPath(String path) {
        try (PreparedStatement stmnt = database.getConnection().prepareStatement("SELECT imageId, name, imagePath FROM Image WHERE imagePath=?")) {
            stmnt.setString(1,path);
            ResultSet results = stmnt.executeQuery();
            if (results.next()) {
                return new Image(results.getInt("imageId"),results.getString("name"),path);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        throwEntityNotFound(path);
        return null;
    }

    public <S extends CardTable> Image getFromCard(S card) {
        try {
            PreparedStatement statement = database.getConnection().prepareStatement("SELECT Image.imageId, Image.name, Image.imagePath FROM "+card.getTableName()+", Image WHERE "+card.getPrimaryKeyName()+" = ? AND "+card.getTableName()+".imageId = Image.imageId");
            statement.setInt(1,card.getPrimaryKey());
            ResultSet result = statement.executeQuery();
            if (result.next()) {
                return new Image(result.getInt("imageId"),result.getString("name"),result.getString("imagePath"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        throwEntityNotFound(card.getImageId());
        return null;
    }

    public <S extends Image> Image add(S entity) {
        try (PreparedStatement stmnt = database.getConnection().prepareStatement("INSERT INTO Image (name,imagePath) VALUES (?,?)")) {
            stmnt.setString(1,entity.getName());
            stmnt.setString(2,entity.getImagePath());
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
