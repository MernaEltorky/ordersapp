package com.example.firebase;

public class UserData {


    private String name, email ,profileImageUrl;
    protected boolean isProvider;



    public UserData() {

    }

    public UserData(String name, String email, String profileImageUrl, boolean isProvider) {
        this.name = name;
        this.email = email;
        this.profileImageUrl = profileImageUrl;
        this.isProvider = isProvider;
    }

    public boolean isProvider() {
        return isProvider;
    }

    public void setProvider(boolean provider) {
        isProvider = provider;
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

    public String getProfileImageUrl() {
        return profileImageUrl;
    }

    public void setProfileImageUrl(String profileImageUrl) {
        this.profileImageUrl = profileImageUrl;
    }

    @Override
    public String toString() {
        return "UserData{" +
                "name='" + name + '\'' +
                ", email='" + email + '\'' +
                ", profileImageUrl='" + profileImageUrl + '\'' +
                ", isProvider=" + isProvider +
                '}';
    }
}

