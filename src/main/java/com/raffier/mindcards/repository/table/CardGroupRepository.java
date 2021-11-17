package com.raffier.mindcards.repository.table;

import com.raffier.mindcards.errorHandling.EntityNotFoundException;
import com.raffier.mindcards.model.table.CardGroup;
import com.raffier.mindcards.model.table.Image;
import com.raffier.mindcards.model.table.Mindcard;
import com.raffier.mindcards.model.table.User;
import com.raffier.mindcards.repository.AppDatabase;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.sql.PreparedStatement;
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
    public void save(CardGroup entity) {
        executeUpdate(
                "UPDATE CardGroup SET deckId=?, title=?, imageId=?, description=? WHERE cardGroupId=?",
                (stmnt) -> {
                    stmnt.setInt(2, entity.getDeckId());
                    stmnt.setString(1, entity.getTitle());
                    stmnt.setInt(3, entity.getImageId());
                    stmnt.setString(4, entity.getDescription());
                });
    }

    public CardGroup add(CardGroup entity) {
        int newId = executeUpdate(
                "INSERT INTO CardGroup (deckId, title, imageId, description) VALUES (?,?,?,?)",
                (stmnt) -> {
                    stmnt.setInt(1, entity.getDeckId());
                    stmnt.setString(2, entity.getTitle());
                    stmnt.setInt(3, entity.getImageId());
                    stmnt.setString(4, entity.getDescription());
                });
        System.out.println("Card Group with ID "+newId+" successfully created.");
        return getById(newId);
    }

    public void deleteById(Integer id) {
        executeUpdate(
                "DELETE FROM CardGroup WHERE cardGroupId=?",
                (stmnt) -> stmnt.setInt(1, id));
        System.out.println("Card Group with ID "+id+" successfully deleted.");
    }

    // Queries //
    public CardGroup getById(Integer id) {
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

    public boolean isOwner(User user, int cardId) {
        return executeQuery(
                "SELECT Deck.* FROM Deck, CardGroup WHERE cardGroupId=? AND CardGroup.deckId=Deck.deckId AND ownerId=?",
                (stmnt) -> {
                    stmnt.setInt(1,cardId);
                    stmnt.setInt(2,user.getUserId());
                },
                (ResultSet::next)
        );
    }

    public boolean isPrivate(int cardId) {
        return executeQuery(
                "SELECT Deck.* FROM Deck, CardGroup WHERE cardGroupId=? AND CardGroup.deckId=Deck.deckId AND isPrivate=true",
                (stmnt) -> stmnt.setInt(1,cardId),
                (ResultSet::next)
        );
    }

    public List<CardGroup> getRandomFromDeck(int deckId, int amount) {
        return executeQuery(
                "SELECT CardGroup.* FROM CardGroup, Deck WHERE CardGroup.deckId = ? ORDER BY RANDOM() LIMIT ?",
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

}
