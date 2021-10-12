package com.raffier.mindcards.service.markdown;

import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class MarkdownService {

    private final int longestPlain = 2;

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
        characterMappings.add(new MarkdownEntityMapping("*", "<em>","</em>"));
    }

    public String parsePlaintext(String plaintext) {
        StringBuilder output = new StringBuilder();

        Stack<MarkdownEntityMapping> markdownQueue = new Stack<>();

        int i = 0;
        while (i < plaintext.length()) {

            String plainString = plaintext.substring(i,i+longestPlain); //The plain string being parsed which also includes extra characters

            //Find a mapping that matches
            CharacterEntityMapping matchingMap = null;
            for (CharacterEntityMapping mapping : characterMappings) {
                String entityPlain = mapping.getPlain();
                int entityPlainLength = entityPlain.length();
                if (plainString.substring(0,entityPlainLength).equals(entityPlain) && (matchingMap == null || mapping.getPlain().length() > entityPlainLength)) {
                    //Checks whether the first letters of a substring matches the mapping plain string
                    //if there is already a matching map, check whether the new mapping's plain string length is longer
                    //Example: Mapping (*) is already found but Mapping (**) can also be applied
                    matchingMap = mapping;
                }
            }
            //Get resulting string and handle markdown
            int plainLength = 1;
            String resultString = plainString.substring(0,1);
            if (matchingMap != null) {
                plainLength = matchingMap.getPlain().length();
                resultString = matchingMap.getEntityString();

                if (matchingMap instanceof MarkdownEntityMapping) {
                    if (markdownQueue.peek() == matchingMap) {
                        resultString = ( (MarkdownEntityMapping)matchingMap ).getClosingString();
                        markdownQueue.pop();
                    }
                    else { markdownQueue.add( (MarkdownEntityMapping)matchingMap ); }
                }
            }
            //Add result to output and increment i to the next un-parsed character
            output.append(resultString);
            i += plainLength;
        }

        return output.toString();
    }

}
