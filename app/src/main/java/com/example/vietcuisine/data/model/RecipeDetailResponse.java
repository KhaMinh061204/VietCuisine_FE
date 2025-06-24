package com.example.vietcuisine.data.model;

import java.util.List;

public class RecipeDetailResponse {
    private boolean success;
    private String message;
    private Recipe data;

    public RecipeDetailResponse() {}

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Recipe getData() {
        return data;
    }

    public void setData(Recipe data) {
        this.data = data;
    }
}
