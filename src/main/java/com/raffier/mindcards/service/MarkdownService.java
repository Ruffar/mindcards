package com.raffier.mindcards.service;

import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class MarkdownService {

    Set<CharacterEntityMapping> characterMappings;

    private MarkdownService() {
        initializeMappings();
    }

    private void initializeMappings() {
        characterMappings = new HashSet<CharacterEntityMapping>();
        //Reserved HTML characters
        characterMappings.add(new CharacterEntityMapping("<", "&lt;"));
        characterMappings.add(new CharacterEntityMapping(">", "&gt;"));
        characterMappings.add(new CharacterEntityMapping("&", "&amp;"));
        characterMappings.add(new CharacterEntityMapping("\"", "&quot;")); // \" refers to the quotation marks character which can't be written normally
        characterMappings.add(new CharacterEntityMapping("'", "&apos;"));
        //Markdown characters
        characterMappings.add(new MarkdownEntityMapping("**", "<b>","</b>"));
    }

    public String parsePlaintext(String plaintext) {
        StringBuilder output = new StringBuilder();

        Stack<MarkdownEntityMapping> markdownQueue = new Stack<>();

        for (int i = 0; i < plaintext.length(); i++) {

            String plainString = plaintext.substring(i,i+2); //end parameter of substring is exclusive, gets the next 2 characters

            boolean plainMapped = false;
            String resultString = "";
            for (CharacterEntityMapping mapping : characterMappings) {
                if (mapping.canMapToPlain(plainString.substring(0,1)) || mapping.canMapToPlain(plainString)) { // checks whether the first character of the plain string maps then if both
                    if (mapping instanceof MarkdownEntityMapping) {
                        if (markdownQueue.peek() == mapping) {
                            resultString = ((MarkdownEntityMapping) mapping).getEntityString(true);
                            markdownQueue.pop();
                        } else {
                            resultString = mapping.getEntityString();
                            markdownQueue.push((MarkdownEntityMapping) mapping);
                        }
                    } else {
                        resultString = mapping.getEntityString();
                    }
                    plainMapped = true;
                }
            }
            resultString = plainString;

        }

        return output.toString();
    }

    private static class CharacterEntityMapping {

        private final String plaintext;
        private final String entityString;

        private CharacterEntityMapping(String plaintext, String entityString) {
            this.plaintext = plaintext;
            this.entityString = entityString;
        }

        public boolean canMapToEntity(String string) { return string.equals(plaintext); }
        public boolean canMapToPlain(String string) { return string.equals(entityString); }

        public String getPlain() { return plaintext; }
        public String getEntityString() { return entityString; }

    }

    private static class MarkdownEntityMapping extends CharacterEntityMapping {

        private final String closingString;

        private MarkdownEntityMapping(String plaintext, String entityString, String closingString) {
            super(plaintext, entityString);
            this.closingString = closingString;
        }

        public boolean canMapToPlain(String string) { return super.canMapToPlain(string) || string.equals(closingString); }

        public String getEntityString(boolean closing) { return closing ? closingString : getEntityString(); }

    }

}
