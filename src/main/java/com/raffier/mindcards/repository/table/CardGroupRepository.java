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
public class CardGroupRepository extends CardRepository<CardGroup> {

    @Autowired
    public CardGroupRepository(AppDatabase database) {
        super(database);
    }

    protected void throwEntityNotFound(Integer id) { throw new EntityNotFoundException("Card Group", id); }

    // Updates //
    public void save(CardGroup entity) throws SQLException {
        getById(entity.getPrimaryKey());
        executeUpdate(
                "UPDATE CardGroup SET deckId=?, title=?, imageId=?, description=? WHERE cardGroupId=?",
                (stmnt) -> {
                    stmnt.setInt(1, entity.getDeckId());
                    stmnt.setString(2, entity.getTitle());
                    stmnt.setObject(3, entity.getImageId());
                    stmnt.setString(4, entity.getDescription());
                    stmnt.setInt(5, entity.getCardGroupId());
                });
    }

    public CardGroup add(CardGroup entity) throws SQLException {
        int newId = executeUpdate(
                "INSERT INTO CardGroup (deckId, title, imageId, description) VALUES (?,?,?,?)",
                (stmnt) -> {
                    stmnt.setInt(1, entity.getDeckId());
                    stmnt.setString(2, entity.getTitle());
                    stmnt.setObject(3, entity.getImageId());
                    stmnt.setString(4, entity.getDescription());
                });
        System.out.println("Card Group with ID "+newId+" successfully created.");
        return getById(newId);
    }

    public void deleteById(Integer id) throws SQLException {
        getById(id);
        executeUpdate(
                "DELETE FROM CardGroup WHERE cardGroupId=?",
                (stmnt) -> stmnt.setInt(1, id));
        System.out.println("Card Group with ID "+id+" successfully deleted.");
    }

    // Queries //
    public CardGroup getById(Integer id) throws SQLException {
        return executeQuery(
                "SELECT * FROM CardGroup WHERE cardGroupId=?",
                (stmnt) -> stmnt.setInt(1,id),

                (results) -> {
                    if (results.next()) {
                        return new CardGroup(id,results.getInt("deckId"),results.getString("title"),results.getInt("imageId"),results.getString("description"));
                    }
                    throwEntityNotFound(id);
                    return null;
                });
    }

    public boolean isOwner(User user, int cardId) throws SQLException {
        return executeQuery(
                "SELECT Deck.* FROM Deck, CardGroup WHERE cardGroupId=? AND CardGroup.deckId=Deck.deckId AND ownerId=?",
                (stmnt) -> {
                    stmnt.setInt(1,cardId);
                    stmnt.setInt(2,user.getUserId());
                },
                (ResultSet::next) //Return true if a result is found
        );
    }

    public List<CardGroup> getRandomFromDeck(int deckId, int amount) throws SQLException {
        return executeQuery(
                "SELECT * FROM CardGroup WHERE CardGroup.deckId = ? ORDER BY RANDOM() LIMIT ?",
                (stmnt) -> {
                    stmnt.setInt(1, deckId);
                    stmnt.setInt(2, amount);
                },

                (results) -> {

                    List<CardGroup> outList = new ArrayList<>();
                    while (results.next()) {
                        outList.add(new CardGroup(results.getInt("cardGroupId"), results.getInt("deckId"), results.getString("title"), results.getInt("imageId"), results.getString("description")));
                    }
                    return outList;
                });
    }

    public List<CardGroup> search(int deckId, String searchString, int amount, int offset) throws SQLException {
        return executeQuery(
                "SELECT g1.*, " +
                        "(SELECT COUNT(g2.cardGroupId) FROM CardGroup g2 WHERE g1.cardGroupId = g2.cardGroupId AND (g2.title LIKE ? OR g2.description LIKE ?)) AS groupScore, " + //deckScore = 1 if title or description of a deck matches
                        "(SELECT COUNT(Mindcard.mindcardId) FROM Mindcard, GroupMindcard WHERE GroupMindcard.cardGroupId = g1.cardGroupId AND GroupMindcard.mindcardId = Mindcard.mindcardId AND (Mindcard.title LIKE ? OR Mindcard.description LIKE ?)) AS mindcardScore, " + //mindcardScore
                        "(SELECT COUNT(Infocard.infocardId) FROM Infocard, GroupMindcard WHERE GroupMindcard.cardGroupId = g1.cardGroupId AND GroupMindcard.mindcardId = Infocard.mindcardId AND Infocard.description LIKE ?) AS infocardScore " +
                        "FROM CardGroup g1 " +
                        "WHERE g1.deckId = ? " +
                        "ORDER BY groupScore DESC, mindcardScore DESC, infocardScore DESC LIMIT ? OFFSET ?;",

                (stmnt) -> {
                    for (int i = 1; i <= 5; i++) {
                        stmnt.setString(i, "%"+searchString+"%"); //Set parameters 1 to 5 inclusive as searchString
                    }
                    stmnt.setInt(6, deckId);
                    stmnt.setInt(7,amount);
                    stmnt.setInt(8,offset);
                },

                (results) -> {
                    List<CardGroup> outList = new ArrayList<>();
                    while (results.next()) {
                        outList.add( new CardGroup(results.getInt("cardGroupId"),results.getInt("deckId"),results.getString("title"),results.getInt("imageId"),results.getString("description")) );
                    }
                    return outList;
                });
    }

}
