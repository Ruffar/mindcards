package com.raffier.mindcards.repository;

import java.sql.*;

public class AppDatabase {

    private static final String dbFolderUrl = "jdbc:sqlite:src/main/resources/db/";

    private final String dbPath;
    private final Connection genericConnection;

    public AppDatabase(String dbName) {

        this.dbPath = dbFolderUrl + dbName + ".db";
        this.genericConnection = getConnection();

        createMindcardTable();
        createInfocardTable();
        createCardGroupTable();
        createGroupCardTable();
        createCardPackTable();
        createImageTable();
        createTagTable();
        createPackTagTable();
        createUserTable();
        createUserCardStatsTable();
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
    
    public void executeSqlStatement(String sqlStatement) {
        //Connection conn = getConnection();
        try (Statement statement = genericConnection.createStatement()) {
            statement.executeUpdate(sqlStatement);
            ResultSet dbm = genericConnection.getMetaData().getTables(null,null,"Mindcard",null);
            if (dbm.next()) { System.out.println("ymeme"); }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    public ResultSet executeSqlQuery(String sqlStatement) {
        //Connection conn = getConnection();
        try (Statement statement = genericConnection.createStatement()) {
            ResultSet cool = statement.executeQuery(sqlStatement);
            while (cool.next()) {
                System.out.println(cool.getString("title"));
            }
            return statement.executeQuery(sqlStatement);
        } catch (SQLException e) {
            System.out.println(e.getMessage());
            return null;
        }
    }
             
    //Create tables
    private void createMindcardTable() {
        try (Statement statement = genericConnection.createStatement()) {
            statement.executeUpdate("CREATE TABLE IF NOT EXISTS Mindcard" +
                    "(mindcardId INTEGER PRIMARY KEY AUTOINCREMENT," +
                    "packId INTEGER NOT NULL," +
                    "title TEXT," +
                    "imageId INTEGER," +
                    "description TEXT," +
                    "FOREIGN KEY (packId) references CardPack(packId)," +
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
                    "packId INTEGER NOT NULL," +
                    "title TEXT," +
                    "imageId INTEGER," +
                    "description TEXT," +
                    "FOREIGN KEY (packId) references CardPack(packId)," +
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
    private void createCardPackTable() {
        try (Statement statement = genericConnection.createStatement()) {
            statement.executeUpdate("CREATE TABLE IF NOT EXISTS CardPack" +
                    "(packId INTEGER PRIMARY KEY AUTOINCREMENT," +
                    "ownerId INTEGER," +
                    "title TEXT," +
                    "imageId INTEGER," +
                    "description TEXT," +
                    "FOREIGN KEY (ownerId) references User(userId)," +
                    "FOREIGN KEY (imageId) references Image(imageId) )");
            statement.executeUpdate("INSERT OR REPLACE INTO CardPack VALUES (0,'DeletedPack',0,'Pack does not exist anymore.',0)");
        } catch (SQLException e) {
            e.printStackTrace();
        }
        System.out.println("Card Pack Table created successfully!");
    }
    private void createImageTable() {
        try (Statement statement = genericConnection.createStatement()) {
            statement.executeUpdate("CREATE TABLE IF NOT EXISTS Image" +
                    "(imageId INTEGER PRIMARY KEY AUTOINCREMENT," +
                    "imagePath TEXT )");
            statement.executeUpdate("INSERT OR REPLACE INTO Image VALUES (0,'DeletedImage')");
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
    private void createPackTagTable() {
        try (Statement statement = genericConnection.createStatement()) {
            statement.executeUpdate("CREATE TABLE IF NOT EXISTS PackTag" +
                    "(packId INTEGER NOT NULL," +
                    "tagId INTEGER NOT NULL," +
                    "PRIMARY KEY (packId,tagId)," +
                    "FOREIGN KEY (packId) references CardPack(packId)," +
                    "FOREIGN KEY (tagId) references Tag(tagId) )");
        } catch (SQLException e) {
            e.printStackTrace();
        }
        System.out.println("Pack Tag Table created successfully!");
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

    
}
