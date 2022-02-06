package com.raffier.mindcards.repository.table;

import com.raffier.mindcards.model.table.CardTable;
import com.raffier.mindcards.model.table.User;
import com.raffier.mindcards.repository.AppDatabase;

import java.sql.SQLException;

public abstract class CardRepository<T extends CardTable> extends EntityRepository<T,Integer> {

    protected CardRepository(AppDatabase database) {
        super(database);
    }

    public abstract boolean isOwner(User user, int cardId) throws SQLException;
}
