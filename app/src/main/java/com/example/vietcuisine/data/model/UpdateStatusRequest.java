package com.example.vietcuisine.data.model;

public class UpdateStatusRequest {
    private String status;
    private String reason;

    public UpdateStatusRequest() {}

    public UpdateStatusRequest(String status, String reason) {
        this.status = status;
        this.reason = reason;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }
}
