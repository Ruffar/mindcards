package com.raffier.mindcards.service;

import com.raffier.mindcards.model.card.CardElement;
import com.raffier.mindcards.model.card.CardImagePair;
import com.raffier.mindcards.model.table.CardTable;
import com.raffier.mindcards.model.table.Image;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CardModelService {

    @Autowired
    MarkdownService markdownService;

    //Card elements
    public <S extends CardTable> CardElement<S> getCardElement(CardImagePair<S> cardPair) {
        S card = cardPair.getCard();
        Image image = cardPair.getImage();
        String unescapedDesc = markdownService.parsePlaintext(card.getDescription());

        return new CardElement<>(card, image, unescapedDesc);
    }

}
