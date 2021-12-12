package com.raffier.mindcards.util;

import java.util.EnumSet;

public enum ImageChangeType {

    NONE("none"),
    REMOVE("remove"),
    UPLOAD("upload"),
    URL("url");

    private final String name;

    private ImageChangeType(String name) {
        this.name = name;
    }

    public String getName() { return this.name; }
    public String toString() { return this.name; }

    public static ImageChangeType getImageChangeTypeFromString(String string) {
        if (string != null) {
            String lowercase = string.toLowerCase();
            for (ImageChangeType type : EnumSet.allOf(ImageChangeType.class)) {
                if (type.name.equals(lowercase)) { return type; }
            }
        }
        return NONE;
    }

}
