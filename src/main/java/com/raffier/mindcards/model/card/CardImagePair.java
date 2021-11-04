package com.raffier.mindcards.model.card;

import com.raffier.mindcards.model.table.CardTable;
import com.raffier.mindcards.model.table.Image;

public class CardImagePair<T extends CardTable> {

    private final T card;
    private final Image image;

    public CardImagePair(T card, Image image) {
        this.card = card;
        this.image = image;
    }

    public T getCard() { return card; }
    public Image getImage() { return image; }
}
