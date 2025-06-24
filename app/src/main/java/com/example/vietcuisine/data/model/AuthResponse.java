package com.example.vietcuisine.data.model;

import com.google.gson.annotations.SerializedName;

public class AuthResponse {
    private boolean success;
    private String message;
    private String token;
    
    @SerializedName("account")
    private Account account;
    
    @SerializedName("user")
    private User user;

    // Constructors
    public AuthResponse() {}

    // Getters and Setters
    public boolean isSuccess() { return success; }
    public void setSuccess(boolean success) { this.success = success; }

    public String getMessage() { return message; }
    public void setMessage(String message) { this.message = message; }

    public String getToken() { return token; }
    public void setToken(String token) { this.token = token; }

    public Account getAccount() { return account; }
    public void setAccount(Account account) { this.account = account; }

    public User getUser() { 
        // If user is null but account exists, try to get user from account
        if (user == null && account != null) {
            return account.getUser();
        }
        return user; 
    }
    public void setUser(User user) { this.user = user; }
}
