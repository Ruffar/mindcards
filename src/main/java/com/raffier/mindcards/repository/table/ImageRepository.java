package com.raffier.mindcards.repository.table;

import com.raffier.mindcards.errorHandling.EntityNotFoundException;
import com.raffier.mindcards.model.table.CardTable;
import com.raffier.mindcards.model.table.Image;
import com.raffier.mindcards.repository.AppDatabase;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.sql.SQLException;

@Component
public class ImageRepository extends EntityRepository<Image, Integer> {

    @Autowired
    public ImageRepository(AppDatabase database) {
        super(database);
    }

    protected void throwEntityNotFound(Integer id) { throw new EntityNotFoundException("Image", id); }

    // Updates //
    public void save(Image entity) throws SQLException {
        getById(entity.getPrimaryKey());
        executeUpdate(
                "UPDATE Image SET imagePath=? WHERE imageId=?",
                (stmnt) -> {
                    stmnt.setString(1, entity.getImagePath());
                    stmnt.setInt(2, entity.getImageId());
                });
    }

    public Image add(Image entity) throws SQLException {
        int newId = executeUpdate(
                "INSERT INTO Image (imagePath) VALUES (?)",
                (stmnt) -> stmnt.setString(1, entity.getImagePath()));
        System.out.println("Image with ID "+newId+" successfully created.");
        return getById(newId);
    }

    public void deleteById(Integer id) throws SQLException {
        getById(id);
        executeUpdate(
                "DELETE FROM Image WHERE imageId=?",
                (stmnt) -> stmnt.setInt(1,id));
        System.out.println("Image with ID "+id+" successfully deleted.");
    }

    // Queries //
    public Image getById(Integer id) throws SQLException {
        return executeQuery(
                "SELECT * FROM Image WHERE imageId=?",
                (stmnt) -> stmnt.setInt(1,id),

                (results) -> {
                    if (results.next()) {
                        return new Image(id, results.getString("imagePath"));
                    }
                    throwEntityNotFound(id);
                    return null;
                });
    }

    public <S extends CardTable> Image getFromCard(S card) throws SQLException {
        return executeQuery(
                "SELECT Image.imageId, Image.imagePath FROM "+card.getTableName()+", Image WHERE "+card.getPrimaryKeyName()+" = ? AND "+card.getTableName()+".imageId = Image.imageId",
                (stmnt) -> stmnt.setInt(1,card.getPrimaryKey()),

                (results) -> {
                    if (results.next()) {
                        return new Image(results.getInt("imageId"), results.getString("imagePath"));
                    }
                    throwEntityNotFound(card.getImageId());
                    return null;
                });
    }
}
