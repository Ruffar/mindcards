package com.raffier.mindcards.util;

import java.util.*;

public enum CardType {

    NONE("none"),
    DECK("deck"),
    MINDCARD("mindcard",DECK),
    INFOCARD("infocard",MINDCARD),
    CARDGROUP("cardgroup",DECK);

    private final String name; //The name of the enum
    private final CardType parent; //The parent enum, can be null

    CardType(String name, CardType parent) {
        this.name = name;
        this.parent = parent;
    }
    CardType(String name) {
        this(name,null);
    }

    public String getName() { return this.name; }
    public String toString() { return this.name; }

    public static CardType getCardTypeFromString(String string) {
        if (string != null) {
            String lowercase = string.toLowerCase(); //ignore capitalization
            for (CardType type : EnumSet.allOf(CardType.class)) { //Check all enums
                if (type.name.equals(lowercase)) { return type; } //If they match, return the type
            }
        }
        return NONE;
    }

    public CardType getParentType() {
        return this.parent;
    }

}
