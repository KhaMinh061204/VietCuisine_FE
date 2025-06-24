package com.example.vietcuisine.data.model;

public class RecipeIngredient {
    private String name;
    private String quantity;

    // Constructors
    public RecipeIngredient() {}

    public RecipeIngredient(String name, String quantity) {
        this.name = name;
        this.quantity = quantity;
    }

    // Getters and Setters
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getQuantity() { return quantity; }
    public void setQuantity(String quantity) { this.quantity = quantity; }
}
