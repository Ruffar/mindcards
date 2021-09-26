package com.raffier.mindcards.web;

import org.springframework.context.annotation.Bean;
import org.springframework.lang.Nullable;

public class LoginSubmission {

    private String email;
    private String password;

    public LoginSubmission() {}

    public String getEmail() {
        return this.email;
    }
    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return this.password;
    }
    public void setPassword(String password) {
        this.password = password;
    }

}
