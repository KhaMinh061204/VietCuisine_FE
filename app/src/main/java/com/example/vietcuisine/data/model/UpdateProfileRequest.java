package com.example.vietcuisine.data.model;

public class UpdateProfileRequest {
    private String name;
    private String phone;
    private String gender;
    private String bio;

    public UpdateProfileRequest() {}

    public UpdateProfileRequest(String name, String phone, String gender, String bio) {
        this.name = name;
        this.phone = phone;
        this.gender = gender;
        this.bio = bio;
    }

    // Getters
    public String getName() {
        return name;
    }

    public String getPhone() {
        return phone;
    }

    public String getGender() {
        return gender;
    }

    public String getBio() {
        return bio;
    }

    // Setters
    public void setName(String name) {
        this.name = name;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public void setBio(String bio) {
        this.bio = bio;
    }
}
