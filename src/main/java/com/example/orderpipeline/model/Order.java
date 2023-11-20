package com.example.orderpipeline.model;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public class Order {

    private String orderId;
    private String customerName;
    private String product;
    private int quantity;
    private BigDecimal price;
    private OrderStatus status;
    private LocalDateTime createdAt;

    public Order() {
        this.status = OrderStatus.PENDING;
        this.createdAt = LocalDateTime.now();
    }

    public Order(String orderId, String customerName, String product, int quantity, BigDecimal price) {
        this();
        this.orderId = orderId;
        this.customerName = customerName;
        this.product = product;
        this.quantity = quantity;
        this.price = price;
    }

    public String getOrderId() { return orderId; }
    public void setOrderId(String orderId) { this.orderId = orderId; }

    public String getCustomerName() { return customerName; }
    public void setCustomerName(String customerName) { this.customerName = customerName; }

    public String getProduct() { return product; }
    public void setProduct(String product) { this.product = product; }

    public int getQuantity() { return quantity; }
    public void setQuantity(int quantity) { this.quantity = quantity; }

    public BigDecimal getPrice() { return price; }
    public void setPrice(BigDecimal price) { this.price = price; }

    public OrderStatus getStatus() { return status; }
    public void setStatus(OrderStatus status) { this.status = status; }

    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }

    @Override
    public String toString() {
        return "Order{orderId='" + orderId + "', customer='" + customerName +
               "', product='" + product + "', qty=" + quantity +
               ", price=" + price + ", status=" + status + "}";
    }
}
