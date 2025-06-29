package com.example.vietcuisine.data.model;

public class UpdateProfileRequest {
    private String name;
    private String email;
    private String gender;
    private String phone;
    private String avatar;

    public UpdateProfileRequest(String name, String email, String gender, String phone, String avatar) {
        this.name = name;
        this.email = email;
        this.gender = gender;
        this.phone = phone;
        this.avatar = avatar;
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

    public String getEmail() {
        return email;
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

    public void setEmail(String email) {
        this.email = email;
    }
}
