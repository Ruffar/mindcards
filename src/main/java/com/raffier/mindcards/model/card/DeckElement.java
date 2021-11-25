package com.raffier.mindcards.model.card;

import com.raffier.mindcards.model.table.CardTable;
import com.raffier.mindcards.model.table.Deck;
import com.raffier.mindcards.model.table.Image;

public class DeckElement extends CardElement<Deck> {

    private final boolean isFavourited;
    private final int totalFavourites;

    public DeckElement(Deck card, Image image, boolean isFavourited, int totalFavourites) {
        super(card, image);
        this.isFavourited = isFavourited;
        this.totalFavourites = totalFavourites;
    }

    public boolean isFavourited() { return isFavourited; }
    public int getTotalFavourites() { return totalFavourites; }

}
