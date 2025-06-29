package com.example.vietcuisine.data.model;

public class LikeRequest {
    private String targetId;
    private String onModel;

    public LikeRequest(String onModel,String targetId) {
        this.onModel = onModel;
        this.targetId = targetId;
    }

    // Getters and Setters
    public String getTargetId() { return targetId; }
    public void setTargetId(String targetId) { this.targetId = targetId; }

    public String getOnModel() { return onModel; }
    public void setOnModel(String onModel) { this.onModel = onModel; }
}
