package com.raffier.mindcards.service;

import com.raffier.mindcards.model.card.CardElement;
import com.raffier.mindcards.model.table.Deck;
import com.raffier.mindcards.model.table.Favourite;
import com.raffier.mindcards.repository.table.DeckRepository;
import com.raffier.mindcards.repository.table.FavouriteRepository;
import com.raffier.mindcards.repository.table.ImageRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

@Service
public class DeckService {

    @Autowired
    CardUtilityService cardUtilityService;

    @Autowired
    private FavouriteRepository favouriteRepository;
    @Autowired
    private DeckRepository deckRepository;

    //Favourites
    public int getDeckFavourites(int deckId) {
        if (deckRepository.getById(deckId) == null) return 0; //Will throw EntityNotFoundException if deck is not found; if not thrown, the function will return 0

        return favouriteRepository.getTotalDeckFavourites(deckId);
    }

    public void addFavourite(int deckId, int userId) {
        favouriteRepository.add(new Favourite(deckId, userId));
    }

    public void removeFavourite(int deckId, int userId) {
        favouriteRepository.delete(new Favourite(deckId, userId));
    }

    //Sorting
    //Pages start at 0
    public List<CardElement<Deck>> getRandom(int amount) {
        return cardUtilityService.getCardElementList(deckRepository.getRandom(amount));
    }

    public List<CardElement<Deck>> getPopular(int amount, int page) {
        return cardUtilityService.getCardElementList(deckRepository.getPopular(amount,amount*page));
    }

    //Deck Searching
    public List<CardElement<Deck>> searchDeck(String searchString) {
        return cardUtilityService.getCardElementList(deckRepository.search(searchString,10,10));
    }

}
