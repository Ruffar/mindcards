package com.raffier.mindcards.data;

import java.sql.*;

public class AppDatabase {

    private static String dbFolderUrl = "jdbc:sqlite:src/main/resources/db/";

    private final String dbUrl;
    
    private Connection connection;

    public AppDatabase(String dbName) {

        dbUrl = dbFolderUrl + dbName + ".db";
        this.connection = getConnection();

    }

    public Connection getConnection() {
        
        try (Connection connection = DriverManager.getConnection(dbUrl)) {

            if (connection != null) {
                DatabaseMetaData meta = connection.getMetaData();
                System.out.println("The driver name is " + meta.getDriverName());
                System.out.println("A new database has been created.");
            }

            return connection;

        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }

        return null;

    }
    
    private void executeSqlStatement(String sqlStatement) {
        try (Statement statement = conn.createStatement() {
            statement.execute(sqlStatement);
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }
             
    //Create tables
    
    
}
