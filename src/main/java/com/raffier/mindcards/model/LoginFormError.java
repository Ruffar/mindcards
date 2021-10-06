package com.raffier.mindcards.model;

public class LoginFormError {

    private boolean invalidLogin;
    private boolean wrongEmailFormat;
    private boolean wrongPasswordFormat;

    public LoginFormError(boolean correctEmailFormat, boolean correctPasswordFormat) {
        this.invalidLogin = correctEmailFormat && correctPasswordFormat;
        this.wrongEmailFormat = !correctEmailFormat;
        this.wrongPasswordFormat = !correctPasswordFormat;
    }

    public boolean isInvalidLogin() { return invalidLogin; }
    public void setInvalidLogin(boolean invalidLogin) { this.invalidLogin = invalidLogin; }
    public boolean isWrongEmailFormat() { return wrongEmailFormat; }
    public void setWrongEmailFormat(boolean wrongEmailFormat) { this.wrongEmailFormat = wrongEmailFormat; }
    public boolean isWrongPasswordFormat() { return wrongPasswordFormat; }
    public void setWrongPasswordFormat(boolean wrongPasswordFormat) { this.wrongPasswordFormat = wrongPasswordFormat; }

}
