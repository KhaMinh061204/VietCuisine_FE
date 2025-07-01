package com.example.vietcuisine.data.model;

import java.math.BigDecimal;
import java.util.List;

public class OrderListResponse {
    private String message;
    private List<IngredientOrder> orders;
    private String orderId;
    private BigDecimal totalCost;
    private IngredientOrder.ShippingAddress shippingAddress;
    private String paymentStatus;
    private String paymentMethod;

    public OrderListResponse(){};
    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public List<IngredientOrder> getOrders() {
        return orders;
    }

    public void setData(List<IngredientOrder> data) {
        this.orders = data;
    }

    public String getOrderId() {
        return orderId;
    }

    public BigDecimal getTotalCost() {
        return totalCost;
    }

    public IngredientOrder.ShippingAddress getShippingAddress() {
        return shippingAddress;
    }

    public String getPaymentMethod() {
        return paymentMethod;
    }

    public String getPaymentStatus() {
        return paymentStatus;
    }
}
