package com.example.vietcuisine.data.model;

import com.google.gson.annotations.SerializedName;

public class Account {
    @SerializedName("_id")
    private String id;
    
    // This can be either a User object or just a string ID
    private Object user; // Can be String or User object
    
    private String email;
    private String role;
    private String status;
    private String passwordResetToken;
    private String passwordResetExpires;
    private boolean isOtpVerified;
    private String createdAt;
    private String updatedAt;

    // Constructors
    public Account() {}

    // Getters and Setters
    public String getId() { return id; }
    public void setId(String id) { this.id = id; }

    public User getUser() { 
        if (user instanceof User) {
            return (User) user;
        }
        // If user is just an ID string, return null
        // The user info should be fetched separately if needed
        return null;
    }
    
    public String getUserId() {
        if (user instanceof String) {
            return (String) user;
        } else if (user instanceof User) {
            return ((User) user).getId();
        }
        return null;
    }
    
    public void setUser(Object user) { this.user = user; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getRole() { return role; }
    public void setRole(String role) { this.role = role; }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }

    public String getPasswordResetToken() { return passwordResetToken; }
    public void setPasswordResetToken(String passwordResetToken) { this.passwordResetToken = passwordResetToken; }

    public String getPasswordResetExpires() { return passwordResetExpires; }
    public void setPasswordResetExpires(String passwordResetExpires) { this.passwordResetExpires = passwordResetExpires; }

    public boolean isOtpVerified() { return isOtpVerified; }
    public void setOtpVerified(boolean otpVerified) { this.isOtpVerified = otpVerified; }

    public String getCreatedAt() { return createdAt; }
    public void setCreatedAt(String createdAt) { this.createdAt = createdAt; }

    public String getUpdatedAt() { return updatedAt; }
    public void setUpdatedAt(String updatedAt) { this.updatedAt = updatedAt; }
}
