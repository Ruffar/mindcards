package com.raffier.mindcards.repository.table;

import com.raffier.mindcards.errorHandling.EntityNotFoundException;
import com.raffier.mindcards.model.table.CardGroup;
import com.raffier.mindcards.model.table.Image;
import com.raffier.mindcards.model.table.Mindcard;
import com.raffier.mindcards.repository.AppDatabase;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

@Component
public class CardGroupRepository extends EntityRepository<CardGroup,Integer> {

    @Autowired
    public CardGroupRepository(AppDatabase database) {
        super(database);
    }

    protected void throwEntityNotFound(Integer id) { throw new EntityNotFoundException("Card Group", id); }

    public <S extends CardGroup> void save(S entity) {
        executeUpdate(
                "UPDATE CardGroup SET deckId=?, title=?, imageId=?, description=? WHERE cardGroupId=?",
                (stmnt) -> {
                    stmnt.setInt(2, entity.getDeckId());
                    stmnt.setString(1, entity.getTitle());
                    stmnt.setInt(3, entity.getImageId());
                    stmnt.setString(4, entity.getDescription());
                });
    }

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

    public <S extends CardGroup> CardGroup add(S entity) {
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

    public <S extends CardGroup> void delete(S entity) {
        deleteById(entity.getCardGroupId());
    }

    public void deleteById(Integer id) {
        executeUpdate(
                "DELETE FROM CardGroup WHERE cardGroupId=?",
                (stmnt) -> stmnt.setInt(1, id));
        System.out.println("Card Group with ID "+id+" successfully deleted.");
    }

}
