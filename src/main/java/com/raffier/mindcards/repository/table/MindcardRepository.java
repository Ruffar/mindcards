package com.raffier.mindcards.repository.table;

import com.raffier.mindcards.errorHandling.EntityNotFoundException;
import com.raffier.mindcards.model.table.Image;
import com.raffier.mindcards.model.table.Infocard;
import com.raffier.mindcards.model.table.Mindcard;
import com.raffier.mindcards.repository.AppDatabase;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.SQLType;
import java.util.ArrayList;
import java.util.List;

@Component
public class MindcardRepository extends EntityRepository<Mindcard,Integer> {

    @Autowired
    public MindcardRepository(AppDatabase database) {
        super(database);
    }

    protected void throwEntityNotFound(Integer id) { throw new EntityNotFoundException("Mindcard", id); }

    public <S extends Mindcard> void save(S entity) {
        executeUpdate(
                "UPDATE Mindcard SET deckId=?,title=?,imageId=?,description=? WHERE mindcardId=?",
                (stmnt) -> {
                    stmnt.setInt(1, entity.getDeckId());
                    stmnt.setString(2, entity.getTitle());
                    stmnt.setInt(3, entity.getImageId());
                    stmnt.setString(4, entity.getDescription());
                    stmnt.setInt(5, entity.getMindcardId());
                });
    }

    public Mindcard getById(Integer id) {
        return executeQuery(
                "SELECT * FROM Mindcard WHERE mindcardId=?",
                (stmnt) -> stmnt.setInt(1,id),

                (results) -> {
                    if (results.next()) {
                        return new Mindcard(id, results.getInt("deckId"), results.getString("title"), results.getInt("imageId"), results.getString("description"));
                    }
                    throwEntityNotFound(id);
                    return null;
                });
    }

    public List<Mindcard> getRandomFromDeck(int deckId, int amount) {
        return executeQuery(
                "SELECT Mindcard.* FROM Mindcard, Deck WHERE Mindcard.deckId = ? ORDER BY RANDOM() LIMIT ?",
                (stmnt) -> {
                    stmnt.setInt(1, deckId);
                    stmnt.setInt(2, amount);
                },

                (results) -> {
                    List<Mindcard> outList = new ArrayList<>();
                    while (results.next()) {
                        outList.add(new Mindcard(results.getInt("mindcardId"), results.getInt("deckId"), results.getString("title"), results.getInt("imageId"), results.getString("description")));
                    }
                    return outList;
                });
    }

    public <S extends Mindcard> Mindcard add(S entity) {
        int newId = executeUpdate(
                "INSERT INTO Mindcard (deckId, title, imageId, description) VALUES (?,?,?,?)",
                (stmnt) -> {
                    stmnt.setInt(1, entity.getDeckId());
                    stmnt.setString(2, entity.getTitle());
                    stmnt.setInt(3, entity.getImageId());
                    stmnt.setString(4, entity.getDescription());
                });
        System.out.println("Mindcard with ID "+newId+" successfully created.");
        return getById(newId);
    }

    public <S extends Mindcard> void delete(S entity) {
        deleteById(entity.getMindcardId());
    }

    public void deleteById(Integer id) {
        executeUpdate(
                "DELETE FROM Mindcard WHERE mindcardId=?",
                (stmnt) -> stmnt.setInt(1,id));
        System.out.println("Mindcard with ID "+id+" successfully deleted.");
    }

}
