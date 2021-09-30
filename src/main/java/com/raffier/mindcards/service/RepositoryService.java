package com.raffier.mindcards.service;


import com.raffier.mindcards.AppConfig;
import com.raffier.mindcards.repository.CardPackRepository;
import com.raffier.mindcards.repository.MindcardRepository;

public class RepositoryService {

    private static final MindcardRepository mindcardRepository = new MindcardRepository(AppConfig.getDatabase());
    private static final CardPackRepository cardPackRepository = new CardPackRepository(AppConfig.getDatabase());


    public static MindcardRepository getMindcardRepository() {return mindcardRepository; }
    public static CardPackRepository getCardPackRepository() { return cardPackRepository; }

}
