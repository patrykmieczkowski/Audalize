package com.mieczkowskidev.audalize.model;

/**
 * Created by Patryk Mieczkowski on 2016-02-09
 */
public class DataProfile {

    private String username;
    private String email;
    private String personality;

    public DataProfile() {
    }

    public String getUsername() {
        return username;
    }

    public String getEmail() {
        return email;
    }

    public String getPersonality() {
        return personality;
    }

    @Override
    public String toString() {
        return "DataProfile{" +
                "username='" + username + '\'' +
                ", email='" + email + '\'' +
                ", personality='" + personality + '\'' +
                '}';
    }
}
