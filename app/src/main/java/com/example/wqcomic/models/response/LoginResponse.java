package com.example.wqcomic.models.response;


import com.example.wqcomic.models.User;

public class LoginResponse {
    private String token; // Thêm trường token
    private User user;

    // Getter và Setter cho token
    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }
}