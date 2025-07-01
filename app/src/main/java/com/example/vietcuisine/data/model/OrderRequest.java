package com.example.vietcuisine.data.model;
import com.example.vietcuisine.data.model.IngredientOrder.ShippingAddress;
import java.util.List;

public class OrderRequest {
    private List<OrderItem> orders;
    private ShippingAddress shippingAddress;
    private String paymentMethod;

    public OrderRequest() {}

    public OrderRequest(List<OrderItem> orders, ShippingAddress shippingAddress, String paymentMethod) {
        this.orders = orders;
        this.shippingAddress = shippingAddress;
        this.paymentMethod = paymentMethod;
    }

    public List<OrderItem> getOrders() {
        return orders;
    }

    public void setOrders(List<OrderItem> orders) {
        this.orders = orders;
    }

    public ShippingAddress getShippingAddress() {
        return shippingAddress;
    }

    public void setShippingAddress(ShippingAddress shippingAddress) {
        this.shippingAddress = shippingAddress;
    }

    public String getPaymentMethod() {
        return paymentMethod;
    }

    public void setPaymentMethod(String paymentMethod) {
        this.paymentMethod = paymentMethod;
    }

    public static class OrderItem {
        private String ingredientId;
        private int quantity;

        public OrderItem() {}

        public OrderItem(String ingredientId, int quantity) {
            this.ingredientId = ingredientId;
            this.quantity = quantity;
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

    }
}
