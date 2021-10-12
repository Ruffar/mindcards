package com.raffier.mindcards.service.markdown;

public class CharacterEntityMapping {

    private final String plaintext;
    private final String entityString;

    public CharacterEntityMapping(String plaintext, String entityString) {
        this.plaintext = plaintext;
        this.entityString = entityString;
    }

    public boolean canMapToEntity(String string) { return string.equals(plaintext); }
    public boolean canMapToPlain(String string) { return string.equals(entityString); }

    public String getPlain() { return plaintext; }
    public String getEntityString() { return entityString; }

}
