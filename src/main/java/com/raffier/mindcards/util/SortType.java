package com.raffier.mindcards.util;

import java.util.EnumSet;

public enum SortType {
    NONE("none"),
    NEWEST("newest"),
    POPULAR("popular");

    private final String name;

    SortType(String name) {
        this.name = name;
    }

    public String getName() { return this.name; }
    public String toString() { return this.name; }

    public static SortType getSortTypeFromString(String string) {
        if (string != null) {
            String lowercase = string.toLowerCase();
            for (SortType type : EnumSet.allOf(SortType.class)) {
                if (type.name.equals(lowercase)) { return type; }
            }
        }
        return NONE;
    }
}
