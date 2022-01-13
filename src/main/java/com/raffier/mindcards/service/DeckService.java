package com.raffier.mindcards.service;

import com.raffier.mindcards.model.table.Favourite;
import com.raffier.mindcards.repository.table.DeckRepository;
import com.raffier.mindcards.repository.table.FavouriteRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class DeckService {

    @Autowired
    private FavouriteRepository favouriteRepository;
    @Autowired
    private DeckRepository deckRepository;

    //Favourites
    public void addFavourite(int deckId, int userId) {
        favouriteRepository.add(new Favourite(deckId, userId));
    }

    public void removeFavourite(int deckId, int userId) {
        favouriteRepository.delete(new Favourite(deckId, userId));
    }

    public boolean hasUserFavourited(int deckId, int userId) {
        return favouriteRepository.exists(new Favourite(deckId,userId));
    }

    public void updateLastViewed(int deckId, int userId) {
        //Saves the favourite again, with its time set to the current time in the constructor used
        favouriteRepository.save(new Favourite(deckId, userId));
    }

    public int getTotalFavourites(int deckId) {
        return favouriteRepository.getTotalDeckFavourites(deckId);
    }


}
