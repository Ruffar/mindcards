package com.raffier.mindcards.model.table;

import com.raffier.mindcards.model.AppDatabase;

import java.sql.*;

public class User extends DatabaseTable {

    //Database columns
    private final int userId;

    private String username;
    private String password;
    private String email;
    private boolean isDeveloper;

    private User(AppDatabase database, int userId, ResultSet rawData) throws SQLException {
        super(database, "User");

        this.userId = userId;
        this.username = rawData.getString("username");
        this.password = rawData.getString("password");
        this.email = rawData.getString("email");
        this.isDeveloper = rawData.getBoolean("isDeveloper");
    }

    public int getUserId() { return userId; }
    public String getUsername() { return username; }
    public String getPassword() { return password; }
    public String getEmail() { return email; }
    public boolean isDeveloper() { return isDeveloper; }

    public void updateUsername(String newName) {
        try (PreparedStatement statement = database.getConnection().prepareStatement("UPDATE User SET username=? WHERE userId=?")) {
            statement.setInt(2, userId);
            statement.setString(1, newName);
            statement.executeUpdate();
            this.username = newName;
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    public void updatePassword(String newPassword) {
        try (PreparedStatement statement = database.getConnection().prepareStatement("UPDATE User SET password=? WHERE userId=?")) {
            statement.setInt(2, userId);
            statement.setString(1, newPassword);
            statement.executeUpdate();
            this.password = newPassword;
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    public void updateEmail(String newEmail) {
        try (PreparedStatement statement = database.getConnection().prepareStatement("UPDATE User SET email=? WHERE userId=?")) {
            statement.setInt(2, userId);
            statement.setString(1, newEmail);
            statement.executeUpdate();
            this.email = newEmail;
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    public void updateDeveloperStatus(boolean newStatus) {
        try (PreparedStatement statement = database.getConnection().prepareStatement("UPDATE User SET isDeveloper=? WHERE userId=?")) {
            statement.setInt(2, userId);
            statement.setBoolean(1, newStatus);
            statement.executeUpdate();
            this.isDeveloper = newStatus;
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void delete() {
        try (PreparedStatement stmnt = database.getConnection().prepareStatement("DELETE FROM User WHERE userId=?")) {
            stmnt.setInt(1,userId);
            stmnt.executeUpdate();
            System.out.println("User with ID "+userId+" successfully deleted.");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static User addUser(AppDatabase database, String username, String password, String email) {
        try (PreparedStatement stmnt = database.getConnection().prepareStatement("INSERT INTO User (username,password,email) VALUES (?,?,?)")) {
            stmnt.setString(1,username);
            stmnt.setString(2,password);
            stmnt.setString(3,email);
            stmnt.executeUpdate();
            ResultSet generatedIds = stmnt.getGeneratedKeys();
            if (generatedIds.next()) {
                int newId = generatedIds.getInt(1);
                System.out.println("User with ID "+newId+" successfully created.");
                return getUser(database, newId);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static User getUser(AppDatabase database, int userId) {

        try (PreparedStatement stmnt = database.getConnection().prepareStatement("SELECT username,password,email,isDeveloper FROM User WHERE userId=?")) {
            stmnt.setInt(1,userId);
            ResultSet results = stmnt.executeQuery();
            if (results.next()) {
                return new User(database, userId, results);
            } else {
                System.out.println("User with ID "+userId+" cannot be found.");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return null;

    }

    public static User getUserFromEmail(AppDatabase database, String email) {

        try (PreparedStatement stmnt = database.getConnection().prepareStatement("SELECT userId,username,password,email,isDeveloper FROM User WHERE email=?")) {
            stmnt.setString(1,email);
            ResultSet results = stmnt.executeQuery();
            if (results.next()) {
                return new User(database, results.getInt("userId"), results);
            } else {
                System.out.println("User with E-Mail "+email+" cannot be found.");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return null;

    }

}
