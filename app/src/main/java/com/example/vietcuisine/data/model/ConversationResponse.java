package com.example.vietcuisine.data.model;

import java.util.List;

public class ConversationResponse {
    private boolean success;
    private String message;
    private List<Conversation> data;

    public ConversationResponse() {}

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public List<Conversation> getData() {
        return data;
    }

    public void setData(List<Conversation> data) {
        this.data = data;
    }

    public static class Conversation {
        private String id;
        private String userId;
        private String userName;
        private String userAvatar;
        private String lastMessage;
        private String lastMessageTime;
        private boolean isOnline;
        private int unreadCount;

        public Conversation() {}

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getUserId() {
            return userId;
        }

        public void setUserId(String userId) {
            this.userId = userId;
        }

        public String getUserName() {
            return userName;
        }

        public void setUserName(String userName) {
            this.userName = userName;
        }

        public String getUserAvatar() {
            return userAvatar;
        }

        public void setUserAvatar(String userAvatar) {
            this.userAvatar = userAvatar;
        }

        public String getLastMessage() {
            return lastMessage;
        }

        public void setLastMessage(String lastMessage) {
            this.lastMessage = lastMessage;
        }

        public String getLastMessageTime() {
            return lastMessageTime;
        }

        public void setLastMessageTime(String lastMessageTime) {
            this.lastMessageTime = lastMessageTime;
        }

        public boolean isOnline() {
            return isOnline;
        }

        public void setOnline(boolean online) {
            isOnline = online;
        }

        public int getUnreadCount() {
            return unreadCount;
        }

        public void setUnreadCount(int unreadCount) {
            this.unreadCount = unreadCount;
        }
    }
}
