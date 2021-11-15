package com.raffier.mindcards.model.table;

import com.raffier.mindcards.repository.AppDatabase;

import java.sql.*;

public class Tag extends EntityTable<Integer> {

    //Database columns
    private int tagId;
    private String tagName;

    public Tag(int tagId) {
        super("Image");
        this.tagId = tagId;
    }

    public Tag(int tagId, String tagName) {
        this(tagId);
        this.tagName = tagName;
    }

    public Integer getPrimaryKey() { return getTagId(); }
    public int getTagId() { return tagId; }
    public String getTagName() { return tagName; }

    public void setTagId(int tagId) { this.tagId = tagId; }
    public void setTagName(String tagName) { this.tagName = tagName; }

}
