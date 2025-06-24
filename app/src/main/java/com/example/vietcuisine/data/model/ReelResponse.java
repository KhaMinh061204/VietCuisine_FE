package com.example.vietcuisine.data.model;

import java.util.List;

public class ReelResponse {
    private boolean status;
    private String message;
    private List<Reel> reels;

    public ReelResponse() {}

    public boolean isStatus() { return status; }
    public void setStatus(boolean status) { this.status = status; }

    public String getMessage() { return message; }
    public void setMessage(String message) { this.message = message; }

    public List<Reel> getReels() { return reels; }
    public void setReels(List<Reel> reels) { this.reels = reels; }
}
