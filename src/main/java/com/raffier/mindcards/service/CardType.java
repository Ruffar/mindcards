package com.raffier.mindcards.service;

import java.util.*;

public enum CardType {

    NONE("none"),
    INFOCARD("infocard"),
    MINDCARD("mindcard"),
    CARDGROUP("cardgroup"),
    CARDPACK("cardpack");

    private final String name;

    private CardType(String name) {
        this.name = name;
    }

    public String getName() { return this.name; }

    public static CardType getCardTypeFromString(String string) {
        for (CardType type : EnumSet.allOf(CardType.class)) {
            if (type.name.equals(string)) { return type; }
        }
        return NONE;
    }

}
