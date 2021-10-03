package com.raffier.mindcards.service;

import com.raffier.mindcards.AppConfig;
import com.raffier.mindcards.model.CardElement;
import com.raffier.mindcards.model.table.Image;
import com.raffier.mindcards.model.table.Infocard;
import com.raffier.mindcards.model.table.Mindcard;
import com.raffier.mindcards.repository.table.*;

public class RepositoryService {

    private static final MindcardRepository mindcardRepository = new MindcardRepository(AppConfig.getDatabase());
    private static final InfocardRepository infocardRepository = new InfocardRepository(AppConfig.getDatabase());
    private static final CardPackRepository cardPackRepository = new CardPackRepository(AppConfig.getDatabase());
    private static final ImageRepository imageRepository = new ImageRepository(AppConfig.getDatabase());
    private static final UserRepository userRepository = new UserRepository(AppConfig.getDatabase());

    public static MindcardRepository getMindcardRepository() { return mindcardRepository; }
    public static InfocardRepository getInfocardRepository() { return infocardRepository; }
    public static CardPackRepository getCardPackRepository() { return cardPackRepository; }
    public static ImageRepository getImageRepository() { return imageRepository; }
    public static UserRepository getUserRepository() { return userRepository; }

    public static CardElement getElement(Mindcard mindcard) {
        Image image = mindcardRepository.getImage(mindcard.getMindcardId());
        String imagePath = image != null ? image.getImagePath() : "";
        return new CardElement(mindcard.getTitle(),imagePath,mindcard.getDescription());
    }

    public static CardElement getElement(Infocard infocard) {
        Image image = infocardRepository.getImage(infocard.getInfocardId());
        String imagePath = image != null ? image.getImagePath() : "";
        return new CardElement("",imagePath,infocard.getDescription());
    }

}
