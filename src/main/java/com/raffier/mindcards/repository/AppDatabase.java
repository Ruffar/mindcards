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
    private final Connection genericConnection;

    @Autowired
    public AppDatabase(ResourceLoader resourceLoader, @Value("${databasename}") String databaseName) {

        try {
            this.dbPath = "jdbc:sqlite:"+resourceLoader.getResource("classpath:/db/").getURI().getPath() + databaseName + ".db";
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
        this.genericConnection = getConnection();

        createMindcardTable();
        createInfocardTable();
        createCardGroupTable();
        createGroupCardTable();
        createDeckTable();
        createImageTable();
        createTagTable();
        createDeckTagTable();
        createUserTable();
        createUserCardStatsTable();
        createFavouriteTable();
    }

    public Connection getConnection() {

        if (genericConnection == null) {
            try {
                Connection connection = DriverManager.getConnection(dbPath);

                if (connection != null) {
                    System.out.println("Database created successfully!");
                }

                return connection;

            } catch (SQLException e) {
                e.printStackTrace();
            }
        }

        return genericConnection;

    }
             
    //Create tables
    private void createMindcardTable() {
        try (Statement statement = genericConnection.createStatement()) {
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
    private void createInfocardTable() {
        try (Statement statement = genericConnection.createStatement()) {
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
    private void createCardGroupTable() {
        try (Statement statement = genericConnection.createStatement()) {
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
    private void createGroupCardTable() {
        try (Statement statement = genericConnection.createStatement()) {
            statement.executeUpdate("CREATE TABLE IF NOT EXISTS GroupCard" +
                    "(cardGroupId INTEGER NOT NULL," +
                    "mindcardId INTEGER NOT NULL," +
                    "PRIMARY KEY (cardGroupId,mindcardId)," +
                    "FOREIGN KEY (cardGroupId) references CardGroup(cardGroupId)," +
                    "FOREIGN KEY (mindcardId) references Mindcard(mindcardId) )");
        } catch (SQLException e) {
            e.printStackTrace();
        }
        System.out.println("Group Card Table created successfully!");
    }
    private void createDeckTable() {
        try (Statement statement = genericConnection.createStatement()) {
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
    private void createImageTable() {
        try (Statement statement = genericConnection.createStatement()) {
            statement.executeUpdate("CREATE TABLE IF NOT EXISTS Image" +
                    "(imageId INTEGER PRIMARY KEY AUTOINCREMENT," +
                    "name TEXT," +
                    "imagePath TEXT )");
            statement.executeUpdate("INSERT OR REPLACE INTO Image VALUES (0,'DeletedImage','')");
        } catch (SQLException e) {
            e.printStackTrace();
        }
        System.out.println("Image Table created successfully!");
    }
    private void createTagTable() {
        try (Statement statement = genericConnection.createStatement()) {
            statement.executeUpdate("CREATE TABLE IF NOT EXISTS Tag" +
                    "(tagId INTEGER PRIMARY KEY AUTOINCREMENT," +
                    "tagName TEXT )");
            statement.executeUpdate("INSERT OR REPLACE INTO Tag VALUES (0,'DeletedTag')");
        } catch (SQLException e) {
            e.printStackTrace();
        }
        System.out.println("Tag Table created successfully!");
    }
    private void createDeckTagTable() {
        try (Statement statement = genericConnection.createStatement()) {
            statement.executeUpdate("CREATE TABLE IF NOT EXISTS DeckTag" +
                    "(deckId INTEGER NOT NULL," +
                    "tagId INTEGER NOT NULL," +
                    "PRIMARY KEY (deckId,tagId)," +
                    "FOREIGN KEY (deckId) references Deck(deckId)," +
                    "FOREIGN KEY (tagId) references Tag(tagId) )");
        } catch (SQLException e) {
            e.printStackTrace();
        }
        System.out.println("Deck Tag Table created successfully!");
    }
    private void createUserTable() {
        try (Statement statement = genericConnection.createStatement()) {
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
    private void createUserCardStatsTable() {
        try (Statement statement = genericConnection.createStatement()) {
            statement.executeUpdate("CREATE TABLE IF NOT EXISTS UserCardStats" +
                    "(userId INTEGER NOT NULL," +
                    "mindcardId INTEGER NOT NULL," +
                    "mastery INTEGER Default 0,"+
                    "PRIMARY KEY (userId,mindcardId)," +
                    "FOREIGN KEY (userId) references User(userId)," +
                    "FOREIGN KEY (mindcardId) references Mindcard(mindcardId) )");
        } catch (SQLException e) {
            e.printStackTrace();
        }
        System.out.println("User Card Stats Table created successfully!");
    }
    private void createFavouriteTable() {
        try (Statement statement = genericConnection.createStatement()) {
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
