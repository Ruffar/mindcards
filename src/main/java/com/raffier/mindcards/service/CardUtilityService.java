package com.raffier.mindcards.service;

import com.raffier.mindcards.errorHandling.EntityNotFoundException;
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
    //Ownership
    public boolean isUserCardOwner(CardType cardType, User user, int cardId) {
        switch(cardType) {
            case INFOCARD:
                return isUserCardOwner(CardType.MINDCARD,user,infocardRepository.getById(cardId).getMindcardId());
            case MINDCARD:
                return isUserCardOwner(CardType.DECK,user,mindcardRepository.getById(cardId).getDeckId());
            case CARDGROUP:
                return isUserCardOwner(CardType.DECK,user,cardGroupRepository.getById(cardId).getDeckId());
            case DECK:
                if (user == null) return false;
                return deckRepository.getById(cardId).getOwnerId() == user.getUserId();
            default:
                return false;
        }
    }

    //Existence
    public <T extends EntityRepository<?,Integer>> boolean cardExists(CardType cardType, int cardId) {
        switch (cardType) {
            case INFOCARD:
                return infocardRepository.getById(cardId) != null;
            case MINDCARD:
                return mindcardRepository.getById(cardId) != null;
            case CARDGROUP:
                return cardGroupRepository.getById(cardId) != null;
            case DECK:
                return deckRepository.getById(cardId) != null;
            default:
                return false;
        }
    }

}
