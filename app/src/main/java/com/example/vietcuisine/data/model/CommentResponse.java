package com.example.vietcuisine.data.model;

import java.util.List;

public class CommentResponse {
    private boolean success;
    private String message;
    private List<Comment> data;

    public CommentResponse() {}

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

    public List<Comment> getData() {
        return data;
    }

    public void setData(List<Comment> data) {
        this.data = data;
    }
}
