package com.example.vietcuisine.data.model;

import java.util.List;

public class PostResponse {
    private boolean status;
    private String message;
    private List<Post> posts;

    public PostResponse() {}

    public boolean isStatus() { return status; }
    public void setStatus(boolean status) { this.status = status; }

    public String getMessage() { return message; }
    public void setMessage(String message) { this.message = message; }

    public List<Post> getPosts() { return posts; }
    public void setPosts(List<Post> posts) { this.posts = posts; }
}
