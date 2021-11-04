package com.raffier.mindcards.service;

import com.raffier.mindcards.errorHandling.EntityNotFoundException;
import com.raffier.mindcards.model.card.CardElement;
import com.raffier.mindcards.model.table.CardTable;
import com.raffier.mindcards.model.table.Image;
import com.raffier.mindcards.repository.table.EntityRepository;
import com.raffier.mindcards.repository.table.ImageRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class CardUtilityService {

    @Autowired
    ImageRepository imageRepository;

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

    public <T extends CardTable, S extends EntityRepository<T,Integer>> CardElement<T> getCardElement(int cardId, S repository) {
        T card = repository.getById(cardId);
        Image image = getCardImage(card);
        return new CardElement<>(card,image);
    }

    public  <T extends CardTable> List<CardElement<T>> getCardElementList(List<T> list) {
        List<CardElement<T>> pairList = new ArrayList<>();
        for (T i: list) {
            Image image = getCardImage(i);
            pairList.add(new CardElement<>(i,image));
        }
        return pairList;
    }

}
