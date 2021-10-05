package com.raffier.mindcards.model.table;

import com.raffier.mindcards.repository.AppDatabase;

import java.sql.*;

public class User extends EntityTable<Integer> {

    //Database columns
    private int userId;

    private String username;
    private String password;
    private String email;
    private boolean isDeveloper;
    private boolean studyHelp;

    public User() {
        super("user");
    }

    public User(int userId) {
        super("User");
        this.userId = userId;
    }

    public User(int userId, String username, String password, String email, boolean isDeveloper, boolean studyHelp) {
        this(userId);
        this.username = username;
        this.password = password;
        this.email = email;
        this.isDeveloper = isDeveloper;
        this.studyHelp = studyHelp;
    }

    public Integer getPrimaryKey() { return getUserId(); }
    public int getUserId() { return userId; }
    public String getUsername() { return username; }
    public String getPassword() { return password; }
    public String getEmail() { return email; }
    public boolean isDeveloper() { return isDeveloper; }
    public boolean isUsingStudyHelp() { return studyHelp; }

    public void setPrimaryKey(Integer primaryKey) { setUserId(primaryKey);}
    public void setUserId(int userId) { this.userId = userId; }
    public void setUsername(String username) { this.username = username; }
    public void setPassword(String password) { this.password = password; }
    public void setEmail(String email) { this.email = email; }
    public void setDeveloperStatus(boolean isDeveloper) { this.isDeveloper = isDeveloper; }
    public void setStudyHelpStatus(boolean studyHelp) { this.studyHelp = studyHelp; }

}
