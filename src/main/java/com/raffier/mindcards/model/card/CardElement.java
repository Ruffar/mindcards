package com.raffier.mindcards.model.card;

import com.raffier.mindcards.model.table.CardTable;
import com.raffier.mindcards.model.table.Image;
import com.raffier.mindcards.model.table.TitledCardTable;

import java.io.Serializable;

public class CardElement<T extends CardTable> implements Serializable {

    private T cardObject;
    private Image image;
    private String unescapedDesc;

    public CardElement() {}

    public CardElement(T cardObject, Image image, String unescapedDesc) {
        this.cardObject = cardObject;
        this.image = image;
        this.unescapedDesc = unescapedDesc;
    }

    public boolean hasTitle() { return (cardObject instanceof TitledCardTable); }

    public T getCardObject() { return this.cardObject; }
    public Image getImage() { return this.image; }
    public String getUnescapedDesc() { return this.unescapedDesc; }

    public void setCardObject(T card) { this.cardObject = card; }
    public void setImage(Image image) { this.image = image; }
    public void setUnescapedDesc(String unescapedDesc) { this.unescapedDesc = unescapedDesc; }

}
