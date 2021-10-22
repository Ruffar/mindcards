package com.raffier.mindcards.service;

import com.raffier.mindcards.errorHandling.FormFieldException;
import com.raffier.mindcards.errorHandling.UnauthorisedAccessException;
import com.raffier.mindcards.model.login.LoginFormError;
import com.raffier.mindcards.model.login.LoginSubmission;
import com.raffier.mindcards.model.table.*;
import com.raffier.mindcards.repository.AppDatabase;
import com.raffier.mindcards.repository.table.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserService {

    private AppDatabase appDatabase;

    private final UserRepository userRepository;

    @Autowired
    public UserService(AppDatabase appDatabase) {
        userRepository = new UserRepository(appDatabase);
    }

    public User userLogin(String email, String password) {
        if (!isCorrectEmailFormat(email)) throw new FormFieldException("E-Mail must be in the format \"name@mail\"");
        if (!isCorrectPasswordFormat(password)) throw new FormFieldException("Password must have at least one number, lowercase alphabet, and uppercase alphabet and be at least 8 characters");//throw new FormFieldException("Password must have at least 8 characters and an uppercase, a lowercase letter, and a number");
        return userRepository.getByLogin(email, password);
    }

    private boolean isCorrectEmailFormat(String email) {
        return email.contains("@");
    }

    private boolean isCorrectPasswordFormat(String password) {
        //return password.matches("^[a-zA-Z0-9]{8,}$");
        return password.matches("^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z]).{8,}$");
    }

}
