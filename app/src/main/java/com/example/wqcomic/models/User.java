package com.example.wqcomic.models;

import com.google.gson.annotations.SerializedName;

public class User {
    @SerializedName("_id")
    private String id;
    private String name;
    private String email;
    private String password;
    private boolean isActive;
    private boolean isPremium;
    private String avatar;

//    public User(String id, String name, String email, String password, boolean isActive) {
//        this.id = id;
//        this.name = name;
//        this.email = email;
//        this.password = password;
//        this.isActive = isActive;
//    }

    public User(String id, String name, String email, String password, boolean isActive, boolean isPremium, String avatar) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.password = password;
        this.isActive = isActive;
        this.isPremium = isPremium;
        this.avatar = avatar;
    }

    // Getters và Setters
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public boolean isActive() {
        return isActive;
    }

    public void setActive(boolean active) {
        isActive = active;
    }

    public boolean isPremium() {
        return isPremium;
    }

    public void setPremium(boolean premium) {
        isPremium = premium;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }
}