package com.example.vietcuisine.data.model;

public class Ingredient {
    private String _id;
    private String name;
    private String unit;
    private double unitPrice;
    private String category;
    private int stock;    private String imageUrl;
    private String createdAt;
    private String updatedAt;
    private int quantity = 0; // For cart functionality

    // Constructors
    public Ingredient() {}

    // Copy constructor for cart functionality
    public Ingredient(Ingredient other) {
        this._id = other._id;
        this.name = other.name;
        this.unit = other.unit;
        this.unitPrice = other.unitPrice;
        this.category = other.category;
        this.stock = other.stock;
        this.imageUrl = other.imageUrl;
        this.createdAt = other.createdAt;
        this.updatedAt = other.updatedAt;
        this.quantity = other.quantity;
    }

    public Ingredient(String name, String unit, double unitPrice, String category, int stock, String imageUrl) {
        this.name = name;
        this.unit = unit;
        this.unitPrice = unitPrice;
        this.category = category;
        this.stock = stock;
        this.imageUrl = imageUrl;
    }

    // Getters and Setters
    public String getId() { return _id; }
    public void setId(String id) { this._id = id; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getUnit() { return unit; }
    public void setUnit(String unit) { this.unit = unit; }

    public double getUnitPrice() { return unitPrice; }
    public void setUnitPrice(double unitPrice) { this.unitPrice = unitPrice; }

    public String getCategory() { return category; }
    public void setCategory(String category) { this.category = category; }

    public int getStock() { return stock; }
    public void setStock(int stock) { this.stock = stock; }

    public String getImageUrl() { return imageUrl; }
    public void setImageUrl(String imageUrl) { this.imageUrl = imageUrl; }

    public String getCreatedAt() { return createdAt; }
    public void setCreatedAt(String createdAt) { this.createdAt = createdAt; }    public String getUpdatedAt() { return updatedAt; }
    public void setUpdatedAt(String updatedAt) { this.updatedAt = updatedAt; }

    public int getQuantity() { return quantity; }
    public void setQuantity(int quantity) { this.quantity = quantity; }
}
