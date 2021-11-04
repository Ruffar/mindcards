package com.raffier.mindcards.service;

import java.util.*;

public enum CardType {

    NONE("none"),
    DECK("deck"),
    MINDCARD("mindcard",DECK),
    INFOCARD("infocard",MINDCARD),
    CARDGROUP("cardgroup",DECK);

    private final String name;
    private final CardType parent;

    private CardType(String name, CardType parent) {
        this.name = name;
        this.parent = parent;
    }
    private CardType(String name) {
        this(name,null);
    }

    public String getName() { return this.name; }
    public String toString() { return this.name; }

    public static CardType getCardTypeFromString(String string) {
        for (CardType type : EnumSet.allOf(CardType.class)) {
            if (type.name.equals(string.toLowerCase())) { return type; }
        }
        return NONE;
    }

    public CardType getParentType() {
        return this.parent;
    }

}
