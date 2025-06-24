package com.example.vietcuisine.data.model;

public class OrderDetailResponse {
    private boolean success;
    private String message;
    private IngredientOrder data;

    public OrderDetailResponse() {}

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

    public IngredientOrder getData() {
        return data;
    }

    public void setData(IngredientOrder data) {
        this.data = data;
    }
}
