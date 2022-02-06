package com.raffier.mindcards.service;

import com.raffier.mindcards.model.table.GroupMindcard;
import com.raffier.mindcards.repository.table.CardGroupRepository;
import com.raffier.mindcards.repository.table.GroupMindcardRepository;
import com.raffier.mindcards.repository.table.MindcardRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.SQLException;

@Service
public class CardGroupService {

    @Autowired
    private GroupMindcardRepository groupMindcardRepository;
    @Autowired
    private CardGroupRepository cardGroupRepository;
    @Autowired
    private MindcardRepository mindcardRepository;

    public void addMindcardToCardGroup(int mindcardId, int cardGroupId) throws SQLException {
        mindcardRepository.getById(mindcardId);
        cardGroupRepository.getById(cardGroupId);
        groupMindcardRepository.add(new GroupMindcard(cardGroupId, mindcardId));
    }

    public void removeMindcardFromCardGroup(int mindcardId, int cardGroupId) throws SQLException {
        groupMindcardRepository.deleteById(new GroupMindcard(cardGroupId, mindcardId));
    }

    public boolean isMindcardInCardGroup(int mindcardId, int cardGroupId) throws SQLException {
        return groupMindcardRepository.exists(new GroupMindcard(cardGroupId, mindcardId));
    }

}
