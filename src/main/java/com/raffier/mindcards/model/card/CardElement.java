package com.raffier.mindcards.model.card;

import com.raffier.mindcards.model.table.CardTable;
import com.raffier.mindcards.model.table.Image;
import com.raffier.mindcards.model.table.TitledCardTable;

import java.io.Serializable;

public class CardElement<T extends CardTable> implements Serializable {

    private final T card;
    private final Image image;

    private final boolean isOwned;
    private final boolean isFavourited;
    private final int totalFavourites;

    public CardElement(T card, Image image, boolean isOwned, boolean isFavourited, int totalFavourites) {
        this.card = card;
        this.image = image;
        this.isOwned = isOwned;
        this.isFavourited = isFavourited;
        this.totalFavourites = totalFavourites;
    }

    public CardElement(T card, Image image, boolean isOwned) {
        this(card,image,isOwned,false,0);
    }

    public T getCard() { return card; }
    public Image getImage() { return image; }

    public boolean hasTitle() { return card instanceof TitledCardTable; }

    public String getDescription() { return card.getDescription(); }

    public String getTitle() {
        if (card instanceof TitledCardTable) {
            return ((TitledCardTable) card).getTitle();
        }
        return null;
    }

    public String getImagePath() { return image != null ? image.getImagePath() : ""; }


    public boolean isOwned() { return isOwned; }
    public boolean isFavourited() { return isFavourited; }
    public int getTotalFavourites() { return totalFavourites; }

}
