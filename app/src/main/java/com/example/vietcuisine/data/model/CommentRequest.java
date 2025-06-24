package com.example.vietcuisine.data.model;

public class CommentRequest {
    private String content;
    private String targetId;
    private String onModel; // "Recipe", "Post", "Reel"

    public CommentRequest() {}

    public CommentRequest(String content, String targetId, String onModel) {
        this.content = content;
        this.targetId = targetId;
        this.onModel = onModel;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getTargetId() {
        return targetId;
    }

    public void setTargetId(String targetId) {
        this.targetId = targetId;
    }

    public String getOnModel() {
        return onModel;
    }

    public void setOnModel(String onModel) {
        this.onModel = onModel;
    }
}
