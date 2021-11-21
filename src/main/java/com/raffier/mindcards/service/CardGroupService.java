package com.raffier.mindcards.service;

import com.raffier.mindcards.model.table.GroupMindcard;
import com.raffier.mindcards.repository.table.GroupMindcardRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CardGroupService {

    @Autowired
    private GroupMindcardRepository groupMindcardRepository;


    public void addMindcardToCardGroup(int mindcardId, int cardGroupId) {
        groupMindcardRepository.add(new GroupMindcard(cardGroupId, mindcardId));
    }

    public void removeMindcardFromCardGroup(int mindcardId, int cardGroupId) {
        groupMindcardRepository.delete(new GroupMindcard(cardGroupId, mindcardId));
    }

    public boolean isMindcardInCardGroup(int mindcardId, int cardGroupId) {
        return groupMindcardRepository.exists(new GroupMindcard(cardGroupId, mindcardId));
    }

}
