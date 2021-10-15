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

    //Card elements
    public <S extends CardTable> CardElement<S> getCardElement(Pair<S,Image> cardPair) {
        S card = cardPair.getKey();
        Image image = cardPair.getValue();
        String unescapedDesc = markdownService.parsePlaintext(card.getDescription());

        return new CardElement<S>(card, image, unescapedDesc);
    }

}
