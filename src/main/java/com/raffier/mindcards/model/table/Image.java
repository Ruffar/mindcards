package com.raffier.mindcards.model.table;

import com.raffier.mindcards.repository.AppDatabase;

import java.sql.*;

public class Image extends EntityTable<Integer> {

    //Database columns
    private int imageId;
    private String imagePath;

    public Image(int imageId) {
        super("Image");
        this.imageId = imageId;
    }

    public Image(int imageId, String imagePath) {
        this(imageId);
        this.imagePath = imagePath;
    }

    public Integer getPrimaryKey() { return getImageId(); }
    public int getImageId() { return imageId; }
    public String getImagePath() { return imagePath; }

    public void setPrimaryKey(Integer primaryKey) { setImageId(primaryKey); }
    public void setImageId(int imageId) { this.imageId = imageId; }
    public void setImagePath(String imagePath) { this.imagePath = imagePath; }

}
