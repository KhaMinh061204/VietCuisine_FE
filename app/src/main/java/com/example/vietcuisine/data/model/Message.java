package com.example.vietcuisine.data.model;

public class Message {
    private String _id;
    private String senderId;
    private String receiverId;
    private String text;
    private String createdAt;

    // Constructors
    public Message() {}

    public Message(String senderId, String receiverId, String text, String createdAt) {
        this.senderId = senderId;
        this.receiverId = receiverId;
        this.text = text;
        this.createdAt = createdAt;
    }

    // Getters and Setters
    public String getId() { return _id; }
    public void setId(String id) { this._id = id; }

    public String getSenderId() { return senderId; }
    public void setSenderId(String senderId) { this.senderId = senderId; }

    public String getReceiverId() { return receiverId; }
    public void setReceiverId(String receiverId) { this.receiverId = receiverId; }

    public String getText() { return text; }
    public void setText(String text) { this.text = text; }

    public String getCreatedAt() { return createdAt; }
    public void setCreatedAt(String createdAt) { this.createdAt = createdAt; }

}
