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
public class DeckRepository extends CardRepository<Deck> {

    @Autowired
    public DeckRepository(AppDatabase database) {
        super(database);
    }

    protected void throwEntityNotFound(Integer id) { throw new EntityNotFoundException("Deck", id); }

    // Updates //
    public void save(Deck entity) throws SQLException {
        getById(entity.getPrimaryKey());
        executeUpdate(
                "UPDATE Deck SET ownerId=?, title=?, imageId=?, description=?, isPrivate=?, timeCreated=? WHERE deckId=?",
                (stmnt) -> {
                    stmnt.setInt(1,entity.getOwnerId());
                    stmnt.setString(2,entity.getTitle());
                    stmnt.setObject(3,entity.getImageId());
                    stmnt.setString(4,entity.getDescription());
                    stmnt.setBoolean(5,entity.isPrivate());
                    stmnt.setDate(6,entity.getTimeCreated());
                    stmnt.setInt(7,entity.getDeckId());
        });
    }

    public Deck add(Deck entity) throws SQLException {
        int newId = executeUpdate(
                "INSERT INTO Deck (ownerId, title, imageId, description, isPrivate, timeCreated) VALUES (?,?,?,?,?,?)",
                (stmnt) -> {
                    stmnt.setInt(1,entity.getOwnerId());
                    stmnt.setString(2,entity.getTitle());
                    stmnt.setObject(3,entity.getImageId());
                    stmnt.setString(4,entity.getDescription());
                    stmnt.setBoolean(5, entity.isPrivate());
                    stmnt.setDate(6, entity.getTimeCreated()); //Set the deck's creation time to current time
                });
        System.out.println("Deck with ID "+newId+" successfully created.");
        return getById(newId);
    }

    public void deleteById(Integer id) throws SQLException {
        getById(id);
        executeUpdate(
                "DELETE FROM Deck WHERE deckId=?",
                (stmnt) -> stmnt.setInt(1,id));
        System.out.println("Deck with ID "+id+" successfully deleted.");
    }

    // Queries //
    public Deck getById(Integer id) throws SQLException {
        return executeQuery(
                "SELECT * FROM Deck WHERE deckId=?",
                (stmnt) -> stmnt.setInt(1,id),

                (results) -> {
                    if (results.next()) {
                        return new Deck(id, results.getInt("ownerId"), results.getString("title"), results.getInt("imageId"), results.getString("description"), results.getBoolean("isPrivate"), results.getDate("timeCreated"));
                    }
                    throwEntityNotFound(id);
                    return null;
                });
    }

    public boolean isOwner(User user, int cardId) throws SQLException {
        return executeQuery(
                "SELECT Deck.* FROM Deck WHERE deckId=? AND ownerId=?",
                (stmnt) -> {
                    stmnt.setInt(1,cardId);
                    stmnt.setInt(2,user.getUserId());
                },
                (ResultSet::next) //Return true if result is found
        );
    }

    public List<Deck> search(String searchString, int amount, int offset) throws SQLException {
        return executeQuery(
                "SELECT d1.*, " +
                        "(SELECT COUNT(d2.deckId) FROM Deck d2 WHERE d1.deckId = d2.deckId AND (d2.title LIKE ? OR d2.description LIKE ?)) AS deckScore, " + //deckScore = 1 if title or description of a deck matches
                        "(SELECT COUNT(Mindcard.mindcardId) FROM Mindcard WHERE Mindcard.deckId = d1.deckId AND (Mindcard.title LIKE ? OR Mindcard.description LIKE ?)) AS mindcardScore, " + //mindcardScore
                        "(SELECT COUNT(CardGroup.cardGroupId) FROM CardGroup WHERE CardGroup.deckId = d1.deckId AND (CardGroup.title LIKE ? OR CardGroup.description LIKE ?)) AS groupScore, " +
                        "(SELECT COUNT(Infocard.infocardId) FROM Infocard, Mindcard WHERE Infocard.mindcardId = Mindcard.mindcardId AND Mindcard.deckId = d1.deckId AND Infocard.description LIKE ?) AS infocardScore " +
                        "FROM Deck d1 " +
                        "WHERE d1.deckId != 0 " +
                        "ORDER BY deckScore DESC, groupScore DESC, mindcardScore DESC, infocardScore DESC LIMIT ? OFFSET ?;",

                (stmnt) -> {
                    for (int i = 1; i <= 7; i++) {
                        stmnt.setString(i, "%"+searchString+"%"); //Set parameters 1 to 7 inclusive as searchString
                    }
                    stmnt.setInt(8,amount);
                    stmnt.setInt(9,offset);
                },

                (results) -> {
                    List<Deck> outList = new ArrayList<>();
                    while (results.next()) {
                        outList.add( new Deck(results.getInt("deckId"),results.getInt("ownerId"),results.getString("title"),results.getInt("imageId"),results.getString("description"),results.getBoolean("isPrivate"),results.getDate("timeCreated")) );
                    }
                    return outList;
                });
    }

    public List<Deck> getRandom(int amount) throws SQLException {
        return executeQuery(
                "SELECT * FROM Deck WHERE deckId != 0 ORDER BY RANDOM() LIMIT ?",
                (stmnt) -> {
                    stmnt.setInt(1,amount);
                },

                (results) -> {
                    List<Deck> outList = new ArrayList<>();
                    while (results.next()) {
                        outList.add( new Deck(results.getInt("deckId"),results.getInt("ownerId"),results.getString("title"),results.getInt("imageId"),results.getString("description"),results.getBoolean("isPrivate"),results.getDate("timeCreated")) );
                    }
                    return outList;
                });
    }

    public List<Deck> getPopular(int amount, int offset) throws SQLException {
        return executeQuery(
                "SELECT Deck.*, COUNT(Favourite.deckId) AS favCount FROM Deck, Favourite WHERE Deck.deckId != 0 AND Deck.deckId = Favourite.deckId GROUP BY Favourite.deckId ORDER BY favCount DESC LIMIT ? OFFSET ?",
                (stmnt) -> {
                    stmnt.setInt(1,amount);
                    stmnt.setInt(2,offset);
                },

                (results) -> {
                    List<Deck> outList = new ArrayList<>();
                    while (results.next()) {
                        outList.add( new Deck(results.getInt("deckId"),results.getInt("ownerId"),results.getString("title"),results.getInt("imageId"),results.getString("description"),results.getBoolean("isPrivate"),results.getDate("timeCreated")) );
                    }
                    return outList;
                });
    }

    public List<Deck> getNewest(int amount, int offset) throws SQLException {
        return executeQuery(
                "SELECT * FROM Deck WHERE Deck.deckId != 0 ORDER BY timeCreated DESC LIMIT ? OFFSET ?",
                (stmnt) -> {
                    stmnt.setInt(1,amount);
                    stmnt.setInt(2,offset);
                },

                (results) -> {
                    List<Deck> outList = new ArrayList<>();
                    while (results.next()) {
                        outList.add( new Deck(results.getInt("deckId"),results.getInt("ownerId"),results.getString("title"),results.getInt("imageId"),results.getString("description"),results.getBoolean("isPrivate"),results.getDate("timeCreated")) );
                    }
                    return outList;
                });
    }

    public List<Deck> getOldestViewedFavourites(int userId, int amount, int offset) throws SQLException {
        return executeQuery(
                "SELECT Deck.* FROM Deck, Favourite WHERE Favourite.deckId=Deck.deckId AND Favourite.userId=? ORDER BY Favourite.lastViewed ASC LIMIT ? OFFSET ?",
                (stmnt) -> {
                    stmnt.setInt(1,userId);
                    stmnt.setInt(2,amount);
                    stmnt.setInt(3,offset);
                },

                (results) -> {
                    List<Deck> outList = new ArrayList<>();
                    while (results.next()) {
                        outList.add( new Deck(results.getInt("deckId"),results.getInt("ownerId"),results.getString("title"),results.getInt("imageId"),results.getString("description"),results.getBoolean("isPrivate"),results.getDate("timeCreated")) );
                    }
                    return outList;
                });
    }

    public List<Deck> getUserDecks(int userId) throws SQLException {
        return executeQuery(
                "SELECT * FROM Deck WHERE Deck.ownerId = ?",
                (stmnt) -> {
                    stmnt.setInt(1,userId);
                },

                (results) -> {
                    List<Deck> outList = new ArrayList<>();
                    while (results.next()) {
                        outList.add( new Deck(results.getInt("deckId"),results.getInt("ownerId"),results.getString("title"),results.getInt("imageId"),results.getString("description"),results.getBoolean("isPrivate"),results.getDate("timeCreated")) );
                    }
                    return outList;
                });
    }
}
