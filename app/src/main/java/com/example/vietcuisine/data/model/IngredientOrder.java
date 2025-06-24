package com.example.vietcuisine.data.model;

import java.util.List;

public class IngredientOrder {
    private String _id;
    private String userId;
    private List<OrderItem> items;
    private double totalCost;
    private String paymentMethod;
    private String paymentStatus;
    private ShippingAddress shippingAddress;
    private String status;
    private String createdAt;
    private String updatedAt;

    // Constructors
    public IngredientOrder() {}

    // Inner classes
    public static class OrderItem {
        private String ingredient;
        private int quantity;
        private double unitPriceAtTime;

        public OrderItem() {}

        public OrderItem(String ingredient, int quantity, double unitPriceAtTime) {
            this.ingredient = ingredient;
            this.quantity = quantity;
            this.unitPriceAtTime = unitPriceAtTime;
        }

        // Getters and Setters
        public String getIngredient() { return ingredient; }
        public void setIngredient(String ingredient) { this.ingredient = ingredient; }

        public int getQuantity() { return quantity; }
        public void setQuantity(int quantity) { this.quantity = quantity; }

        public double getUnitPriceAtTime() { return unitPriceAtTime; }
        public void setUnitPriceAtTime(double unitPriceAtTime) { this.unitPriceAtTime = unitPriceAtTime; }
    }

    public static class ShippingAddress {
        private String recipientName;
        private String phone;
        private String address;

        public ShippingAddress() {}

        public ShippingAddress(String recipientName, String phone, String address) {
            this.recipientName = recipientName;
            this.phone = phone;
            this.address = address;
        }

        // Getters and Setters
        public String getRecipientName() { return recipientName; }
        public void setRecipientName(String recipientName) { this.recipientName = recipientName; }

        public String getPhone() { return phone; }
        public void setPhone(String phone) { this.phone = phone; }

        public String getAddress() { return address; }
        public void setAddress(String address) { this.address = address; }
    }

    // Getters and Setters
    public String getId() { return _id; }
    public void setId(String id) { this._id = id; }

    public String getUserId() { return userId; }
    public void setUserId(String userId) { this.userId = userId; }

    public List<OrderItem> getItems() { return items; }
    public void setItems(List<OrderItem> items) { this.items = items; }

    public double getTotalCost() { return totalCost; }
    public void setTotalCost(double totalCost) { this.totalCost = totalCost; }

    public String getPaymentMethod() { return paymentMethod; }
    public void setPaymentMethod(String paymentMethod) { this.paymentMethod = paymentMethod; }

    public String getPaymentStatus() { return paymentStatus; }
    public void setPaymentStatus(String paymentStatus) { this.paymentStatus = paymentStatus; }

    public ShippingAddress getShippingAddress() { return shippingAddress; }
    public void setShippingAddress(ShippingAddress shippingAddress) { this.shippingAddress = shippingAddress; }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }

    public String getCreatedAt() { return createdAt; }
    public void setCreatedAt(String createdAt) { this.createdAt = createdAt; }

    public String getUpdatedAt() { return updatedAt; }
    public void setUpdatedAt(String updatedAt) { this.updatedAt = updatedAt; }
}
