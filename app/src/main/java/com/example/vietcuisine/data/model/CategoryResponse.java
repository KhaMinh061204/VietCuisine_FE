package com.example.vietcuisine.data.model;

import java.util.List;

public class CategoryResponse {
    private boolean status;
    private String message;
    private List<Category> categories;

    // Constructors
    public CategoryResponse() {}

    // Getters and Setters
    public boolean isStatus() { return status; }
    public void setStatus(boolean status) { this.status = status; }

    public String getMessage() { return message; }
    public void setMessage(String message) { this.message = message; }

    public List<Category> getCategories() { return categories; }
    public void setCategories(List<Category> categories) { this.categories = categories; }
}
