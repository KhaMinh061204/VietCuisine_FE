package com.example.vietcuisine.data.model;

import java.util.List;

public class IngredientResponse {
    private boolean status;
    private String message;
    private List<Ingredient> ingredients;

    public IngredientResponse() {}

    public boolean isStatus() { return status; }
    public void setStatus(boolean status) { this.status = status; }

    public String getMessage() { return message; }
    public void setMessage(String message) { this.message = message; }

    public List<Ingredient> getIngredients() { return ingredients; }
    public void setIngredients(List<Ingredient> ingredients) { this.ingredients = ingredients; }
}
