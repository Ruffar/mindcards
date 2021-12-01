package com.raffier.mindcards.service;

import com.raffier.mindcards.errorHandling.FormFieldException;
import com.raffier.mindcards.model.table.*;
import com.raffier.mindcards.repository.table.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserService {

    @Autowired
    private EncryptionService encryptionService;
    @Autowired
    private UserRepository userRepository;

    public User getUser(int userId) {
        return userRepository.getById(userId);
    }

    public User userLogin(String email, String password) {
        if (!isCorrectEmailFormat(email)) throw new FormFieldException("E-Mail must be in the format \"name@mail\"");
        if (!isCorrectPasswordFormat(password)) throw new FormFieldException("Password must have at least one number, lowercase alphabet, and uppercase alphabet and be at least 8 characters");//throw new FormFieldException("Password must have at least 8 characters and an uppercase, a lowercase letter, and a number");

        List<User> users = userRepository.getByEmail(email);
        for (User user : users) {
            if (encryptionService.passwordMatches(password,user)) return user;
        }
        throw new FormFieldException("Could not find user with matching E-Mail and Password");
    }

    public User userRegister(String username, String email, String password) {
        if (!isValidUsername(username)) throw new FormFieldException("Username must have at least 3 characters");
        if (!isCorrectEmailFormat(email)) throw new FormFieldException("E-Mail must be in the format \"name@mail\"");
        if (!isCorrectPasswordFormat(password)) throw new FormFieldException("Password must have at least one number, lowercase alphabet, and uppercase alphabet and be at least 8 characters");//throw new FormFieldException("Password must have at least 8 characters and an uppercase, a lowercase letter, and a number");

        User newUser = userRepository.add(new User(0,username,password,email,false)); //User id does not matter as adding the entity will automatically assign an ID
        newUser.setPassword(encryptionService.encryptPassword(password, newUser)); //Change user password to its encrypted version
        userRepository.save(newUser);

        return newUser;
    }

    private boolean isValidUsername(String username) {
        return username.matches("^.{3,}$");
    }

    private boolean isCorrectEmailFormat(String email) {
        return email.contains("@");
    }

    private boolean isCorrectPasswordFormat(String password) {
        //return password.matches("^[a-zA-Z0-9]{8,}$");
        return password.matches("^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z]).{8,}$");
    }

}
