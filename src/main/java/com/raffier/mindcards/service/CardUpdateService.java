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
import java.sql.SQLException;
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
    @Autowired
    private UserRepository userRepository;

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
    public <T extends CardTable> void setCardImage(T card, ImageUpdate imageUpdate) throws SQLException {

        Image image = cardUtilityService.getCardImage(card);

        //If there is no image entity for the card and it's going to be changed to a new one, create an image entity
        if (image == null && imageUpdate.getChangeType() != ImageChangeType.REMOVE && imageUpdate.getChangeType() != ImageChangeType.NONE) {
            image = imageRepository.add(new Image(0, "")); //ImageID is automatically set by repository
            card.setImageId(image.getImageId()); //Set the card's imageID to the new entity
        }

        //If there is an image to be changed, change it
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
                    card.setImageId(0); //Set card's image to 0 (deleted image entity)
                    imageRepository.delete(image);
                }
            } catch (IOException e) {
                throw new ImageChangeException();
            }
        }
    }

    //Update card main information
    public void updateDeck(int cardId, String title, ImageUpdate imageUpdate, String description) throws SQLException {
        Deck card = deckRepository.getById(cardId);
        card.setTitle(title);
        setCardImage(card,imageUpdate);
        card.setDescription(description);
        deckRepository.save(card);
    }

    public void updateCardGroup(int cardId, String title, ImageUpdate imageUpdate, String description) throws SQLException {
        CardGroup card = cardGroupRepository.getById(cardId);
        card.setTitle(title);
        setCardImage(card,imageUpdate);
        card.setDescription(description);
        cardGroupRepository.save(card);
    }

    public void updateMindcard(int cardId, String title, ImageUpdate imageUpdate, String description) throws SQLException {
        Mindcard card = mindcardRepository.getById(cardId);
        card.setTitle(title);
        setCardImage(card,imageUpdate);
        card.setDescription(description);
        mindcardRepository.save(card);
    }

    public void updateInfocard(int cardId, ImageUpdate imageUpdate, String description) throws SQLException {
        Infocard card = infocardRepository.getById(cardId);
        setCardImage(card,imageUpdate);
        card.setDescription(description);
        infocardRepository.save(card);
    }

    //Add cards
    private <T extends CardTable, S extends EntityRepository<T,Integer>> T addCard(T newCard, S repository) throws SQLException {
        T card = repository.add(newCard);
        repository.save(card);
        return card;
    }

    public Mindcard addMindcard(int deckId) throws SQLException {
        deckRepository.getById(deckId);
        return addCard( new Mindcard(0,deckId,"New Mindcard",0,"Description"), mindcardRepository);
    }
    public Infocard addInfocard(int mindcardId) throws SQLException {
        mindcardRepository.getById(mindcardId);
        return addCard( new Infocard(0,mindcardId,0,"Description"), infocardRepository);
    }
    public CardGroup addCardGroup(int deckId) throws SQLException {
        deckRepository.getById(deckId);
        return addCard( new CardGroup(0,deckId, "New Group",0,"Description"), cardGroupRepository);
    }
    public Deck addDeck(int ownerId) throws SQLException {
        userRepository.getById(ownerId);
        return addCard( new Deck(0,ownerId,"New Deck",0,"Description"), deckRepository);
    }

    //Delete cards
    private <T extends CardTable, S extends EntityRepository<T,Integer>> void deleteCard(int cardId, S repository) throws SQLException {
        T card = repository.getById(cardId);
        Image image = cardUtilityService.getCardImage(card);
        if (image != null) {
            imageRepository.delete(image); //Delete image if card has one
        }
        repository.delete(card);
    }

    public void deleteMindcard(int cardId) throws SQLException {
        deleteCard(cardId,mindcardRepository);
    }
    public void deleteInfocard(int cardId) throws SQLException {
        deleteCard(cardId,infocardRepository);
    }
    public void deleteCardGroup(int cardId) throws SQLException {
        deleteCard(cardId,cardGroupRepository);
    }
    public void deleteDeck(int cardId) throws SQLException {
        deleteCard(cardId,deckRepository);
    }

    //Hyperlink validation
    public boolean areHyperlinksValid(String plainText) {
        Pattern pattern = Pattern.compile("\\[(.+?)]\\((.+?)\\)");
        Matcher matcher = pattern.matcher(plainText);
        while (matcher.find()) {

            String cardPath = matcher.group(2); //Get url path of card
            String[] cardPathVariables = cardPath.split("/"); //Split path
            //Proper URL path consists of one of the card types followed by its ID which should only be an integer number (e.g. 'mindcard/3')
            if (cardPathVariables.length == 2 && cardPathVariables[0].matches("mindcard|group|deck") && cardPathVariables[1].matches("[0-9]+")) {

                CardType cardType = CardType.getCardTypeFromString(cardPathVariables[0]);
                int cardId = Integer.parseInt(cardPathVariables[1]); //Convert the written ID to Integer

                if (!cardUtilityService.cardExists(cardType,cardId)) {
                    throw new InvalidHyperlinkException(cardPath); //If card doesn't exist then raise error
                }

            } else {
                throw new InvalidHyperlinkException(cardPath); //Invalid URL Path format
            }
        }

        return true;
    }

}
