package com.example.vietcuisine.data.model;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

public class RecipeDetailResponse {
    @SerializedName("status")
    private boolean status;

    @SerializedName("message")
    private String message;

    @SerializedName("recipes")
    private List<Recipe> recipes;

    public boolean isSuccess() {
        return status;
    }

    public Recipe getData() {
        return recipes != null && !recipes.isEmpty() ? recipes.get(0) : null;
    }

    public String getMessage() {
        return message;
    }

    public RecipeDetailResponse() {}

    public void setSuccess(boolean success) {
        this.status = success;
    }


    public void setMessage(String message) {
        this.message = message;
    }


    public void setData(Recipe recipe) {
        this.recipes = new ArrayList<>();
        this.recipes.add(recipe);
    }
}
