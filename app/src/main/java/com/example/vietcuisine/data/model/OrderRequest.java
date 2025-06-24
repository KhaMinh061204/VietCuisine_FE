package com.example.vietcuisine.data.model;

import java.util.List;

public class OrderRequest {
    private List<OrderItem> items;
    private String deliveryAddress;
    private String paymentMethod;
    private String notes;

    public OrderRequest() {}

    public List<OrderItem> getItems() {
        return items;
    }

    public void setItems(List<OrderItem> items) {
        this.items = items;
    }

    public String getDeliveryAddress() {
        return deliveryAddress;
    }

    public void setDeliveryAddress(String deliveryAddress) {
        this.deliveryAddress = deliveryAddress;
    }

    public String getPaymentMethod() {
        return paymentMethod;
    }

    public void setPaymentMethod(String paymentMethod) {
        this.paymentMethod = paymentMethod;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }

    public static class OrderItem {
        private String ingredientId;
        private int quantity;
        private double unitPrice;

        public OrderItem() {}

        public OrderItem(String ingredientId, int quantity, double unitPrice) {
            this.ingredientId = ingredientId;
            this.quantity = quantity;
            this.unitPrice = unitPrice;
        }

        public String getIngredientId() {
            return ingredientId;
        }

        public void setIngredientId(String ingredientId) {
            this.ingredientId = ingredientId;
        }

        public int getQuantity() {
            return quantity;
        }

        public void setQuantity(int quantity) {
            this.quantity = quantity;
        }

        public double getUnitPrice() {
            return unitPrice;
        }

        public void setUnitPrice(double unitPrice) {
            this.unitPrice = unitPrice;
        }
    }
}
