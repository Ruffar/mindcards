package com.raffier.mindcards.service;

import com.raffier.mindcards.errorHandling.ImageChangeException;
import com.raffier.mindcards.errorHandling.InvalidHyperlinkException;
import com.raffier.mindcards.model.table.*;
import com.raffier.mindcards.model.web.ImageFileUpdate;
import com.raffier.mindcards.model.web.ImageUpdate;
import com.raffier.mindcards.model.web.ImageUrlUpdate;
import com.raffier.mindcards.repository.table.*;
import com.raffier.mindcards.util.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
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

    private final Path imageDirectory;

    @Autowired
    private CardUpdateService(@Value("${dynamicContentDirectory}") String dirPath) {
        File directoryFile = new File(dirPath+"/images/"); //Get the directory
        directoryFile.mkdirs(); //Create images folder if it doesn't exist
        imageDirectory = Paths.get(directoryFile.getPath());
    }

    //Images

    /*
    Saves the image into the database but DOES NOT save the card
     */
    public <T extends CardTable> void setCardImage(T card, ImageUpdate imageUpdate) {

        Image image = cardUtilityService.getCardImage(card);
        if (image == null && imageUpdate.getChangeType() != ImageChangeType.REMOVE && imageUpdate.getChangeType() != ImageChangeType.NONE) {
            image = imageRepository.add(new Image(0, ""));
            card.setImageId(image.getImageId());
        }

        if (image != null) {

            try {
                Path path = imageDirectory.resolve(image.getImageId() + ".png");
                //Update file
                if (imageUpdate instanceof ImageFileUpdate) {
                    MultipartFile file = ((ImageFileUpdate) imageUpdate).getFile();
                    Files.copy(file.getInputStream(), path, StandardCopyOption.REPLACE_EXISTING);
                    image.setImagePath("/cardImages/" + image.getImageId() + ".png");
                    imageRepository.save(image);

                //Update URL
                } else if (imageUpdate instanceof ImageUrlUpdate) {
                    Files.deleteIfExists(path);
                    image.setImagePath( ((ImageUrlUpdate) imageUpdate).getUrl() );
                    imageRepository.save(image);

                //Remove image
                } else if (imageUpdate.getChangeType() == ImageChangeType.REMOVE) {
                    Files.deleteIfExists(path);
                    card.setImageId(0);
                    imageRepository.delete(image);
                }
            } catch (IOException e) {
                throw new ImageChangeException();
            }
        }

    }

    //Update card main information
    public void updateDeck(int cardId, String title, ImageUpdate imageUpdate, String description) {
        Deck card = deckRepository.getById(cardId);
        card.setTitle(title);
        setCardImage(card,imageUpdate);
        card.setDescription(description);
        deckRepository.save(card);
    }

    public void updateCardGroup(int cardId, String title, ImageUpdate imageUpdate, String description) {
        CardGroup card = cardGroupRepository.getById(cardId);
        card.setTitle(title);
        setCardImage(card,imageUpdate);
        card.setDescription(description);
        cardGroupRepository.save(card);
    }

    public void updateMindcard(int cardId, String title, ImageUpdate imageUpdate, String description) {
        Mindcard card = mindcardRepository.getById(cardId);
        card.setTitle(title);
        setCardImage(card,imageUpdate);
        card.setDescription(description);
        mindcardRepository.save(card);
    }

    public void updateInfocard(int cardId, ImageUpdate imageUpdate, String description) {
        Infocard card = infocardRepository.getById(cardId);
        setCardImage(card,imageUpdate);
        card.setDescription(description);
        infocardRepository.save(card);
    }

    //Add cards
    private <T extends CardTable, S extends EntityRepository<T,Integer>> T addCard(T newCard, S repository) {
        T card = repository.add(newCard);
        repository.save(card);
        return card;
    }

    public Mindcard addMindcard(int deckId) {
        return addCard( new Mindcard(0,deckId,"New Mindcard",0,"Description"), mindcardRepository);
    }
    public Infocard addInfocard(int mindcardId) {
        return addCard( new Infocard(0,mindcardId,0,"Description"), infocardRepository);
    }
    public CardGroup addCardGroup(int deckId) {
        return addCard( new CardGroup(0,deckId, "New Group",0,"Description"), cardGroupRepository);
    }
    public Deck addDeck(int ownerId) {
        return addCard( new Deck(0,ownerId,"New Deck",0,"Description"), deckRepository);
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
