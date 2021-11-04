package com.raffier.mindcards.repository.table;

import com.raffier.mindcards.errorHandling.EntityNotFoundException;
import com.raffier.mindcards.model.table.*;
import com.raffier.mindcards.repository.AppDatabase;
import com.raffier.mindcards.repository.SQLConsumer;
import com.raffier.mindcards.repository.SQLFunction;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.function.Function;

@Component
public class DeckRepository extends EntityRepository<Deck,Integer> {

    @Autowired
    public DeckRepository(AppDatabase database) {
        super(database);
    }

    protected void throwEntityNotFound(Integer id) { throw new EntityNotFoundException("Deck", id); }

    public <S extends Deck> void save(S entity) {
        executeUpdate(
                "UPDATE Deck SET ownerId=?, title=?, imageId=?, description=?, isPrivate=?, timeCreated=? WHERE deckId=?",
                (stmnt) -> {
                    stmnt.setInt(1,entity.getOwnerId());
                    stmnt.setString(2,entity.getTitle());
                    stmnt.setInt(3,entity.getImageId());
                    stmnt.setString(4,entity.getDescription());
                    stmnt.setBoolean(5,entity.isPrivate());
                    stmnt.setDate(6,entity.getTimeCreated());
                    stmnt.setInt(7,entity.getDeckId());
        });
    }

    public Deck getById(Integer id) {
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

    public List<Deck> search(String searchString, int amount, int offset) {
        return executeQuery(
                "SELECT Deck.*, (IFF(Deck.title LIKE '%?%' OR Deck.description LIKE '%?%',100,0) + COUNT(Mindcard.*)*2 + COUNT(CardGroup.*)*2 + COUNT(Infocard.*)) as MatchScore FROM Deck " +
                "INNER JOIN Mindcard ON Mindcard.deckId = Deck.deckId AND (Mindcard.title LIKE '%?%' OR Mindcard.description LIKE '%?%') " +
                "INNER JOIN CardGroup ON CardGroup.deckId = Deck.deckId AND (CardGroup.title LIKE '%?%' OR CardGroup.description LIKE '%?%') " +
                "INNER JOIN Infocard ON Infocard.mindcardId = Mindcard.mindcardId AND Mindcard.deckId = Deck.deckId AND Infocard.description LIKE '%?%' " +
                "ORDER BY MatchScore HAVING MatchScore > 0 DESC LIMIT ? OFFSET ?",

                (stmnt) -> {
                    for (int i = 1; i <= 7; i++) {
                        stmnt.setString(i, searchString); //Set parameters 1 to 7 inclusive as searchString
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

    public List<Deck> getNewest(int amount, int offset) {
        return executeQuery(
                "SELECT * FROM Deck ORDER BY timeCreated DESC LIMIT ? OFFSET ?",
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

    public List<Deck> getRandom(int amount) {
        return executeQuery(
                "SELECT Deck.* FROM Deck ORDER BY RANDOM() LIMIT ?",
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

    public List<Deck> getUserDecks(int userId) {
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

    public List<Deck> getFavouritedDecks(int userId) {
        return executeQuery(
                "SELECT Deck.* FROM Deck, Favourite WHERE Favourite.userId = ? AND Deck.deckId = Favourite.deckId",
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

    public <S extends Deck> Deck add(S entity) {
        int newId = executeUpdate(
                "INSERT INTO Deck (ownerId, title, imageId, description, isPrivate, timeCreated) VALUES (?,?,?,?,?,?)",
                (stmnt) -> {
                    stmnt.setInt(1,entity.getOwnerId());
                    stmnt.setString(2,entity.getTitle());
                    stmnt.setInt(3,entity.getImageId());
                    stmnt.setString(4,entity.getDescription());
                    stmnt.setBoolean(5, entity.isPrivate());
                    stmnt.setDate(6, new Date(Instant.now().getEpochSecond())); //Set the deck's creation time to current time
                });
        System.out.println("Deck with ID "+newId+" successfully created.");
        return getById(newId);
    }

    public <S extends Deck> void delete(S entity) {
        deleteById(entity.getDeckId());
    }

    public void deleteById(Integer id) {
        executeUpdate(
                "DELETE FROM Deck WHERE deckId=?",
                (stmnt) -> stmnt.setInt(1,id));
        System.out.println("Deck with ID "+id+" successfully deleted.");
    }

    /*public List<Tag> getTags(int deckId) {
        List<Tag> outList = new ArrayList<>();
        try {
            PreparedStatement statement = database.getConnection().prepareStatement("SELECT Tag.tagId, Tag.tagName FROM Tag, PackTag WHERE PackTag.deckId=? AND Tag.tagId=PackTag.tagId");
            statement.setInt(1,deckId);
            ResultSet result = statement.executeQuery();
            while (result.next()) {
                outList.add(new Tag(result.getInt("tagId"),result.getString("tagName")));
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return outList;
    }*/

}
