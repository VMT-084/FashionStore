
package com.fashionstore.model;

import java.math.BigDecimal;

public class OrderItem {

    private int orderItemId;
    private int orderId;
    private int productId;
    private int variantId;

    private String productName;
    private String size;
    private String color;

    private int quantity;

    private BigDecimal unitPrice;
    private BigDecimal subtotal;

    // Default constructor
    public OrderItem() {
    }

    // Parameterized constructor
    public OrderItem(int orderItemId, int orderId, int productId,
                     int variantId, String productName, String size,
                     String color, int quantity,
                     BigDecimal unitPrice, BigDecimal subtotal) {

        this.orderItemId = orderItemId;
        this.orderId = orderId;
        this.productId = productId;
        this.variantId = variantId;
        this.productName = productName;
        this.size = size;
        this.color = color;
        this.quantity = quantity;
        this.unitPrice = unitPrice;
        this.subtotal = subtotal;
    }

    // Getters and Setters
    public int getOrderItemId() {
        return orderItemId;
    }

    public void setOrderItemId(int orderItemId) {
        this.orderItemId = orderItemId;
    }

    public int getOrderId() {
        return orderId;
    }

    public void setOrderId(int orderId) {
        this.orderId = orderId;
    }

    public int getProductId() {
        return productId;
    }

    public void setProductId(int productId) {
        this.productId = productId;
    }

    public int getVariantId() {
        return variantId;
    }

    public void setVariantId(int variantId) {
        this.variantId = variantId;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public String getSize() {
        return size;
    }

    public void setSize(String size) {
        this.size = size;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public BigDecimal getUnitPrice() {
        return unitPrice;
    }

    public void setUnitPrice(BigDecimal unitPrice) {
        this.unitPrice = unitPrice;
    }

    public BigDecimal getSubtotal() {
        return subtotal;
    }

    public void setSubtotal(BigDecimal subtotal) {
        this.subtotal = subtotal;
    }
}
