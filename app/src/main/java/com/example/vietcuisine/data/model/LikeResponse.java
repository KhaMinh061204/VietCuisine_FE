package com.example.vietcuisine.data.model;

import java.util.List;

public class LikeResponse {
    private boolean success;
    private String message;
    private List<LikeRequest> data;

    public LikeResponse() {}

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

    public List<LikeRequest> getData() {
        return data;
    }

    public void setData(List<LikeRequest> data) {
        this.data = data;
    }
}
