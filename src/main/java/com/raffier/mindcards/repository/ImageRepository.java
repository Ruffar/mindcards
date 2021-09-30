package com.raffier.mindcards.repository;

import com.raffier.mindcards.model.table.Image;

public class ImageRepository extends EntityRepository<Image, Integer> {

    public ImageRepository(AppDatabase database) {
        super(database);
    }

    public <S extends Image> void save(S entity) {

    }

    public Image getById(Integer integer) {
        return null;
    }

    public <S extends Image> Image add(S entity) {
        return null;
    }

    public <S extends Image> void delete(S entity) {

    }

    public void deleteById(Integer integer) {

    }

}
