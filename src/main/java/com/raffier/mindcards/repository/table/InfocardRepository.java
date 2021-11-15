package com.raffier.mindcards.repository.table;

import com.raffier.mindcards.errorHandling.EntityNotFoundException;
import com.raffier.mindcards.model.table.*;
import com.raffier.mindcards.repository.AppDatabase;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

@Component
public class InfocardRepository extends CardRepository<Infocard> {

    @Autowired
    public InfocardRepository(AppDatabase database) {
        super(database);
    }

    protected void throwEntityNotFound(Integer id) { throw new EntityNotFoundException("Infocard", id); }

    // Updates //
    public void save(Infocard entity) {
        executeUpdate(
                "UPDATE Infocard SET mindcardId=?,imageId=?,description=? WHERE infocardId=?",
                (stmnt) -> {
                    stmnt.setInt(1, entity.getMindcardId());
                    stmnt.setInt(2, entity.getImageId());
                    stmnt.setString(3, entity.getDescription());
                    stmnt.setInt(4, entity.getInfocardId());
                });
    }

    public Infocard add(Infocard entity) {
        int newId = executeUpdate(
                "INSERT INTO Infocard (mindcardId, imageId, description) VALUES (?,?,?)",
                (stmnt) -> {
                    stmnt.setInt(1, entity.getMindcardId());
                    stmnt.setInt(2, entity.getImageId());
                    stmnt.setString(3, entity.getDescription());
                });
        System.out.println("Infocard with ID "+newId+" successfully created.");
        return getById(newId);
    }

    public void delete(Infocard entity) {
        deleteById(entity.getInfocardId());
    }

    public void deleteById(Integer id) {
        executeUpdate(
                "DELETE FROM Infocard WHERE infocardId=?",
                (stmnt) -> stmnt.setInt(1,id));
        System.out.println("Infocard with ID "+id+" successfully deleted.");
    }

    // Queries //
    public Infocard getById(Integer id) {
        return executeQuery(
                "SELECT * FROM Infocard WHERE infocardId=?",
                (stmnt) -> stmnt.setInt(1,id),

                (results) -> {
                    if (results.next()) {
                        return new Infocard(id, results.getInt("mindcardId"), results.getInt("imageId"), results.getString("description"));
                    }
                    throwEntityNotFound(id);
                    return null;
                });
    }

    public List<Infocard> getFromMindcard(int mindcardId) {
        return executeQuery(
                "SELECT infocardId, imageId, description FROM Infocard WHERE mindcardId = ?",
                (stmnt) -> stmnt.setInt(1,mindcardId),

                (results) -> {
                    List<Infocard> outList = new ArrayList<>();
                    while (results.next()) {
                        outList.add(new Infocard(results.getInt("infocardId"), mindcardId, results.getInt("imageId"), results.getString("description")));
                    }
                    return outList;
                });
    }

    public boolean isOwner(User user, int cardId) {
        return executeQuery(
                "SELECT Deck.* FROM Deck, Mindcard, Infocard WHERE infocardId=? AND Infocard.mindcardId=Mindcard.mindcardId AND Mindcard.deckId=Deck.deckId AND ownerId=?",
                (stmnt) -> {
                    stmnt.setInt(1,cardId);
                    stmnt.setInt(2,user.getUserId());
                },
                (ResultSet::next)
        );
    }

}
