package com.example.vietcuisine.data.model;

import java.util.List;

public class MessageResponse {
    private boolean success;
    private String message;
    private List<Message> data;

    public MessageResponse() {}

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

    public List<Message> getData() {
        return data;
    }

    public void setData(List<Message> data) {
        this.data = data;
    }
}
