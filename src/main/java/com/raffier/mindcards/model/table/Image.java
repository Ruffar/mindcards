package com.raffier.mindcards.model.table;

import com.raffier.mindcards.repository.AppDatabase;

import java.sql.*;

public class Image extends EntityTable<Integer> {

    //Database columns
    private int imageId;
    private String name;
    private String imagePath;

    public Image(int imageId) {
        super("Image");
        this.imageId = imageId;
    }

    public Image(int imageId, String name, String imagePath) {
        this(imageId);
        this.name = name;
        this.imagePath = imagePath;
    }

    public Integer getPrimaryKey() { return getImageId(); }
    public int getImageId() { return imageId; }
    public String getName() { return name; }
    public String getImagePath() { return imagePath; }

    public void setPrimaryKey(Integer primaryKey) { setImageId(primaryKey); }
    public void setImageId(int imageId) { this.imageId = imageId; }
    public void setName(String name) { this.name = name; }
    public void setImagePath(String imagePath) { this.imagePath = imagePath; }

}
