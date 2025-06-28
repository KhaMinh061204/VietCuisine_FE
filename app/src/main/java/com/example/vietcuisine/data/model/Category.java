package com.example.vietcuisine.data.model;

public class Category {
    private String _id;
    private String name;
    private String imageUrl;

    // Constructors
    public Category() {}

    public Category(String name,String imageUrl) {
        this.name = name;
        this.imageUrl = imageUrl;
    }

    // Getters and Setters
    public String getId() { return _id; }
    public void setId(String id) { this._id = id; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getImageUrl() { return imageUrl; }
    public void setImageUrl(String imageUrl) { this.imageUrl = imageUrl; }

}
