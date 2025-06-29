package com.example.vietcuisine.data.model;

public class CommentRequest {
    private String content;
    private String targetId;
    private String onModel;
    private String parentId; //

    public CommentRequest(String content, String targetId, String targetType) {
        this.content = content;
        this.targetId = targetId;
        this.onModel = targetType;
    }

    public CommentRequest(String content, String targetId, String targetType, String parentId) {
        this(content, targetId, targetType);
        this.parentId = parentId;
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

    public void setOnModel(String targetType) {
        this.onModel = targetType;
    }
    public String getParentId() {
        return parentId;
    }
    public void setParentId(String parentId) {
        this.parentId = parentId;
    }
}
