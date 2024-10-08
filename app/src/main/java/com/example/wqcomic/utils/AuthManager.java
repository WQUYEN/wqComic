package com.example.wqcomic.utils;

import android.content.Context;
import android.content.SharedPreferences;

import com.example.wqcomic.models.User;
import com.google.gson.Gson;

public class AuthManager {
    private static final String PREFS_NAME = "MyAppPrefs";
    private static final String USER_KEY = "user";
    private static final String TOKEN_KEY = "token";
    private final SharedPreferences sharedPreferences;
    private final Gson gson;

    public AuthManager(Context context) {
        sharedPreferences = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
        gson = new Gson();
    }

    public String getUserId() {
        String userJson = sharedPreferences.getString(USER_KEY, null);
        if (userJson != null) {
            User user = gson.fromJson(userJson, User.class);
            return user.getId();
        }
        return null;
    }

    public String getToken() {
        return sharedPreferences.getString(TOKEN_KEY, null);
    }
}