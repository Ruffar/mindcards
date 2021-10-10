package com.raffier.mindcards.service;

import com.raffier.mindcards.errorHandling.EntityNotFoundException;
import com.raffier.mindcards.model.card.CardElement;
import com.raffier.mindcards.model.card.MindcardElements;
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

    private final MindcardRepository mindcardRepository;
    private final InfocardRepository infocardRepository;
    private final CardGroupRepository cardGroupRepository;
    private final CardPackRepository cardPackRepository;
    private final ImageRepository imageRepository;

    @Autowired
    public CardService(AppDatabase appDatabase) {
        mindcardRepository = new MindcardRepository(appDatabase);
        infocardRepository = new InfocardRepository(appDatabase);
        cardGroupRepository = new CardGroupRepository(appDatabase);
        cardPackRepository = new CardPackRepository(appDatabase);
        imageRepository = new ImageRepository(appDatabase);
    }

    //Generic entity getters with error handling
    private Mindcard getMindcardEntity(int cardId) {
        Mindcard card = mindcardRepository.getById(cardId);
        if (card == null) throw new EntityNotFoundException("Mindcard", cardId);
        return card;
    }

    private Infocard getInfocardEntity(int cardId) {
        Infocard card = infocardRepository.getById(cardId);
        if (card == null) throw new EntityNotFoundException("Mindcard", cardId);
        return card;
    }

    //Card elements
    public <S extends CardTable> CardElement<S> getCardElement(S card) {
        Image image = imageRepository.getFromCard(card);
        String imagePath = image != null ? image.getImagePath() : "";

        return new CardElement<S>(card, image);
    }

    public CardElement<Mindcard> getMindcardElement(int cardId) {
        return getCardElement(getMindcardEntity(cardId));
    }

    public CardElement<Infocard> getInfocardElement(int cardId) {
        return getCardElement(getInfocardEntity(cardId));
    }

    public void updateMindcard(int cardId, String title, int imageId, String description) {
        Mindcard card = getMindcardEntity(cardId);
        card.setTitle(title);
        card.setImageId(imageId);
        card.setDescription(description);
        mindcardRepository.save(card);
    }

    public void updateInfocard(int cardId, int imageId, String description) {
        Infocard card = getInfocardEntity(cardId);
        card.setImageId(imageId);
        card.setDescription(description);
        infocardRepository.save(card);
    }

    public MindcardElements getMindcardElements(int mindcardId) {
        Mindcard mindcard = mindcardRepository.getById(mindcardId);
        if (mindcard == null) throw new EntityNotFoundException("Mindcard", mindcardId);
        CardElement<Mindcard> mindcardElement = getCardElement(mindcard);

        List<Infocard> infocards = infocardRepository.getFromMindcard(mindcardId);
        List<CardElement<Infocard>> infoElements = new ArrayList<>();
        for (Infocard i: infocards) {
            CardElement<Infocard> element = getCardElement(i);
            infoElements.add(element);
        }

        return new MindcardElements(mindcardElement, infoElements);
    }

    public boolean isUserMindcardOwner(User user, int cardId) {
        if (user == null) return false;
        CardPack pack = cardPackRepository.getFromMindcard(cardId);
        return pack.getOwnerId() == user.getUserId();
    }

    public boolean isUserInfocardOwner(User user, int cardId) {
        if (user == null) return false;
        Mindcard card = mindcardRepository.getFromInfocard(cardId);
        CardPack pack = cardPackRepository.getFromMindcard(card.getMindcardId());
        return pack.getOwnerId() == user.getUserId();
    }

    public void updateMindcardElements(MindcardElements elements) {
        /*<input type="hidden" th:value="${cardElement.card.primaryId}" th:name="${fieldName}+'.card.primaryId'"/>
    <input type="hidden" th:value="${cardElement.image.imageId}" th:name="${fieldName}+'.image.imageId'"/>*/
        //Update mindcard
        Mindcard newMindcard = elements.getMindcard().getCardObject();
        Mindcard mindcard = mindcardRepository.getById(newMindcard.getMindcardId());
        mindcard.setTitle(newMindcard.getTitle());
        mindcard.setDescription(newMindcard.getDescription());
        mindcardRepository.save(mindcard);

        System.out.println(newMindcard.getMindcardId());

        //Update infocards
        for (CardElement<Infocard> infoElement : elements.getInfocards()) {
            Infocard newInfocard = infoElement.getCardObject();
            Infocard infocard = infocardRepository.getById(newInfocard.getInfocardId());
            infocard.setDescription(newMindcard.getDescription());
            infocardRepository.save(infoElement.getCardObject());
        }
    }

    public int getInfocardMindcardId(int infocardId) {
        return mindcardRepository.getFromInfocard(infocardId).getMindcardId();
    }

}
