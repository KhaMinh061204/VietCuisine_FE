package com.example.vietcuisine.data.model;

public class RecipeIngredient {
    private String _id;
    private String ingredientId;
    private String ingredientName;
    private double quantity;
    private String unit;
    private String recipeId;

    // Constructors
    public RecipeIngredient() {}

    public RecipeIngredient(String ingredientName, double quantity, String unit) {
        this.ingredientName = ingredientName;
        this.quantity = quantity;
        this.unit = unit;
    }

    // Getters and Setters
    public String getId() { return _id; }
    public void setId(String id) { this._id = id; }

    public String getIngredientId() { return ingredientId; }
    public void setIngredientId(String ingredientId) { this.ingredientId = ingredientId; }

    public String getIngredientName() { return ingredientName; }
    public void setIngredientName(String ingredientName) { this.ingredientName = ingredientName; }

    public double getQuantity() { return quantity; }
    public void setQuantity(double quantity) { this.quantity = quantity; }

    public String getUnit() { return unit; }
    public void setUnit(String unit) { this.unit = unit; }

    public String getRecipeId() { return recipeId; }
    public void setRecipeId(String recipeId) { this.recipeId = recipeId; }

    // Backward compatibility for old getName() method
    public String getName() { return ingredientName; }
    public void setName(String name) { this.ingredientName = name; }
}
