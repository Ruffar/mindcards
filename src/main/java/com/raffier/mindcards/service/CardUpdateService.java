package com.raffier.mindcards.service;

import com.raffier.mindcards.errorHandling.EntityNotFoundException;
import com.raffier.mindcards.errorHandling.InvalidHyperlinkException;
import com.raffier.mindcards.model.card.CardElement;
import com.raffier.mindcards.model.table.*;
import com.raffier.mindcards.repository.AppDatabase;
import com.raffier.mindcards.repository.table.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ResourceLoader;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service
public class CardUpdateService {

    @Autowired
    private CardUtilityService cardUtilityService;

    @Autowired
    private MindcardRepository mindcardRepository;
    @Autowired
    private InfocardRepository infocardRepository;
    @Autowired
    private CardGroupRepository cardGroupRepository;
    @Autowired
    private DeckRepository deckRepository;
    @Autowired
    private ImageRepository imageRepository;

    private Path imageDirectory;

    @Autowired
    private CardUpdateService(AppDatabase appDatabase, ResourceLoader resourceLoader) {
        try {
            File directoryFile = new File(resourceLoader.getResource("classpath:/").getURI().resolve("static/images/card/")); //Get the directory
            directoryFile.mkdirs(); //Create parent directories if they don't exist
            imageDirectory = Paths.get(directoryFile.getPath());
        } catch(IOException e) {
            System.out.println(e.getMessage());
        }
    }

    //Images

    /*
    Saves the image into the database but DOES NOT save the card
     */
    public <T extends CardTable> void setCardImage(T card, MultipartFile file) {

        Image image = cardUtilityService.getCardImage(card);
        if (image == null && !file.isEmpty()) {
            image = imageRepository.add( new Image(0,"","") );
            card.setImageId(image.getImageId());
            image.setImagePath("/images/card/"+image.getImageId() + ".png");
            imageRepository.save(image);
        }

        if (image != null) {
            try {
                Path path = imageDirectory.resolve(image.getImageId() + ".png");
                if (!file.isEmpty()) {
                    Files.copy(file.getInputStream(), path, StandardCopyOption.REPLACE_EXISTING);
                } else {
                    Files.deleteIfExists(path);
                    card.setImageId(0);
                    imageRepository.delete(image);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

    }

    //Update card main information
    private <T extends CardTable> void updateCard(T card) {
        switch(card.getCardType()) {
            case INFOCARD:
                infocardRepository.save((Infocard) card);
                break;
            case MINDCARD:
                mindcardRepository.save((Mindcard) card);
                break;
            case CARDGROUP:
                cardGroupRepository.save((CardGroup) card);
                break;
            case DECK:
                deckRepository.save((Deck) card);
                break;
        }
    }

    public void updateMindcard(int cardId, String title, MultipartFile image, String description) {
        Mindcard card = mindcardRepository.getById(cardId);
        card.setTitle(title);
        setCardImage(card,image);
        card.setDescription(description);
        mindcardRepository.save(card);
    }

    public void updateInfocard(int cardId, MultipartFile image, String description) {
        Infocard card = infocardRepository.getById(cardId);
        setCardImage(card,image);
        card.setDescription(description);
        infocardRepository.save(card);
    }

    //Add cards
    private <T extends CardTable, S extends EntityRepository<T,Integer>> T addCard(T newCard, MultipartFile image, S repository) {
        T card = repository.add(newCard);
        setCardImage(card,image);
        repository.save(card);
        return card;
    }

    public Mindcard addMindcard(int deckId, String title, MultipartFile image, String description) {
        return addCard( new Mindcard(0,deckId,title,0,description), image, mindcardRepository);
    }
    public Infocard addInfocard(int mindcardId, MultipartFile image, String description) {
        return addCard( new Infocard(0,mindcardId,0,description), image, infocardRepository);
    }
    public CardGroup addCardGroup(int deckId, String title, MultipartFile image, String description) {
        return addCard( new CardGroup(0,deckId, title,0,description), image, cardGroupRepository);
    }
    public Deck addDeck(int ownerId, String title, MultipartFile image, String description) {
        return addCard( new Deck(0,ownerId,title,0,description), image, deckRepository);
    }

    //Delete cards
    private <T extends CardTable, S extends EntityRepository<T,Integer>> void deleteCard(int cardId, S repository) {
        T card = repository.getById(cardId);
        Image image = cardUtilityService.getCardImage(card);
        if (image != null) {
            imageRepository.delete(image);
        }
        repository.delete(card);
    }

    public void deleteMindcard(int cardId) {
        deleteCard(cardId,mindcardRepository);
    }
    public void deleteInfocard(int cardId) {
        deleteCard(cardId,infocardRepository);
    }
    public void deleteCardGroup(int cardId) {
        deleteCard(cardId,cardGroupRepository);
    }
    public void deleteDeck(int cardId) {
        deleteCard(cardId,deckRepository);
    }

    //Hyperlink is valid
    public boolean areHyperlinksValid(String plainText) {
        Pattern pattern = Pattern.compile("\\[(.+?)]\\((.+?)\\)");
        Matcher matcher = pattern.matcher(plainText);
        while (matcher.find()) {

            String cardPath = matcher.group(2);
            String[] cardPathVariables = cardPath.split("/");
            if (cardPathVariables.length == 2 && cardPathVariables[0].matches("mindcard|group|deck") && cardPathVariables[1].matches("[0-9]+")) {

                CardType cardType = CardType.getCardTypeFromString(cardPathVariables[0]);
                int cardId = Integer.parseInt(cardPathVariables[1]);

                if (!cardUtilityService.cardExists(cardType,cardId)) {
                    throw new InvalidHyperlinkException(cardPath);
                }

            } else {
                throw new InvalidHyperlinkException(cardPath);
            }

        }

        return true;
    }

}
