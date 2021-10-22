package com.raffier.mindcards.model.login;

import org.springframework.context.annotation.Bean;
import org.springframework.lang.Nullable;

import java.io.Serializable;

public class LoginSubmission implements Serializable {

    private String email;
    private String password;

    public LoginSubmission(String email, String password) {
        this.email = email;
        this.password = password;
    }

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
