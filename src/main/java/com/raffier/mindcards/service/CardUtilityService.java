package com.raffier.mindcards.service;

import com.raffier.mindcards.errorHandling.EntityNotFoundException;
import com.raffier.mindcards.errorHandling.InvalidCardTypeException;
import com.raffier.mindcards.model.table.*;
import com.raffier.mindcards.repository.table.*;
import com.raffier.mindcards.util.CardType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CardUtilityService {

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

    public <T extends CardTable> Image getCardImage(T card) {
        if (card.getImageId() != null && card.getImageId() != 0) { //Check whether card has an image that is not deleted
            return imageRepository.getFromCard(card);
        }
        return null;
    }

    //Getting correct repository class from a CardType
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
