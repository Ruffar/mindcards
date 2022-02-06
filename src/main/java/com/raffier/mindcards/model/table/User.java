package com.raffier.mindcards.model.table;

public class User extends EntityTable<Integer> {

    //Database columns
    private final int userId;

    private String username;
    private String password;
    private String email;
    private boolean isDeveloper;

    public User(int userId) {
        super("user");
        this.userId = userId;
        this.username = "";
        this.password = "";
        this.email = "";
        this.isDeveloper = false;
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
