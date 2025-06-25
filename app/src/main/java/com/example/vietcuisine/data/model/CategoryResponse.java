package com.example.vietcuisine.data.model;

import java.util.List;

public class CategoryResponse {
    private List<Category> categories;

    // Constructors
    public CategoryResponse() {}

    // Getters and Setters
    public List<Category> getCategories() { return categories; }
    public void setCategories(List<Category> categories) { this.categories = categories; }
}
