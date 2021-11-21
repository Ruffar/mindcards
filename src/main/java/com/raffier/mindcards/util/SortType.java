package com.raffier.mindcards.util;

import java.util.EnumSet;

public enum SortType {
    NONE("none"),
    NEWEST("newest"),
    POPULAR("popular");

    private final String name;

    private SortType(String name) {
        this.name = name;
    }

    public String getName() { return this.name; }
    public String toString() { return this.name; }

    public static SortType getSortTypeFromString(String string) {
        for (SortType type : EnumSet.allOf(SortType.class)) {
            if (type.name.equals(string.toLowerCase())) { return type; }
        }
        return NONE;
    }

}
