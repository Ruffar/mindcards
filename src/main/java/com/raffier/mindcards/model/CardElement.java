package com.raffier.mindcards.model;

public class CardElement {

    private String title;
    private String imagePath;
    private String description;

    public CardElement(String title, String imagePath, String description) {
        this.title = title;
        this.imagePath = imagePath;
        this.description = description;
    }

    public String getTitle() { return this.title; }
    public String getImagePath() { return this.imagePath; }
    public String getDescription() { return description; }
}
