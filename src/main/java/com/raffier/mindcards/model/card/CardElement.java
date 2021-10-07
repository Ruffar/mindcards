package com.raffier.mindcards.model.card;

public class CardElement {

    private int cardId;
    private String title;
    private String imagePath;
    private String description;

    public CardElement(int cardId, String title, String imagePath, String description) {
        this.cardId = cardId;
        this.title = title;
        this.imagePath = imagePath;
        this.description = description;
    }

    public int getCardId() { return this.cardId; }
    public String getTitle() { return this.title; }
    public String getImagePath() { return this.imagePath; }
    public String getDescription() { return description; }

    public void setCardId(int cardId) { this.cardId = cardId; }
    public void setTitle(String title) { this.title = title; }
    public void setImagePath(String imagePath) { this.imagePath = imagePath; }
    public void setDescription(String description) { this.description = description; }

}
