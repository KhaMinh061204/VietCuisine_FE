package com.example.vietcuisine.data.model;

public class UserResponse {
    private boolean status;
    private String message;
    private User user;

    public UserResponse() {}

    public boolean isStatus() { return status; }
    public void setStatus(boolean status) { this.status = status; }

    public String getMessage() { return message; }
    public void setMessage(String message) { this.message = message; }

    public User getUser() { return user; }
    public void setUser(User user) { this.user = user; }
}
