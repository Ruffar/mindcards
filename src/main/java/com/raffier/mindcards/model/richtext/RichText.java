package com.raffier.mindcards.model.richtext;

import java.util.LinkedList;
import java.util.List;

public class RichText {

    List<TextSegment> textList = new LinkedList<>();

    public RichText() {

    }

    public class TextSegment {

        private String text;

        private String hyperlink;
        public boolean isBold;
        public boolean isItalic;

        private TextSegment(String text) {
            this.text = text;
        }

    }

}
