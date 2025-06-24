package com.example.vietcuisine.data.model;

import java.util.List;

public class PostDetailResponse {
    private boolean success;
    private String message;
    private Post data;

    public PostDetailResponse() {}

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

    public Post getData() {
        return data;
    }

    public void setData(Post data) {
        this.data = data;
    }
}
