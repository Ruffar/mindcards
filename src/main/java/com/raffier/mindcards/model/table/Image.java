package com.raffier.mindcards.model.table;

public class Image extends EntityTable<Integer> {

    //Database columns
    private final int imageId;
    private String imagePath;

    public Image(int imageId) {
        super("Image");
        this.imageId = imageId;
        this.imagePath = "";
    }

    public Image(int imageId, String imagePath) {
        this(imageId);
        this.imagePath = imagePath;
    }

    public Integer getPrimaryKey() { return getImageId(); }
    public int getImageId() { return imageId; }
    public String getImagePath() { return imagePath; }

    public void setImagePath(String imagePath) { this.imagePath = imagePath; }
}
