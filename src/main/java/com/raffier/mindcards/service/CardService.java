package com.raffier.mindcards.service;

import com.raffier.mindcards.errorHandling.EntityNotFoundException;
import com.raffier.mindcards.model.card.CardElement;
import com.raffier.mindcards.model.card.MindcardElements;
import com.raffier.mindcards.model.table.*;
import com.raffier.mindcards.repository.AppDatabase;
import com.raffier.mindcards.repository.table.*;
import com.raffier.mindcards.service.markdown.MarkdownService;
import javafx.util.Pair;
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
    public Pair<Mindcard,Image> getMindcardImage(int cardId) {
        Mindcard card = mindcardRepository.getById(cardId);
        Image image = imageRepository.getFromCard(card);
        return new Pair<>(card,image);
    }

    public Pair<Infocard,Image> getInfocardImage(int cardId) {
        Infocard card = infocardRepository.getById(cardId);
        Image image = imageRepository.getFromCard(card);
        return new Pair<>(card,image);
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

    public int getInfocardMindcardId(int infocardId) {
        return mindcardRepository.getFromInfocard(infocardId).getMindcardId();
    }

}
