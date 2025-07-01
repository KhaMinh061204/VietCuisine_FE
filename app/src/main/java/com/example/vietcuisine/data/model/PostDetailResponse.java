package com.example.vietcuisine.data.model;

import java.util.List;

public class PostDetailResponse {
    private String message;
    private Post data;

    public PostDetailResponse() {}

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
