package com.raffier.mindcards.repository.table;

import com.raffier.mindcards.errorHandling.EntityNotFoundException;
import com.raffier.mindcards.model.table.Favourite;
import com.raffier.mindcards.repository.AppDatabase;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class FavouriteRepository extends EntityRepository<Favourite,Favourite> {

    @Autowired
    public FavouriteRepository(AppDatabase database) {
        super(database);
    }

    protected void throwEntityNotFound(Favourite id) { throw new EntityNotFoundException("Favourite", id); }

    // Updates //
    public void save(Favourite entity) {
        //As all columns of a favourite are part of the primary key, there is no possible method of changing any value without creating a new entity
    }

    public Favourite add(Favourite entity) {
        executeUpdate(
                "INSERT INTO Favourite (deckId, userId) VALUES (?,?)",
                (stmnt) -> {
                    stmnt.setInt(1, entity.getDeckId());
                    stmnt.setInt(2, entity.getUserId());
                });
        return entity; //No values are changed, so the same entity can be returned
    }

    public void delete(Favourite entity) {
        deleteById(entity);
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
                        return id; //Similarly, if the deck exists, then it is the same entity used to search
                    }
                    return null;
                });
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
