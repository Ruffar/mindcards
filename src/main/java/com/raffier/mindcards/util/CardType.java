package com.raffier.mindcards.util;

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
        if (string != null) {
            String lowercase = string.toLowerCase();
            for (CardType type : EnumSet.allOf(CardType.class)) {
                if (type.name.equals(lowercase)) { return type; }
            }
        }
        return NONE;
    }

    public CardType getParentType() {
        return this.parent;
    }

}
