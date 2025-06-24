package com.example.vietcuisine.data.model;

public class Comment {
    private String _id;
    private String targetId;
    private String onModel;
    private String userId;
    private String content;
    private String createdAt;
    private String updatedAt;

    // Constructors
    public Comment() {}

    public Comment(String targetId, String onModel, String userId, String content) {
        this.targetId = targetId;
        this.onModel = onModel;
        this.userId = userId;
        this.content = content;
    }

    // Getters and Setters
    public String getId() { return _id; }
    public void setId(String id) { this._id = id; }

    public String getTargetId() { return targetId; }
    public void setTargetId(String targetId) { this.targetId = targetId; }

    public String getOnModel() { return onModel; }
    public void setOnModel(String onModel) { this.onModel = onModel; }

    public String getUserId() { return userId; }
    public void setUserId(String userId) { this.userId = userId; }

    public String getContent() { return content; }
    public void setContent(String content) { this.content = content; }

    public String getCreatedAt() { return createdAt; }
    public void setCreatedAt(String createdAt) { this.createdAt = createdAt; }

    public String getUpdatedAt() { return updatedAt; }
    public void setUpdatedAt(String updatedAt) { this.updatedAt = updatedAt; }
}
