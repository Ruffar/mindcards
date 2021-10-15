package com.raffier.mindcards.repository.table;

import com.raffier.mindcards.errorHandling.EntityNotFoundException;
import com.raffier.mindcards.repository.AppDatabase;

public abstract class EntityRepository<T, ID> {

    protected AppDatabase database;
    public EntityRepository(AppDatabase database) {
        this.database = database;
    }

    private void throwEntityNotFound(ID id) { throw new EntityNotFoundException("Unknown table",id);
    }

    public abstract <S extends T> void save(S entity);

    public abstract T getById(ID id);

    public abstract <S extends T> T add(S entity);

    public abstract <S extends T> void delete(S entity);

    public abstract void deleteById(ID id);

}
