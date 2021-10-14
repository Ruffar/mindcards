package com.raffier.mindcards.service.markdown;

import org.springframework.stereotype.Service;

@Service
public class BoldParser {

    public String parseText(String plainText) {
        return plainText.replaceAll("\\*\\*(.+)\\*\\*","<b>$1</b>");
    }

}
