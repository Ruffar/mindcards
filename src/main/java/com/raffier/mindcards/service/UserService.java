package com.raffier.mindcards.service;

import com.raffier.mindcards.model.CardElement;
import com.raffier.mindcards.model.LoginSubmission;
import com.raffier.mindcards.model.table.*;
import com.raffier.mindcards.repository.AppDatabase;
import com.raffier.mindcards.repository.table.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpSession;

@Service
public class UserService {

    private AppDatabase appDatabase;

    private final UserRepository userRepository;

    @Autowired
    public UserService(AppDatabase appDatabase) {
        userRepository = new UserRepository(appDatabase);
    }

    public User userLogin(LoginSubmission submission) {
        return userRepository.getByLogin(submission.getEmail(), submission.getPassword());
    }

}
