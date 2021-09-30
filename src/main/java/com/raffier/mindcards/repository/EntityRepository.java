package com.raffier.mindcards.repository;

public abstract class EntityRepository<T, ID> {

    protected AppDatabase database;
    public EntityRepository(AppDatabase database) {
        this.database = database;
    }

    public abstract <S extends T> void save(S entity);

    public abstract T getById(ID id);

    public abstract <S extends T> T add(S entity);

    public abstract <S extends T> void delete(S entity);

    public abstract void deleteById(ID id);

}
