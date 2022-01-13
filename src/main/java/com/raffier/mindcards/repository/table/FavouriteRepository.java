package com.raffier.mindcards.repository.table;

import com.raffier.mindcards.errorHandling.EntityNotFoundException;
import com.raffier.mindcards.model.table.Favourite;
import com.raffier.mindcards.repository.AppDatabase;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import java.sql.ResultSet;

@Component
public class FavouriteRepository extends EntityRepository<Favourite,Favourite> {

    @Autowired
    public FavouriteRepository(AppDatabase database) {
        super(database);
    }

    protected void throwEntityNotFound(Favourite id) { throw new EntityNotFoundException("Favourite", id); }

    // Updates //
    public void save(Favourite entity) {
        executeUpdate(
                "UPDATE Favourite SET lastViewed=? WHERE deckId=? AND userId=?",
                (stmnt) -> {
                    stmnt.setDate(1,entity.getLastViewed());
                    stmnt.setInt(2,entity.getDeckId());
                    stmnt.setInt(3,entity.getUserId());
                });
    }

    public Favourite add(Favourite entity) {
        executeUpdate(
                "INSERT INTO Favourite (deckId, userId, lastViewed) VALUES (?,?,?)",
                (stmnt) -> {
                    stmnt.setInt(1, entity.getDeckId());
                    stmnt.setInt(2, entity.getUserId());
                    stmnt.setDate(3, entity.getLastViewed());
                });
        return entity; //No values are changed, so the same entity can be returned
    }

    public void deleteById(Favourite id) {
        executeUpdate(
                "DELETE FROM Favourite WHERE deckId=? AND userId=?",
                (stmnt) -> {
                    stmnt.setInt(1, id.getDeckId());
                    stmnt.setInt(2, id.getUserId());
                });
        System.out.println("Favourite with ID "+id+" successfully deleted.");
    }

    // Queries //
    public Favourite getById(Favourite id) {
        return executeQuery(
                "SELECT * FROM Favourite WHERE deckId=? AND userId=?",
                (stmnt) -> {
                    stmnt.setInt(1, id.getDeckId());
                    stmnt.setInt(2, id.getUserId());
                },
                (results) -> {
                    if (results.next()) {
                        return new Favourite(results.getInt("deckId"), results.getInt("userId"), results.getDate("lastViewed"));
                    }
                    throwEntityNotFound(id);
                    return null;
                });
    }

    public boolean exists(Favourite entity) {
        return executeQuery(
                "SELECT * FROM Favourite WHERE deckId=? AND userId=?",
                (stmnt) -> {
                    stmnt.setInt(1,entity.getDeckId());
                    stmnt.setInt(2,entity.getUserId());
                },
                ResultSet::next //Return true if result is found
        );
    }

    public int getTotalDeckFavourites(int deckId) {
        return executeQuery(
                "SELECT COUNT(*) AS total FROM Favourite WHERE deckId=?",
                (stmnt) -> stmnt.setInt(1,deckId),

                (results) -> {
                    if (results.next()) {
                        return results.getInt("total");
                    }
                    return 0;
                });
    }
}
