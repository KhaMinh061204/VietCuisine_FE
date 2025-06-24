package com.example.vietcuisine.data.model;

import java.util.List;

public class Recipe {
    private String _id;
    private String title;
    private String description;
    private String image;
    private int time;
    private double carbs;
    private double protein;
    private double calories;
    private double fat;
    private String categoriesId;
    private String userOwner;
    private List<RecipeIngredient> ingredients;
    private List<String> steps;
    private List<String> likes;
    private List<String> saves;
    private List<Comment> comments;
    private int likesCount;
    private int savesCount;
    private int commentsCount;
    private String createdAt;
    private String updatedAt;

    // Constructors
    public Recipe() {}

    // Getters and Setters
    public String getId() { return _id; }
    public void setId(String id) { this._id = id; }

    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public String getImage() { return image; }
    public void setImage(String image) { this.image = image; }

    public int getTime() { return time; }
    public void setTime(int time) { this.time = time; }

    public double getCarbs() { return carbs; }
    public void setCarbs(double carbs) { this.carbs = carbs; }

    public double getProtein() { return protein; }
    public void setProtein(double protein) { this.protein = protein; }

    public double getCalories() { return calories; }
    public void setCalories(double calories) { this.calories = calories; }

    public double getFat() { return fat; }
    public void setFat(double fat) { this.fat = fat; }

    public String getCategoriesId() { return categoriesId; }
    public void setCategoriesId(String categoriesId) { this.categoriesId = categoriesId; }

    public String getUserOwner() { return userOwner; }
    public void setUserOwner(String userOwner) { this.userOwner = userOwner; }

    public List<RecipeIngredient> getIngredients() { return ingredients; }
    public void setIngredients(List<RecipeIngredient> ingredients) { this.ingredients = ingredients; }

    public List<String> getSteps() { return steps; }
    public void setSteps(List<String> steps) { this.steps = steps; }

    public List<String> getLikes() { return likes; }
    public void setLikes(List<String> likes) { this.likes = likes; }

    public List<String> getSaves() { return saves; }
    public void setSaves(List<String> saves) { this.saves = saves; }

    public List<Comment> getComments() { return comments; }
    public void setComments(List<Comment> comments) { this.comments = comments; }

    public int getLikesCount() { return likesCount; }
    public void setLikesCount(int likesCount) { this.likesCount = likesCount; }

    public int getSavesCount() { return savesCount; }
    public void setSavesCount(int savesCount) { this.savesCount = savesCount; }

    public int getCommentsCount() { return commentsCount; }
    public void setCommentsCount(int commentsCount) { this.commentsCount = commentsCount; }

    public String getCreatedAt() { return createdAt; }
    public void setCreatedAt(String createdAt) { this.createdAt = createdAt; }

    public String getUpdatedAt() { return updatedAt; }
    public void setUpdatedAt(String updatedAt) { this.updatedAt = updatedAt; }
}
