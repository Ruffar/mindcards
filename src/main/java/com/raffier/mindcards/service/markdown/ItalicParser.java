package com.raffier.mindcards.service.markdown;

import org.springframework.stereotype.Service;

@Service
public class ItalicParser extends MarkdownParser {

    public String parseText(String plainText) {
        return plainText.replaceAll("\\*(.+)\\*","<em>$1</em>");
    }

}
