package com.example.vietcuisine.data.model;

import java.io.Serializable;

public class RecipeIngredient implements Serializable {
    private String _id;
    private String ingredientId;
    private String name;
    private String quantity;
    private String recipeId;

    // Add this field
    private boolean selected = false;

    // Constructors
    public RecipeIngredient() {}

    public RecipeIngredient(String ingredientName, String quantity) {
        this.name = ingredientName;
        this.quantity = quantity;
    }

    // Getters and Setters
    public String getId() { return _id; }
    public void setId(String id) { this._id = id; }

    public String getIngredientId() { return ingredientId; }
    public void setIngredientId(String ingredientId) { this.ingredientId = ingredientId; }

    public String getIngredientName() { return name; }
    public void setIngredientName(String ingredientName) { this.name = ingredientName; }

    public String getQuantity() { return quantity; }
    public void setQuantity(String quantity) { this.quantity = quantity; }

    public String getRecipeId() { return recipeId; }
    public void setRecipeId(String recipeId) { this.recipeId = recipeId; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    // Selection
    public boolean isSelected() { return selected; }
    public void setSelected(boolean selected) { this.selected = selected; }
}