package com.raffier.mindcards.service;

import com.raffier.mindcards.model.card.CardImagePair;
import com.raffier.mindcards.model.table.*;
import com.raffier.mindcards.repository.AppDatabase;
import com.raffier.mindcards.repository.table.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class CardService {

    private AppDatabase database;

    @Autowired
    MarkdownService markdownService;

    private final MindcardRepository mindcardRepository;
    private final InfocardRepository infocardRepository;
    private final CardGroupRepository cardGroupRepository;
    private final CardPackRepository cardPackRepository;
    private final ImageRepository imageRepository;

    @Autowired
    private CardService(AppDatabase appDatabase) {
        mindcardRepository = new MindcardRepository(appDatabase);
        infocardRepository = new InfocardRepository(appDatabase);
        cardGroupRepository = new CardGroupRepository(appDatabase);
        cardPackRepository = new CardPackRepository(appDatabase);
        imageRepository = new ImageRepository(appDatabase);
    }

    //Get card with image
    private <T extends CardTable> CardImagePair<T> getCardImagePair(T card) {
        int imageId = card.getImageId();
        Image image = imageId != 0 ? imageRepository.getFromCard(card) : null;
        return new CardImagePair<>(card,image);
    }

    public CardImagePair<Infocard> getInfocardImagePair(int cardId) {
        return getCardImagePair(infocardRepository.getById(cardId));
    }

    public CardImagePair<Mindcard> getMindcardImagePair(int cardId) {
        return getCardImagePair(mindcardRepository.getById(cardId));
    }

    public CardImagePair<CardGroup> getCardGroupImagePair(int cardId) {
        return getCardImagePair(cardGroupRepository.getById(cardId));
    }

    public CardImagePair<CardPack> getCardPackImagePair(int cardId) {
        return getCardImagePair(cardPackRepository.getById(cardId));
    }

    //
    public List<CardImagePair<Infocard>> getInfocardsFromMindcard(int mindcardId) {
        List<Infocard> infocards = infocardRepository.getFromMindcard(mindcardId);
        List<CardImagePair<Infocard>> infoPairs = new ArrayList<>();
        for (Infocard i: infocards) {
            int imageId = i.getImageId();
            Image image = imageId != 0 ? imageRepository.getFromCard(i) : null;
            infoPairs.add(new CardImagePair<>(i,image));
        }
        return infoPairs;
    }

    //Update cards
    public void updateMindcard(int cardId, String title, int imageId, String description) {
        Mindcard card = mindcardRepository.getById(cardId);
        card.setTitle(title);
        card.setImageId(imageId);
        card.setDescription(description);
        mindcardRepository.save(card);
    }

    public void updateInfocard(int cardId, int imageId, String description) {
        Infocard card = infocardRepository.getById(cardId);
        card.setImageId(imageId);
        card.setDescription(description);
        infocardRepository.save(card);
    }

    public boolean isUserCardOwner(CardType cardType, User user, int cardId) {
        switch(cardType) {
            case INFOCARD:
                return isUserCardOwner(CardType.MINDCARD,user,infocardRepository.getById(cardId).getMindcardId());
            case MINDCARD:
                return isUserCardOwner(CardType.CARDPACK,user,mindcardRepository.getById(cardId).getPackId());
            case CARDGROUP:
                return isUserCardOwner(CardType.CARDPACK,user,cardGroupRepository.getById(cardId).getPackId());
            case CARDPACK:
                if (user == null) return false;
                return cardPackRepository.getById(cardId).getOwnerId() == user.getUserId();
            default:
                return false;
        }
    }

    //Exist
    public boolean infocardExists(int cardId) {
        return infocardRepository.getById(cardId) != null;
    }

    public boolean mindcardExists(int cardId) {
        return mindcardRepository.getById(cardId) != null;
    }

    public boolean cardGroupExists(int cardId) {
        return cardGroupRepository.getById(cardId) != null;
    }

    public boolean cardPackExists(int cardId) {
        return cardPackRepository.getById(cardId) != null;
    }

    public boolean cardExists(CardType cardType, int cardId) {
        switch (cardType) {
            case INFOCARD:
                return infocardRepository.getById(cardId) != null;
            case MINDCARD:
                return mindcardRepository.getById(cardId) != null;
            case CARDGROUP:
                return cardGroupRepository.getById(cardId) != null;
            case CARDPACK:
                return cardPackRepository.getById(cardId) != null;
            default:
                return false;
        }
    }

    public int getInfocardMindcardId(int infocardId) {
        return mindcardRepository.getFromInfocard(infocardId).getMindcardId();
    }

}
