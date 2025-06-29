package com.example.vietcuisine.data.model;

public class ReportRequest {
    private String targetType;
    private String targetId;
    private String reason;

    public ReportRequest(String targetType, String targetId, String reason) {
        this.targetType = targetType;
        this.targetId = targetId;
        this.reason = reason;
    }

    public String getTargetType(){return targetType;}

    public String getTargetId() {
        return targetId;
    }

    public String getReason() {
        return reason;
    }

    public void setTargetType(String targetType) {
        this.targetType = targetType;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }

    public void setTargetId(String targetId) {
        this.targetId = targetId;
    }
}
