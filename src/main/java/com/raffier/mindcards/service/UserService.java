package com.raffier.mindcards.service;

import com.raffier.mindcards.errorHandling.EntityNotFoundException;
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
        //Validate Email and then Password
        if (!isCorrectEmailFormat(email)) throw new FormFieldException("E-Mail must be in the format \"name@mail\"");
        if (!isCorrectPasswordFormat(password)) throw new FormFieldException("Password must have at least one number, lowercase alphabet, and uppercase alphabet and be at least 8 characters");//throw new FormFieldException("Password must have at least 8 characters and an uppercase, a lowercase letter, and a number");

        //Get User with matching Email and compare password hashes
        User user = userRepository.getByEmail(email);
        if (encryptionService.passwordMatches(password,user)) return user;

        //
        throw new FormFieldException("Could not find user with matching E-Mail and Password");
    }

    public User userRegister(String username, String email, String password) {
        //Validate Username, Email and Password
        if (!isValidUsername(username)) throw new FormFieldException("Username must have at least 3 characters");
        if (!isCorrectEmailFormat(email)) throw new FormFieldException("E-Mail must be in the format \"name@mail\"");
        if (!isCorrectPasswordFormat(password)) throw new FormFieldException("Password must have at least one number, lowercase alphabet, and uppercase alphabet and be at least 8 characters");//throw new FormFieldException("Password must have at least 8 characters and an uppercase, a lowercase letter, and a number");

        //Check if a User with this Email already exists
        try {
            userRepository.getByEmail(email);
        } catch (EntityNotFoundException e) {
            throw new FormFieldException("User with E-Mail already exists!");
        }

        User newUser = userRepository.add(new User(0,username,password,email,false)); //User id does not matter as adding the entity will automatically assign an ID
        newUser.setPassword(encryptionService.encryptPassword(password, newUser)); //Change user password to its encrypted version
        userRepository.save(newUser);

        return newUser;
    }

    /*
    "^" and "&" represents the start and end of a RegEx expression respectively
    "." means check for every character in the string
    "*" means check if there is at least one character that matches a condition
    "[]" means a condition group e.g. [0-9] checks for characters between 0 and 9
    "{a,b}" means check if the string's length is between a and b, either can be omitted
     */

    private boolean isValidUsername(String username) {
        return username.matches("^.{3,}$");
    }

    private boolean isCorrectEmailFormat(String email) {
        return email.contains("@");
    }

    private boolean isCorrectPasswordFormat(String password) {
        return password.matches("^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z]).{8,}$");
    }

}
