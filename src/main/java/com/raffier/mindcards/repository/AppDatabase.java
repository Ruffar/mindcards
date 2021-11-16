package com.raffier.mindcards.repository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ResourceLoader;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.sql.*;

@Component
public class AppDatabase {

    private String dbPath;

    @Autowired
    public AppDatabase(ResourceLoader resourceLoader, @Value("${databasename}") String databaseName) {

        //Get database file from path
        try {
            this.dbPath = "jdbc:sqlite:"+resourceLoader.getResource("classpath:/db/").getURI().getPath() + databaseName + ".db";
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }

        Connection genericConnection = getConnection(); //temporary connection, also creates database if not created
        createMindcardTable(genericConnection);
        createInfocardTable(genericConnection);
        createCardGroupTable(genericConnection);
        createGroupMindcardTable(genericConnection);
        createDeckTable(genericConnection);
        createImageTable(genericConnection);
        createUserTable(genericConnection);
        createFavouriteTable(genericConnection);
    }

    public Connection getConnection() {

        try {
            Connection connection = DriverManager.getConnection(dbPath);

            if (connection != null) {
                System.out.println("Database connection created successfully!");
            }

            return connection;

        } catch (SQLException e) {
            System.out.println(e.getMessage());
            return null;
        }

    }
             
    //Create tables
    private void createMindcardTable(Connection connection) {
        try (Statement statement = connection.createStatement()) {
            statement.executeUpdate("CREATE TABLE IF NOT EXISTS Mindcard" +
                    "(mindcardId INTEGER PRIMARY KEY AUTOINCREMENT," +
                    "deckId INTEGER NOT NULL," +
                    "title TEXT," +
                    "imageId INTEGER," +
                    "description TEXT," +
                    "FOREIGN KEY (deckId) references Deck(deckId)," +
                    "FOREIGN KEY (imageId) references Image(imageId) )");
            statement.executeUpdate("INSERT OR REPLACE INTO Mindcard VALUES (0,0,'DeletedCard',0,'Card does not exist anymore.')");
        } catch (SQLException e) {
            e.printStackTrace();
        }
        System.out.println("Mindcard Table created successfully!");
    }
    private void createInfocardTable(Connection connection) {
        try (Statement statement = connection.createStatement()) {
            statement.executeUpdate("CREATE TABLE IF NOT EXISTS Infocard" +
                    "(infocardId INTEGER PRIMARY KEY AUTOINCREMENT," +
                    "mindcardId INTEGER NOT NULL," +
                    "imageId INTEGER," +
                    "description TEXT," +
                    "FOREIGN KEY (mindcardId) references Mindcard(mindcardId)," +
                    "FOREIGN KEY (imageId) references Image(imageId) )");
            statement.executeUpdate("INSERT OR REPLACE INTO Infocard VALUES (0,0,0,'Card does not exist anymore.')");
        } catch (SQLException e) {
            e.printStackTrace();
        }
        System.out.println("Infocard Table created successfully!");
    }
    private void createCardGroupTable(Connection connection) {
        try (Statement statement = connection.createStatement()) {
            statement.executeUpdate("CREATE TABLE IF NOT EXISTS CardGroup" +
                    "(cardGroupId INTEGER PRIMARY KEY AUTOINCREMENT," +
                    "deckId INTEGER NOT NULL," +
                    "title TEXT," +
                    "imageId INTEGER," +
                    "description TEXT," +
                    "FOREIGN KEY (deckId) references Deck(deckId)," +
                    "FOREIGN KEY (imageId) references Image(imageId) )");
            statement.executeUpdate("INSERT OR REPLACE INTO CardGroup VALUES (0,0,'DeletedGroup',0,'Group does not exist anymore.')");
        } catch (SQLException e) {
            e.printStackTrace();
        }
        System.out.println("Card Group Table created successfully!");
    }
    private void createGroupMindcardTable(Connection connection) {
        try (Statement statement = connection.createStatement()) {
            statement.executeUpdate("CREATE TABLE IF NOT EXISTS GroupMindcard" +
                    "(cardGroupId INTEGER NOT NULL," +
                    "mindcardId INTEGER NOT NULL," +
                    "PRIMARY KEY (cardGroupId,mindcardId)," +
                    "FOREIGN KEY (cardGroupId) references CardGroup(cardGroupId)," +
                    "FOREIGN KEY (mindcardId) references Mindcard(mindcardId) )");
        } catch (SQLException e) {
            e.printStackTrace();
        }
        System.out.println("Group Mindcard Table created successfully!");
    }
    private void createDeckTable(Connection connection) {
        try (Statement statement = connection.createStatement()) {
            statement.executeUpdate("CREATE TABLE IF NOT EXISTS Deck" +
                    "(deckId INTEGER PRIMARY KEY AUTOINCREMENT," +
                    "ownerId INTEGER," +
                    "title TEXT," +
                    "imageId INTEGER," +
                    "description TEXT," +
                    "isPrivate INTEGER Default 0," +
                    "timeCreated REAL," +
                    "FOREIGN KEY (ownerId) references User(userId)," +
                    "FOREIGN KEY (imageId) references Image(imageId) )");
            statement.executeUpdate("INSERT OR REPLACE INTO Deck VALUES (0,0,'DeletedDeck',0,'Deck does not exist anymore.',0,0)");
        } catch (SQLException e) {
            e.printStackTrace();
        }
        System.out.println("Deck Table created successfully!");
    }
    private void createImageTable(Connection connection) {
        try (Statement statement = connection.createStatement()) {
            statement.executeUpdate("CREATE TABLE IF NOT EXISTS Image" +
                    "(imageId INTEGER PRIMARY KEY AUTOINCREMENT," +
                    "imagePath TEXT )");
            statement.executeUpdate("INSERT OR REPLACE INTO Image VALUES (0,'')");
        } catch (SQLException e) {
            e.printStackTrace();
        }
        System.out.println("Image Table created successfully!");
    }
    private void createUserTable(Connection connection) {
        try (Statement statement = connection.createStatement()) {
            statement.executeUpdate("CREATE TABLE IF NOT EXISTS User" +
                    "(userId INTEGER PRIMARY KEY AUTOINCREMENT," +
                    "username TEXT," +
                    "password TEXT," +
                    "email TEXT NOT NULL," +
                    "isDeveloper INTEGER Default 0,"+
                    "studyHelp INTEGER Default 1)");
            statement.executeUpdate("INSERT OR REPLACE INTO User VALUES (0,'DeletedUser','deletedpassword1234','deleted@mindcards.com',0,1)");
        } catch (SQLException e) {
            e.printStackTrace();
        }
        System.out.println("User Table created successfully!");
    }
    private void createFavouriteTable(Connection connection) {
        try (Statement statement = connection.createStatement()) {
            statement.executeUpdate("CREATE TABLE IF NOT EXISTS Favourite" +
                    "(deckId INTEGER NOT NULL," +
                    "userId INTEGER NOT NULL," +
                    "PRIMARY KEY (deckId,userId)," +
                    "FOREIGN KEY (deckId) references Deck(deckId)," +
                    "FOREIGN KEY (userId) references User(userId) )");
        } catch (SQLException e) {
            e.printStackTrace();
        }
        System.out.println("Favourite Table created successfully!");
    }

    
}
