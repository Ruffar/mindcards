package com.raffier.mindcards.service;


import com.raffier.mindcards.AppConfig;
import com.raffier.mindcards.repository.MindcardRepository;

public class RepositoryService {

    private static final MindcardRepository mindcardRepository = new MindcardRepository(AppConfig.getDatabase());


    public static MindcardRepository getMindcardRepository() {return mindcardRepository; }

}
