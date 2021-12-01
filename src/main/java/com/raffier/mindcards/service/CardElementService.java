package com.raffier.mindcards.service;

import com.raffier.mindcards.model.web.CardElement;
import com.raffier.mindcards.model.web.DeckElement;
import com.raffier.mindcards.model.table.*;
import com.raffier.mindcards.repository.table.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class CardElementService {

    @Autowired
    private CardUtilityService cardUtilityService;
    @Autowired
    private DeckService deckService;

    @Autowired
    private InfocardRepository infocardRepository;
    @Autowired
    private MindcardRepository mindcardRepository;
    @Autowired
    private CardGroupRepository cardGroupRepository;
    @Autowired
    private DeckRepository deckRepository;

    protected <T extends CardTable, S extends EntityRepository<T,Integer>> CardElement<T> getCardElement(User user, int cardId, S repository) {
        T card = repository.getById(cardId);
        Image image = cardUtilityService.getCardImage(card);
        return new CardElement<>(card,image);
    }

    public DeckElement getDeckElement(User user, int cardId) {
        Deck card = deckRepository.getById(cardId);
        Image image = cardUtilityService.getCardImage(card);
        boolean isFavourited = user != null && deckService.hasUserFavourited(cardId, user.getUserId());
        int totalFavourites = deckService.getTotalFavourites(cardId);
        return new DeckElement(card,image,isFavourited,totalFavourites);
    }

    protected  <T extends CardTable> List<CardElement<T>> getCardElementList(User user, List<T> list) {
        List<CardElement<T>> pairList = new ArrayList<>();
        for (T i: list) {
            Image image = cardUtilityService.getCardImage(i);
            pairList.add(new CardElement<>(i,image));
        }
        return pairList;
    }

    protected List<DeckElement> getDeckElementList(User user, List<Deck> list) {
        List<DeckElement> pairList = new ArrayList<>();
        for (Deck i: list) {
            int deckId = i.getDeckId();

            Image image = cardUtilityService.getCardImage(i);
            boolean isFavourited = user != null && deckService.hasUserFavourited(deckId, user.getUserId());
            int totalFavourites = deckService.getTotalFavourites(deckId);

            pairList.add(new DeckElement(i,image,isFavourited,totalFavourites));
        }
        return pairList;
    }

    // Getting Single Cards //

    public CardElement<Infocard> getInfocardElement(User user, int cardId) {
        return getCardElement(user,cardId,infocardRepository);
    }
    public CardElement<Mindcard> getMindcardElement(User user, int cardId) {
        return getCardElement(user,cardId,mindcardRepository);
    }
    public CardElement<CardGroup> getCardGroupElement(User user, int cardId) {
        return getCardElement(user,cardId,cardGroupRepository);
    }
    //GetDeckElement is already defined

    //  Getting multiple cards  //

    public List<CardElement<Infocard>> getInfocardsFromMindcard(User user, int mindcardId) {
        return getCardElementList(user, infocardRepository.getFromMindcard(mindcardId));
    }

    public List<CardElement<Mindcard>> getMindcardsFromCardGroup(User user, int cardGroupId) {
        return getCardElementList(user, mindcardRepository.getFromCardGroup(cardGroupId));
    }

    public List<CardElement<Mindcard>> getRandomMindcardsFromDeck(User user, int deckId, int amount) {
        return getCardElementList(user, mindcardRepository.getRandomFromDeck(deckId, amount));
    }

    public List<CardElement<CardGroup>> getRandomCardGroupsFromDeck(User user, int deckId, int amount) {
        return getCardElementList(user, cardGroupRepository.getRandomFromDeck(deckId, amount));
    }

    //Decks (Pages start at 0)
    public List<DeckElement> getDecksFromUser(User currentUser, User owner) {
        return getDeckElementList(currentUser, deckRepository.getUserDecks(owner.getUserId()));
    }

    public List<DeckElement> getDeckRandom(User user, int amount) {
        return getDeckElementList(user, deckRepository.getRandom(amount));
    }

    public List<DeckElement> getDeckNewest(User user, int amount, int page) {
        return getDeckElementList(user, deckRepository.getNewest(amount,amount*page));
    }

    public List<DeckElement> getDeckPopular(User user, int amount, int page) {
        return getDeckElementList(user, deckRepository.getPopular(amount,amount*page));
    }

    public List<DeckElement> getDeckOldestViewed(User user, int amount, int page) {
        return getDeckElementList(user, deckRepository.getOldestViewed(amount,amount*page));
    }

    //Deck Searching
    public List<DeckElement> searchDeck(User user, String searchString, int amount, int page) {
        return getDeckElementList(user, deckRepository.search(searchString,amount,amount*page));
    }

}
