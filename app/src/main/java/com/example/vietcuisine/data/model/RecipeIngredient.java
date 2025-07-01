package com.example.vietcuisine.data.model;

import java.io.Serializable;

public class RecipeIngredient implements Serializable {
    private String _id;
    private String ingredientId;
    private String name;
    private String quantity;
    private String recipeId;
    private String imageUrl;

    // Add this field
    private boolean selected = false;
    private double unitPrice = 0;
    private String unit;

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

    public String getUnit() {
        return unit;
    }

    public void setIngredientId(String ingredientId) { this.ingredientId = ingredientId; }

    public String getIngredientName() { return name; }
    public void setIngredientName(String ingredientName) { this.name = ingredientName; }

    public String getQuantity() { return quantity; }
    public String setQuantity(String quantity) { this.quantity = quantity;
        return quantity;
    }

    public String getRecipeId() { return recipeId; }
    public void setRecipeId(String recipeId) { this.recipeId = recipeId; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public double getUnitPrice() { return unitPrice; }

    public String getImageUrl() {
        return imageUrl;
    }

    // Selection
    public boolean isSelected() { return selected; }
    public void setSelected(boolean selected) { this.selected = selected; }
}