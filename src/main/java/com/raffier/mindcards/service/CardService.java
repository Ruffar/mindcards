package com.raffier.mindcards.service;

import com.raffier.mindcards.errorHandling.EntityNotFoundException;
import com.raffier.mindcards.model.CardElement;
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

    public <S extends CardTable> CardElement getCardElement(S card) {
        String title = (card instanceof TitledCardTable) ? ((TitledCardTable) card).getTitle() : null;

        Image image = imageRepository.getFromCard(card);
        String imagePath = image != null ? image.getImagePath() : "";

        return new CardElement(card.getPrimaryKey(),title,imagePath,card.getDescription());
    }

    public CardElement getMindcardElement(int mindcardId) {
        Mindcard card = mindcardRepository.getById(mindcardId);
        if (card == null) throw new EntityNotFoundException("Mindcard", mindcardId);
        return getCardElement(card);
    }

    public List<CardElement> getInfocardElements(int mindcardId) {
        List<Infocard> infocards = infocardRepository.getFromMindcard(mindcardId);
        List<CardElement> infoElements = new ArrayList<>();
        for (Infocard i: infocards) {
            CardElement element = getCardElement(i);
            infoElements.add(element);
        }
        return infoElements;
    }

    public boolean isUserMindcardOwner(User user, int mindcardId) {
        if (user == null) return false;
        Mindcard card = mindcardRepository.getById(mindcardId);
        CardPack cardPack = cardPackRepository.getById(card.getPackId());
        return cardPack.getOwnerId() == user.getUserId();
    }

    public void updateMindcard(CardElement element, int mindcardId) {
        System.out.println(element);
        Mindcard mindcard = mindcardRepository.getById(mindcardId);
        mindcard.setTitle(element.getTitle());
        mindcard.setDescription(element.getDescription());
        mindcardRepository.save(mindcard);
    }

    public void updateInfocard(CardElement element, int infocardId) {
        Infocard infocard = infocardRepository.getById(infocardId);
        infocard.setDescription(element.getDescription());
        infocardRepository.save(infocard);
    }

    public int getInfocardMindcardId(int infocardId) {
        return mindcardRepository.getFromInfocard(infocardId).getMindcardId();
    }

}
