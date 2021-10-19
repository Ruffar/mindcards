package com.raffier.mindcards.service;

import com.raffier.mindcards.service.CardService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.util.HtmlUtils;

import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service
public class MarkdownService {

    @Autowired
    CardService cardService;

    //
    public String parsePlaintext(String plaintext) {
        String output = HtmlUtils.htmlEscape(plaintext); //Get a copy of plaintext where all characters are escaped

        output = parseBold(output);
        output = parseItalic(output);
        output = parseUnderline(output);
        output = parseStrikethrough(output);
        output = parseBlockquote(output);

        //output = parseHyperlink(output);
        output = parseHorizontalRule(output);

        output = parseBulletList(output);
        output = parseNumberList(output);

        output = parseLineBreak(output);

        return output;
    }

    //Parsing functions
    private String parseLineBreak(String plainText) {
        return plainText.replaceAll(">\n",">").replaceAll("\n<","<").replaceAll("\n","</br>");
    }

    private String parseBold(String plainText) {
        return plainText.replaceAll("\\*\\*(.+?)\\*\\*","<strong>$1</strong>");
    }

    private String parseItalic(String plainText) {
        return plainText.replaceAll("\\*(.+?)\\*","<em>$1</em>");
    }

    private String parseUnderline(String plainText) {
        return plainText.replaceAll("__(.+?)__","<u>$1</u>");
    }

    private String parseStrikethrough(String plainText) {
        return plainText.replaceAll("~~(.+?)~~","<del>$1</del>");
    }

    private String parseBlockquote(String plainText) {
        return plainText.replaceAll("(?m)^&gt;\\s(.+?)$","<blockquote>$1</blockquote>"); // &gt; is the html character for the > sign
    }

    private String parseHorizontalRule(String plainText) {
        return plainText.replaceAll("(?m)^---$","<hr>");
    }

    private String parseBulletList(String plainText) {
        Pattern pattern = Pattern.compile("^-(.+?)$",Pattern.MULTILINE);
        Matcher matcher = pattern.matcher(plainText);
        StringBuilder result = new StringBuilder();
        int lastEnd = 0;
        while(matcher.find()) {
            String betweenText = plainText.substring(lastEnd, matcher.start());

            //If the first letter is '-' then open a list
            if ((!betweenText.matches("\\R") && lastEnd != matcher.start()) || lastEnd == 0) {
                if (lastEnd > 0) result.append("</ul>");
                result.append(betweenText).append("<ul>");
            }
            result.append("<li>").append(matcher.group(1)).append("</li>");

            lastEnd = matcher.end();
        }
        if (lastEnd != 0) result.append("</ul>"); //If the last match's end is not 0, then the list must be closed
        result.append(plainText.substring(lastEnd)); //Add the rest of the unread string
        return result.toString();
    }

    private String parseNumberList(String plainText) {
        Pattern pattern = Pattern.compile("^[0-9]+?\\.(.+?)$",Pattern.MULTILINE);
        Matcher matcher = pattern.matcher(plainText);
        StringBuilder result = new StringBuilder();
        int lastEnd = 0;
        while(matcher.find()) {
            String betweenText = plainText.substring(lastEnd, matcher.start());

            System.out.println(betweenText);
            //If the string starts with a number list then open a list
            if ((!betweenText.matches("\\R") && lastEnd != matcher.start()) || lastEnd == 0) {
                if (lastEnd > 0) result.append("</ol>");
                result.append(betweenText).append("<ol>");
            }
            result.append("<li>").append(matcher.group(1)).append("</li>");

            lastEnd = matcher.end();
        }
        if (lastEnd != 0) result.append("</ol>"); //If the last match's end is not 0, then the list must be closed
        result.append(plainText.substring(lastEnd)); //Add the rest of the unread string
        return result.toString();
    }

    private String parseHyperlink(String plainText) {
        return plainText;
    }

}
