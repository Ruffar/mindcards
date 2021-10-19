package com.raffier.mindcards.model.card;

import com.raffier.mindcards.model.table.CardTable;
import com.raffier.mindcards.model.table.Image;
import com.raffier.mindcards.model.table.TitledCardTable;

public class CardImagePair<T extends CardTable> {

    private T card;
    private Image image;

    public CardImagePair() {}

    public CardImagePair(T card, Image image) {
        this.card = card;
        this.image = image;
    }

    public boolean hasTitle() { return (card instanceof TitledCardTable); }

    public T getCard() { return this.card; }
    public Image getImage() { return this.image; }

    public void setCard(T card) { this.card = card; }
    public void setImage(Image image) { this.image = image; }

}
