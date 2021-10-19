package com.raffier.mindcards.model.card;

import com.raffier.mindcards.model.table.CardTable;
import com.raffier.mindcards.model.table.Image;
import com.raffier.mindcards.model.table.TitledCardTable;

import java.io.Serializable;

public class CardElement<T extends CardTable> extends CardImagePair<T> implements Serializable {

    private String unescapedDesc;

    public CardElement() {}

    public CardElement(T cardObject, Image image, String unescapedDesc) {
        super(cardObject, image);
        this.unescapedDesc = unescapedDesc;
    }

    public String getUnescapedDesc() { return this.unescapedDesc; }

    public void setUnescapedDesc(String unescapedDesc) { this.unescapedDesc = unescapedDesc; }

}
