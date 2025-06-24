package com.example.vietcuisine.data.model;

public class LikeRequest {
    private String targetId;
    private String onModel;

    public LikeRequest(String targetId, String onModel) {
        this.targetId = targetId;
        this.onModel = onModel;
    }

    // Getters and Setters
    public String getTargetId() { return targetId; }
    public void setTargetId(String targetId) { this.targetId = targetId; }

    public String getOnModel() { return onModel; }
    public void setOnModel(String onModel) { this.onModel = onModel; }
}
