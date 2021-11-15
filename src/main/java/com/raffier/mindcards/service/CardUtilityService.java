package com.raffier.mindcards.service;

import com.raffier.mindcards.errorHandling.EntityNotFoundException;
import com.raffier.mindcards.errorHandling.InvalidCardTypeException;
import com.raffier.mindcards.model.card.CardElement;
import com.raffier.mindcards.model.table.*;
import com.raffier.mindcards.repository.table.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class CardUtilityService {

    @Autowired
    UserRepository userRepository;
    @Autowired
    ImageRepository imageRepository;

    @Autowired
    InfocardRepository infocardRepository;
    @Autowired
    MindcardRepository mindcardRepository;
    @Autowired
    CardGroupRepository cardGroupRepository;
    @Autowired
    DeckRepository deckRepository;

    @Autowired
    FavouriteRepository favouriteRepository;

    public <T extends CardTable> Image getCardImage(T card) {
        if (card.getImageId() != null && card.getImageId() != 0) {
            try {
                return imageRepository.getFromCard(card);
            } catch(EntityNotFoundException e) {
                card.setImageId(0);
            }
        }
        return null;
    }

    //
    private CardRepository<?> getRepository(CardType cardType) {
        switch (cardType) {
            case INFOCARD:
                return infocardRepository;
            case MINDCARD:
                return mindcardRepository;
            case CARDGROUP:
                return cardGroupRepository;
            case DECK:
                return deckRepository;
            default:
                throw new InvalidCardTypeException(cardType);
        }
    }

    //Ownership
    public boolean isUserCardOwner(CardType cardType, User user, int cardId) {
        if (user == null) return false;
        return getRepository(cardType).isOwner(user,cardId);
    }

    //Existence
    public boolean cardExists(CardType cardType, int cardId) {
        //If a card does not exist, normally an entity not found exception is raised; however, we want to return false in this case
        try {
            return getRepository(cardType).getById(cardId) != null;
        } catch (EntityNotFoundException e) {
            return false;
        }
    }

}