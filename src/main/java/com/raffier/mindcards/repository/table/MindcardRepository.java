package com.raffier.mindcards.repository.table;

import com.raffier.mindcards.errorHandling.EntityNotFoundException;
import com.raffier.mindcards.model.table.*;
import com.raffier.mindcards.repository.AppDatabase;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

@Component
public class MindcardRepository extends CardRepository<Mindcard> {

    @Autowired
    public MindcardRepository(AppDatabase database) {
        super(database);
    }

    protected void throwEntityNotFound(Integer id) { throw new EntityNotFoundException("Mindcard", id); }

    // Updates //
    public void save(Mindcard entity) throws SQLException {
        getById(entity.getPrimaryKey());
        executeUpdate(
                "UPDATE Mindcard SET deckId=?,title=?,imageId=?,description=? WHERE mindcardId=?",
                (stmnt) -> {
                    stmnt.setInt(1, entity.getDeckId());
                    stmnt.setString(2, entity.getTitle());
                    stmnt.setObject(3, entity.getImageId());
                    stmnt.setString(4, entity.getDescription());
                    stmnt.setInt(5, entity.getMindcardId());
                });
    }

    public Mindcard add(Mindcard entity) throws SQLException {
        int newId = executeUpdate(
                "INSERT INTO Mindcard (deckId, title, imageId, description) VALUES (?,?,?,?)",
                (stmnt) -> {
                    stmnt.setInt(1, entity.getDeckId());
                    stmnt.setString(2, entity.getTitle());
                    stmnt.setObject(3, entity.getImageId());
                    stmnt.setString(4, entity.getDescription());
                });
        System.out.println("Mindcard with ID "+newId+" successfully created.");
        return getById(newId);
    }

    public void deleteById(Integer id) throws SQLException {
        getById(id);
        executeUpdate(
                "DELETE FROM Mindcard WHERE mindcardId=?",
                (stmnt) -> stmnt.setInt(1,id));
        System.out.println("Mindcard with ID "+id+" successfully deleted.");
    }

    // Queries //
    public Mindcard getById(Integer id) throws SQLException {
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

    public boolean isOwner(User user, int cardId) throws SQLException {
        return executeQuery(
                "SELECT Deck.* FROM Deck, Mindcard WHERE mindcardId=? AND Mindcard.deckId=Deck.deckId AND ownerId=?",
                (stmnt) -> {
                    stmnt.setInt(1,cardId);
                    stmnt.setInt(2,user.getUserId());
                },
                (ResultSet::next) //Return true if result is found
        );
    }

    public List<Mindcard> getRandomFromDeck(int deckId, int amount) throws SQLException {
        return executeQuery(
                "SELECT * FROM Mindcard WHERE Mindcard.deckId = ? ORDER BY RANDOM() LIMIT ?",
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

    public List<Mindcard> getFromCardGroup(int cardGroupId) throws SQLException {
        return executeQuery(
                "SELECT Mindcard.* FROM Mindcard, GroupMindcard WHERE GroupMindcard.cardGroupId = ? AND GroupMindcard.mindcardId=Mindcard.mindcardId",
                (stmnt) -> stmnt.setInt(1, cardGroupId),

                (results) -> {
                    List<Mindcard> outList = new ArrayList<>();
                    while (results.next()) {
                        outList.add(new Mindcard(results.getInt("mindcardId"), results.getInt("deckId"), results.getString("title"), results.getInt("imageId"), results.getString("description")));
                    }
                    return outList;
                });
    }

    public List<Mindcard> search(int deckId, String searchString, int amount, int offset) throws SQLException {
        return executeQuery(
                "SELECT m1.*, " +
                        "(SELECT COUNT(m2.mindcardId) FROM Mindcard m2 WHERE m2.mindcardId = m1.mindcardId AND (m2.title LIKE ? OR m2.description LIKE ?)) AS mindcardScore, " + //mindcardScore = 1 if title or description of a mindcard matches
                        "(SELECT COUNT(Infocard.infocardId) FROM Infocard WHERE Infocard.mindcardId = m1.mindcardId AND Infocard.description LIKE ?) AS infocardScore " +
                        "FROM Mindcard m1 " +
                        "WHERE m1.deckId = ? " +
                        "ORDER BY mindcardScore DESC, infocardScore DESC LIMIT ? OFFSET ?;",

                (stmnt) -> {
                    for (int i = 1; i <= 3; i++) {
                        stmnt.setString(i, "%"+searchString+"%"); //Set parameters 1 to 3 inclusive as searchString
                    }
                    stmnt.setInt(4,deckId);
                    stmnt.setInt(5,amount);
                    stmnt.setInt(6,offset);
                },

                (results) -> {
                    List<Mindcard> outList = new ArrayList<>();
                    while (results.next()) {
                        outList.add( new Mindcard(results.getInt("mindcardId"),results.getInt("deckId"),results.getString("title"),results.getInt("imageId"),results.getString("description")) );
                    }
                    return outList;
                });
    }
}
