package com.example.vietcuisine.data.model;

import java.util.List;

public class RecipeResponse {
    private boolean status;
    private String message;
    private List<Recipe> recipes;

    // Constructors
    public RecipeResponse() {}

    // Getters and Setters
    public boolean isStatus() { return status; }
    public void setStatus(boolean status) { this.status = status; }

    public String getMessage() { return message; }
    public void setMessage(String message) { this.message = message; }

    public List<Recipe> getRecipes() { return recipes; }
    public void setRecipes(List<Recipe> recipes) { this.recipes = recipes; }
}
