package com.raffier.mindcards.service;

public enum CardType {

    INFOCARD("infocard"),
    MINDCARD("mindcard"),
    CARDGROUP("cardgroup"),
    CARDPACK("cardpack");

    private String name;

    private CardType(String name) {
        this.name = name;
    }

    public String getName() { return this.name; }

    public static CardType getCardTypeFromString();

}
