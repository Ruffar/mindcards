package com.raffier.mindcards.model.table;

import com.raffier.mindcards.repository.AppDatabase;
import org.springframework.context.annotation.Scope;

import java.sql.*;

public class User extends EntityTable<Integer> {

    //Database columns
    private int userId;

    private String username;
    private String password;
    private String email;
    private boolean isDeveloper;

    public User() {
        super("user");
    }

    public User(int userId) {
        super("User");
        this.userId = userId;
    }

    public User(int userId, String username, String password, String email, boolean isDeveloper) {
        this(userId);
        this.username = username;
        this.password = password;
        this.email = email;
        this.isDeveloper = isDeveloper;
    }

    public Integer getPrimaryKey() { return getUserId(); }
    public int getUserId() { return userId; }
    public String getUsername() { return username; }
    public String getPassword() { return password; }
    public String getEmail() { return email; }
    public boolean isUserDeveloper() { return isDeveloper; }

    public void setUsername(String username) { this.username = username; }
    public void setPassword(String password) { this.password = password; }
    public void setEmail(String email) { this.email = email; }
    public void setDeveloperStatus(boolean isDeveloper) { this.isDeveloper = isDeveloper; }

}
