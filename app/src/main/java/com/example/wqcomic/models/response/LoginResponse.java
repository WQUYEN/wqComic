package com.example.wqcomic.models.response;


import com.example.wqcomic.models.User;

public class LoginResponse {
    private User user;
    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

}