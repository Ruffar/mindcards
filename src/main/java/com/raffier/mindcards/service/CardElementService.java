package com.raffier.mindcards.service;

import com.raffier.mindcards.model.card.CardElement;
import com.raffier.mindcards.model.table.*;
import com.raffier.mindcards.repository.table.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class CardElementService {

    @Autowired
    CardUtilityService cardUtilityService;

    @Autowired
    InfocardRepository infocardRepository;
    @Autowired
    MindcardRepository mindcardRepository;
    @Autowired
    CardGroupRepository cardGroupRepository;
    @Autowired
    DeckRepository deckRepository;

    protected <T extends CardTable, S extends EntityRepository<T,Integer>> CardElement<T> getCardElement(User user, int cardId, S repository) {
        T card = repository.getById(cardId);
        Image image = cardUtilityService.getCardImage(card);
        boolean isOwner = cardUtilityService.isUserCardOwner(card.getCardType(),user,cardId);
        return new CardElement<>(card,image,isOwner);
    }

    protected  <T extends CardTable> List<CardElement<T>> getCardElementList(User user, List<T> list) {
        List<CardElement<T>> pairList = new ArrayList<>();
        for (T i: list) {
            Image image = cardUtilityService.getCardImage(i);
            boolean isOwner = cardUtilityService.isUserCardOwner(i.getCardType(),user,i.getPrimaryKey());
            pairList.add(new CardElement<>(i,image,isOwner));
        }
        return pairList;
    }

    //Get card with image
    public CardElement<Infocard> getInfocardElement(User user, int cardId) {
        return getCardElement(user,cardId,infocardRepository);
    }
    public CardElement<Mindcard> getMindcardElement(User user, int cardId) {
        return getCardElement(user,cardId,mindcardRepository);
    }
    public CardElement<CardGroup> getCardGroupElement(User user, int cardId) {
        return getCardElement(user,cardId,cardGroupRepository);
    }
    public CardElement<Deck> getDeckElement(User user, int cardId) {
        return getCardElement(user,cardId,deckRepository);
    }

    //Getting multiple cards
    public List<CardElement<Infocard>> getInfocardsFromMindcard(User user, int mindcardId) {
        return getCardElementList(user, infocardRepository.getFromMindcard(mindcardId));
    }
    public List<CardElement<Mindcard>> getRandomMindcardsFromDeck(User user, int deckId, int amount) {
        return getCardElementList(user, mindcardRepository.getRandomFromDeck(deckId, amount));
    }

}
