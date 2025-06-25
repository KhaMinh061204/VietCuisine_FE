package com.example.vietcuisine.data.model;

import java.util.List;

public class RecipeResponse {
    private List<Recipe> recipes;

    // Constructors
    public RecipeResponse() {}

    // Getters and Setters
    public List<Recipe> getRecipes() { return recipes; }
    public void setRecipes(List<Recipe> recipes) { this.recipes = recipes; }
}
