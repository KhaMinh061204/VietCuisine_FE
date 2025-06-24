package com.example.vietcuisine.data.model;

public class Reel {
    private String _id;
    private String userOwner;
    private String caption;
    private String video;
    private int likesCount;
    private int commentsCount;
    private boolean isLiked;
    private User author;
    private String createdAt;
    private String updatedAt;

    // Constructors
    public Reel() {}

    public Reel(String userOwner, String caption, String video) {
        this.userOwner = userOwner;
        this.caption = caption;
        this.video = video;
    }

    // Getters and Setters
    public String getId() { return _id; }
    public void setId(String id) { this._id = id; }

    public String getUserOwner() { return userOwner; }
    public void setUserOwner(String userOwner) { this.userOwner = userOwner; }

    public String getCaption() { return caption; }
    public void setCaption(String caption) { this.caption = caption; }

    public String getVideo() { return video; }
    public void setVideo(String video) { this.video = video; }

    public int getLikesCount() { return likesCount; }
    public void setLikesCount(int likesCount) { this.likesCount = likesCount; }

    public int getCommentsCount() { return commentsCount; }
    public void setCommentsCount(int commentsCount) { this.commentsCount = commentsCount; }

    public boolean isLiked() { return isLiked; }
    public void setLiked(boolean liked) { this.isLiked = liked; }

    public User getAuthor() { return author; }
    public void setAuthor(User author) { this.author = author; }

    public String getCreatedAt() { return createdAt; }
    public void setCreatedAt(String createdAt) { this.createdAt = createdAt; }

    public String getUpdatedAt() { return updatedAt; }
    public void setUpdatedAt(String updatedAt) { this.updatedAt = updatedAt; }
}
