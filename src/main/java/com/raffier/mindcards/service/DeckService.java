package com.raffier.mindcards.service;

import com.raffier.mindcards.model.table.Favourite;
import com.raffier.mindcards.repository.table.DeckRepository;
import com.raffier.mindcards.repository.table.FavouriteRepository;
import com.raffier.mindcards.repository.table.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.SQLException;

@Service
public class DeckService {

    @Autowired
    private FavouriteRepository favouriteRepository;
    @Autowired
    private DeckRepository deckRepository;
    @Autowired
    private UserRepository userRepository;

    //Favourites
    public void addFavourite(int deckId, int userId) throws SQLException {
        deckRepository.getById(deckId);
        userRepository.getById(userId);
        favouriteRepository.add(new Favourite(deckId, userId));
    }

    public void removeFavourite(int deckId, int userId) throws SQLException {
        favouriteRepository.deleteById(new Favourite(deckId, userId));
    }

    public boolean hasUserFavourited(int deckId, int userId) throws SQLException {
        deckRepository.getById(deckId);
        userRepository.getById(userId);
        return favouriteRepository.exists(new Favourite(deckId,userId));
    }

    public void updateLastViewed(int deckId, int userId) throws SQLException {
        //Saves the favourite again, with its time set to the current time in the constructor used
        favouriteRepository.save(new Favourite(deckId, userId));
    }

    public int getTotalFavourites(int deckId) throws SQLException {
        deckRepository.getById(deckId);
        return favouriteRepository.getTotalDeckFavourites(deckId);
    }
}
