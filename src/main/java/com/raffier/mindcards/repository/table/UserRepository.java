package com.raffier.mindcards.repository.table;

import com.raffier.mindcards.errorHandling.EntityNotFoundException;
import com.raffier.mindcards.model.table.User;
import com.raffier.mindcards.repository.AppDatabase;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import java.util.ArrayList;
import java.util.List;

@Component
public class UserRepository extends EntityRepository<User,Integer> {

    @Autowired
    public UserRepository(AppDatabase database) {
        super(database);
    }

    protected void throwEntityNotFound(Integer id) { throw new EntityNotFoundException("User", id); }

    // Updates //
    public void save(User entity) {
        executeUpdate(
                "UPDATE User SET username=?,password=?,email=?,isDeveloper=? WHERE userId=?",
                (stmnt) -> {
                    stmnt.setString(1, entity.getUsername());
                    stmnt.setString(2, entity.getPassword());
                    stmnt.setString(3, entity.getEmail());
                    stmnt.setBoolean(4, entity.isUserDeveloper());
                    stmnt.setInt(5, entity.getUserId());
                });
    }

    public User add(User entity) {
        int newId = executeUpdate(
                "INSERT INTO User (username,password,email,isDeveloper) VALUES (?,?,?,?)",
                (stmnt) -> {
                    stmnt.setString(1, entity.getUsername());
                    stmnt.setString(2, entity.getPassword());
                    stmnt.setString(3, entity.getEmail());
                    stmnt.setBoolean(4, entity.isUserDeveloper());
                });
        System.out.println("User with ID "+newId+" successfully created.");
        return getById(newId);
    }

    public void deleteById(Integer id) {
        executeUpdate(
                "DELETE FROM User WHERE userId=?",
                (stmnt) -> stmnt.setInt(1,id));
        System.out.println("User with ID "+id+" successfully deleted.");
    }

    // Queries //
    public User getById(Integer id) {
        return executeQuery(
                "SELECT * FROM User WHERE userId=?",
                (stmnt) -> stmnt.setInt(1,id),

                (results) -> {
                    if (results.next()) {
                        return new User(id, results.getString("username"),
                                results.getString("password"), results.getString("email"),
                                results.getBoolean("isDeveloper"));
                    }
                    throwEntityNotFound(id);
                    return null;
                });
    }

    public User getByEmail(String email) {

        return executeQuery(
                "SELECT * FROM User WHERE email=?",
                (stmnt) -> {
                    stmnt.setString(1, email);
                },

                (results) -> {
                    if (results.next()) {
                        return new User(results.getInt("userId"), results.getString("username"),
                                results.getString("password"), email,
                                results.getBoolean("isDeveloper"));
                    }
                    throw new EntityNotFoundException("User with E-Mail "+email+" cannot be found...");
                });
    }
}