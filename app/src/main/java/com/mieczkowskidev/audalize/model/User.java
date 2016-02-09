package com.mieczkowskidev.audalize.model;

/**
 * Created by Patryk Mieczkowski on 2016-02-09
 */
public class User {

    private String AuthToken;

    public User() {
    }

    public String getAuthToken() {
        return AuthToken;
    }

    @Override
    public String toString() {
        return "User{" +
                "AuthToken='" + AuthToken + '\'' +
                '}';
    }
}
