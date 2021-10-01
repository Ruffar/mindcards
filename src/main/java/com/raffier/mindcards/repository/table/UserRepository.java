package com.raffier.mindcards.repository.table;

import com.raffier.mindcards.model.table.User;
import com.raffier.mindcards.repository.AppDatabase;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class UserRepository extends EntityRepository<User,Integer> {

    public UserRepository(AppDatabase database) {
        super(database);
    }

    public <S extends User> void save(S entity) {
        try (PreparedStatement statement = database.getConnection().prepareStatement("UPDATE User SET username=?,password=?,email=?,isDeveloper=?,studyHelp=? WHERE userId=?")) {
            statement.setString(1, entity.getUsername());
            statement.setString(2,entity.getPassword());
            statement.setString(3,entity.getEmail());
            statement.setBoolean(4,entity.isDeveloper());
            statement.setBoolean(5,entity.isUsingStudyHelp());
            statement.setInt(6,entity.getUserId());
            statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public User getById(Integer id) {
        try (PreparedStatement stmnt = database.getConnection().prepareStatement("SELECT username,password,email,isDeveloper,studyHelp FROM User WHERE userId=?")) {
            stmnt.setInt(1,id);
            ResultSet results = stmnt.executeQuery();
            if (results.next()) {
                return new User(id,results.getString("username"),
                        results.getString("password"), results.getString("email"),
                        results.getBoolean("isDeveloper"),results.getBoolean("studyHelp"));
            } else {
                System.out.println("User with ID "+id+" cannot be found.");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return null;
    }

    public User getByLogin(String email, String password) {

        try (PreparedStatement stmnt = database.getConnection().prepareStatement("SELECT userId,username,isDeveloper,studyHelp FROM User WHERE email=? AND password=?")) {
            stmnt.setString(1,email);
            stmnt.setString(2,password);
            ResultSet results = stmnt.executeQuery();
            if (results.next()) {
                return new User(results.getInt("userId"), results.getString("username"), password,
                        email, results.getBoolean("isDeveloper"), results.getBoolean("studyHelp"));
            } else {
                System.out.println("User with login details cannot be found.");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return null;
    }

    public <S extends User> User add(S entity) {
        try (PreparedStatement stmnt = database.getConnection().prepareStatement("INSERT INTO User (username,password,email,isDeveloper,studyHelp) VALUES (?,?,?,?,?)")) {
            stmnt.setString(1,entity.getUsername());
            stmnt.setString(2,entity.getPassword());
            stmnt.setString(3,entity.getEmail());
            stmnt.setBoolean(4,entity.isDeveloper());
            stmnt.setBoolean(5,entity.isUsingStudyHelp());
            stmnt.executeUpdate();
            ResultSet generatedIds = stmnt.getGeneratedKeys();
            if (generatedIds.next()) {
                int newId = generatedIds.getInt(1);
                System.out.println("User with ID "+newId+" successfully created.");
                return getById(newId);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public <S extends User> void delete(S entity) {
        deleteById(entity.getUserId());
    }

    public void deleteById(Integer id) {
        try (PreparedStatement stmnt = database.getConnection().prepareStatement("DELETE FROM User WHERE userId=?")) {
            stmnt.setInt(1,id);
            stmnt.executeUpdate();
            System.out.println("User with ID "+id+" successfully deleted.");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}