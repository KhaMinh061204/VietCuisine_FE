package com.example.vietcuisine.data.model;

import java.util.ArrayList;
import java.util.List;

public class Comment {
    private String _id;
    private String targetId;
    private String onModel;
    private User userId;
    private String content;
    private String createAt;
    private String updatedAt;
    private String parentId;
    private List<Comment> replies;

    public List<Comment> getReplies() {
        return replies != null ? replies : new ArrayList<>();
    }

    // Constructors
    public Comment() {}

    public Comment(String targetId, String onModel, User userId, String content,String parentId) {
        this.targetId = targetId;
        this.onModel = onModel;
        this.userId = userId;
        this.content = content;
        this.parentId=parentId;
    }

    // Getters and Setters
    public String getId() { return _id; }
    public void setId(String id) { this._id = id; }

    public String getTargetId() { return targetId; }
    public void setTargetId(String targetId) { this.targetId = targetId; }

    public String getOnModel() { return onModel; }
    public void setOnModel(String onModel) { this.onModel = onModel; }

    public User getUserId() { return userId; }
    public void setUserId(User userId) { this.userId = userId; }

    public String getContent() { return content; }
    public void setContent(String content) { this.content = content; }

    public String getCreateAt() { return createAt; }
    public void setCreateAt(String createAt) { this.createAt = createAt; }

    public String getUpdatedAt() { return updatedAt; }
    public void setUpdatedAt(String updatedAt) { this.updatedAt = updatedAt; }

    public String getParentId() {
        return parentId;
    }

    public void setParentId(String parentId) {
        this.parentId = parentId;
    }
}
