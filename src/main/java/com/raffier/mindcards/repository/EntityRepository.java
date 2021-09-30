package com.raffier.mindcards.repository;

public abstract class EntityRepository<T, ID> {

    protected AppDatabase database;
    public EntityRepository(AppDatabase database) {
        this.database = database;
    }

    abstract <S extends T> void save(S entity);

    abstract T getById(ID id);

    abstract <S extends T> T add(S entity);

    abstract <S extends T> void delete(S entity);

    abstract void deleteById(ID id);

}
