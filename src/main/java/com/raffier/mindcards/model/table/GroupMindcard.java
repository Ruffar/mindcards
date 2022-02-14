package com.raffier.mindcards.model.table;

public class GroupMindcard extends EntityTable<GroupMindcard> {

    private final int cardGroupId;
    private final int mindcardId;

    public GroupMindcard(int cardGroupId, int mindcardId) {
        super("GroupMindcard");
        this.cardGroupId = cardGroupId;
        this.mindcardId = mindcardId;
    }

    public GroupMindcard getPrimaryKey() {
        return this;
    }

    public int getCardGroupId() { return this.cardGroupId; }
    public int getMindcardId() { return this.mindcardId; }

    public String toString() {
        return "GroupMindcard{" +
                "cardGroupId=" + cardGroupId +
                ", mindcardId=" + mindcardId +
                '}';
    }
}