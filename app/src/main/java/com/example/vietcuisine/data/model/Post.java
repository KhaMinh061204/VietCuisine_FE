package com.example.vietcuisine.data.model;

public class Post {
    private String _id;
    private String userId;
    private String caption;
    private String image;    private String recipeId;
    private int likesCount;
    private int commentsCount;
    private boolean isLiked;
    private String createdAt;
    private String updatedAt;

    // Constructors
    public Post() {}

    public Post(String userId, String caption, String image, String recipeId) {
        this.userId = userId;
        this.caption = caption;
        this.image = image;
        this.recipeId = recipeId;
    }

    // Getters and Setters
    public String getId() { return _id; }
    public void setId(String id) { this._id = id; }

    public String getUserId() { return userId; }
    public void setUserId(String userId) { this.userId = userId; }

    public String getCaption() { return caption; }
    public void setCaption(String caption) { this.caption = caption; }

    public String getImage() { return image; }
    public void setImage(String image) { this.image = image; }

    public String getRecipeId() { return recipeId; }
    public void setRecipeId(String recipeId) { this.recipeId = recipeId; }

    public int getLikesCount() { return likesCount; }
    public void setLikesCount(int likesCount) { this.likesCount = likesCount; }    public int getCommentsCount() { return commentsCount; }
    public void setCommentsCount(int commentsCount) { this.commentsCount = commentsCount; }

    public boolean isLiked() { return isLiked; }
    public void setLiked(boolean liked) { this.isLiked = liked; }

    public String getCreatedAt() { return createdAt; }
    public void setCreatedAt(String createdAt) { this.createdAt = createdAt; }

    public String getUpdatedAt() { return updatedAt; }
    public void setUpdatedAt(String updatedAt) { this.updatedAt = updatedAt; }
}
