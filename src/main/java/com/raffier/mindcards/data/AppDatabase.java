package com.raffier.mindcards.data;

import java.sql.*;

public class AppDatabase {

    private static String dbFolderUrl = "jdbc:sqlite:src/main/resources/db/";

    public static void createDatabase(String dbName) {

        String dbUrl = dbFolderUrl + dbName + ".db";

        try (Connection connection = DriverManager.getConnection(dbUrl)) {

            if (connection != null) {
                DatabaseMetaData meta = connection.getMetaData();
                System.out.println("The driver name is " + meta.getDriverName());
                System.out.println("A new database has been created.");
            }

        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }

    }
    
}
