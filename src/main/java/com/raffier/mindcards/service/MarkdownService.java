package com.raffier.mindcards.service;

import com.raffier.mindcards.service.markdown.BoldParser;
import com.raffier.mindcards.service.markdown.ItalicParser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class MarkdownService {

    @Autowired
    ItalicParser italicParser;
    @Autowired
    BoldParser boldParser;

    Set<CharacterEntityMapping> characterMappings;

    private MarkdownService() {
        initializeMappings();
    }

    private void initializeMappings() {
        characterMappings = new HashSet<CharacterEntityMapping>();
        //Reserved HTML characters
        //Order matters
        characterMappings.add(new CharacterEntityMapping("&", "&amp;"));
        characterMappings.add(new CharacterEntityMapping("<", "&lt;"));
        characterMappings.add(new CharacterEntityMapping(">", "&gt;"));
        characterMappings.add(new CharacterEntityMapping("\"", "&quot;")); // \" refers to the quotation marks character which can't be written normally
        characterMappings.add(new CharacterEntityMapping("'", "&apos;"));
    }

    public String parsePlaintext(String plaintext) {
        String output = plaintext; //Create a copy of the plaintext

        for (CharacterEntityMapping mapping : characterMappings) {
            output = output.replaceAll(mapping.getPlain(),mapping.getEntityString());
        }

        output = boldParser.parseText(output);
        output = italicParser.parseText(output);

        return output;
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

}
