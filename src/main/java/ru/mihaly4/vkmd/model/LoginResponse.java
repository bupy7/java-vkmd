package ru.mihaly4.vkmd.model;

import javax.annotation.Nonnull;

public class LoginResponse {
    @Nonnull
    private String captcha = "";
    private boolean loggedIn;

    public LoginResponse(boolean loggedIn) {
        this.loggedIn = loggedIn;
    }

    @Nonnull
    public String getCaptcha() {
        return captcha;
    }

    public void setCaptcha(@Nonnull String captcha) {
        this.captcha = captcha;
    }

    public boolean isLoggedIn() {
        return loggedIn;
    }

    public void setLoggedIn(boolean loggedIn) {
        this.loggedIn = loggedIn;
    }
}
