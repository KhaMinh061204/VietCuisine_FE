package com.example.vietcuisine.data.model;

public class IngredientDetailResponse {
    private boolean success;
    private String message;
    private Ingredient data;

    public IngredientDetailResponse() {}

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

    public Ingredient getData() {
        return data;
    }

    public void setData(Ingredient data) {
        this.data = data;
    }
}
