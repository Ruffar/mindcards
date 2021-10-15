package com.raffier.mindcards.service;

import com.raffier.mindcards.model.card.CardElement;
import com.raffier.mindcards.model.table.CardTable;
import com.raffier.mindcards.model.table.Image;
import com.raffier.mindcards.model.table.Infocard;
import com.raffier.mindcards.model.table.Mindcard;
import com.raffier.mindcards.service.markdown.MarkdownService;
import javafx.util.Pair;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CardModelService {

    @Autowired
    MarkdownService markdownService;
    @Autowired
    CardService cardService;

    //Card elements
    public <S extends CardTable> CardElement<S> getCardElement(Pair<S,Image> card) {
        Image image = card;
        String imagePath = image != null ? image.getImagePath() : "";
        String unescapedDesc = markdownService.parsePlaintext(card.getDescription());

        return new CardElement<S>(card, image, unescapedDesc);
    }

    public CardElement<Mindcard> getMindcardElement(CardTable cardId) {
        return getCardElement(getMindcardEntity(cardId));
    }

    public CardElement<Infocard> getInfocardElement(int cardId) {
        return getCardElement(getInfocardEntity(cardId));
    }

}
