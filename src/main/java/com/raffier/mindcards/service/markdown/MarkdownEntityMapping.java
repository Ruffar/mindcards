package com.raffier.mindcards.service.markdown;

public class MarkdownEntityMapping extends CharacterEntityMapping {

    private final String closingString;

    public MarkdownEntityMapping(String plaintext, String entityString, String closingString) {
        super(plaintext, entityString);
        this.closingString = closingString;
    }

    public boolean canMapToPlain(String string) { return super.canMapToPlain(string) || string.equals(closingString); }

    public String getClosingString() { return closingString; }

}