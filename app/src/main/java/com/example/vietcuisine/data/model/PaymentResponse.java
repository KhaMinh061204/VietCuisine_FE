package com.example.vietcuisine.data.model;

public class PaymentResponse {
    private String sessionId;
    private String url;

    public PaymentResponse() {}

    public String getSessionId() {
        return sessionId;
    }

    public String getUrl() {
        return url;
    }

    public static class PaymentData {
        private String transactionId;
        private String orderId;
        private String status;
        private double amount;
        private String paymentMethod;
        private String timestamp;

        public PaymentData() {}

        public String getTransactionId() {
            return transactionId;
        }

        public void setTransactionId(String transactionId) {
            this.transactionId = transactionId;
        }

        public String getOrderId() {
            return orderId;
        }

        public void setOrderId(String orderId) {
            this.orderId = orderId;
        }

        public String getStatus() {
            return status;
        }

        public void setStatus(String status) {
            this.status = status;
        }

        public double getAmount() {
            return amount;
        }

        public void setAmount(double amount) {
            this.amount = amount;
        }

        public String getPaymentMethod() {
            return paymentMethod;
        }

        public void setPaymentMethod(String paymentMethod) {
            this.paymentMethod = paymentMethod;
        }

        public String getTimestamp() {
            return timestamp;
        }

        public void setTimestamp(String timestamp) {
            this.timestamp = timestamp;
        }
    }
}
