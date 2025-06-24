package com.example.vietcuisine.data.model;

import java.util.List;

public class OrderResponse {
    private boolean success;
    private String message;
    private List<IngredientOrder> data;

    public OrderResponse() {}

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

    public List<IngredientOrder> getData() {
        return data;
    }

    public void setData(List<IngredientOrder> data) {
        this.data = data;
    }
}
