package com.raffier.mindcards.repository.table;

import com.raffier.mindcards.errorHandling.EntityNotFoundException;
import com.raffier.mindcards.model.table.GroupMindcard;
import com.raffier.mindcards.repository.AppDatabase;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import java.sql.ResultSet;
import java.sql.SQLException;

@Component
public class GroupMindcardRepository extends EntityRepository<GroupMindcard,GroupMindcard> {

    @Autowired
    public GroupMindcardRepository(AppDatabase database) {
        super(database);
    }

    protected void throwEntityNotFound(GroupMindcard id) { throw new EntityNotFoundException("GroupMindcard", id); }

    // Updates //
    public void save(GroupMindcard entity) {
        //Saving GroupMindcard does not change anything as all columns are primary keys
    }

    public GroupMindcard add(GroupMindcard entity) throws SQLException {
        executeUpdate(
                "INSERT INTO GroupMindcard (cardGroupId, mindcardId) VALUES (?,?)",
                (stmnt) -> {
                    stmnt.setInt(1, entity.getCardGroupId());
                    stmnt.setInt(2, entity.getMindcardId());
                });
        return entity; //No values are changed, so the same entity can be returned
    }

    public void deleteById(GroupMindcard id) throws SQLException {
        getById(id);
        executeUpdate(
                "DELETE FROM GroupMindcard WHERE cardGroupId=? AND mindcardId=?",
                (stmnt) -> {
                    stmnt.setInt(1, id.getCardGroupId());
                    stmnt.setInt(2, id.getMindcardId());
                });
        System.out.println("Favourite with ID "+id+" successfully deleted.");
    }

    // Queries //
    public GroupMindcard getById(GroupMindcard id) throws SQLException {
        return executeQuery(
                "SELECT * FROM GroupMindcard WHERE cardGroupId=? AND mindcardId=?",
                (stmnt) -> {
                    stmnt.setInt(1, id.getCardGroupId());
                    stmnt.setInt(2, id.getMindcardId());
                },
                (results) -> {
                    if (results.next()) {
                        return new GroupMindcard(results.getInt("cardGroupId"), results.getInt("mindcardId"));
                    }
                    throwEntityNotFound(id);
                    return null;
                });
    }

    public boolean exists(GroupMindcard entity) throws SQLException {
        return executeQuery(
                "SELECT * FROM GroupMindcard WHERE cardGroupId=? AND mindcardId=?",
                (stmnt) -> {
                    stmnt.setInt(1,entity.getCardGroupId());
                    stmnt.setInt(2,entity.getMindcardId());
                },

                ResultSet::next //Return true is a result is found
        );
    }

}
