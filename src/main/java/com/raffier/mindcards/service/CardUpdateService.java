package com.raffier.mindcards.service;

import com.raffier.mindcards.errorHandling.InvalidHyperlinkException;
import com.raffier.mindcards.model.table.*;
import com.raffier.mindcards.repository.table.*;
import com.raffier.mindcards.util.CardType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.sql.Date;
import java.time.Instant;
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
    public <T extends CardTable> void setCardImage(T card, MultipartFile file) {

        Image image = cardUtilityService.getCardImage(card);
        if (image == null && !file.isEmpty()) {
            image = imageRepository.add( new Image(0,"") );
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
