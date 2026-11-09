package com.fashionstore.model;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public class Order {

    private int orderId;
    private int userId;
    private LocalDateTime orderDate;
    private BigDecimal totalAmount;

    private String paymentMethod;
    private String paymentStatus;
    private String orderStatus;

    private String deliveryName;
    private String deliveryPhone;
    private String deliveryAddress;
    private String deliveryCity;
    private String deliveryState;
    private String deliveryPincode;

    // Default constructor
    public Order() {
    }

    // Parameterized constructor
    public Order(int orderId, int userId, LocalDateTime orderDate,
                 BigDecimal totalAmount, String paymentMethod,
                 String paymentStatus, String orderStatus,
                 String deliveryName, String deliveryPhone,
                 String deliveryAddress, String deliveryCity,
                 String deliveryState, String deliveryPincode) {

        this.orderId = orderId;
        this.userId = userId;
        this.orderDate = orderDate;
        this.totalAmount = totalAmount;
        this.paymentMethod = paymentMethod;
        this.paymentStatus = paymentStatus;
        this.orderStatus = orderStatus;
        this.deliveryName = deliveryName;
        this.deliveryPhone = deliveryPhone;
        this.deliveryAddress = deliveryAddress;
        this.deliveryCity = deliveryCity;
        this.deliveryState = deliveryState;
        this.deliveryPincode = deliveryPincode;
    }

    // Getters and Setters
    public int getOrderId() {
        return orderId;
    }

    public void setOrderId(int orderId) {
        this.orderId = orderId;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public LocalDateTime getOrderDate() {
        return orderDate;
    }

    public void setOrderDate(LocalDateTime orderDate) {
        this.orderDate = orderDate;
    }

    public BigDecimal getTotalAmount() {
        return totalAmount;
    }

    public void setTotalAmount(BigDecimal totalAmount) {
        this.totalAmount = totalAmount;
    }

    public String getPaymentMethod() {
        return paymentMethod;
    }

    public void setPaymentMethod(String paymentMethod) {
        this.paymentMethod = paymentMethod;
    }

    public String getPaymentStatus() {
        return paymentStatus;
    }

    public void setPaymentStatus(String paymentStatus) {
        this.paymentStatus = paymentStatus;
    }

    public String getOrderStatus() {
        return orderStatus;
    }

    public void setOrderStatus(String orderStatus) {
        this.orderStatus = orderStatus;
    }

    public String getDeliveryName() {
        return deliveryName;
    }

    public void setDeliveryName(String deliveryName) {
        this.deliveryName = deliveryName;
    }

    public String getDeliveryPhone() {
        return deliveryPhone;
    }

    public void setDeliveryPhone(String deliveryPhone) {
        this.deliveryPhone = deliveryPhone;
    }

    public String getDeliveryAddress() {
        return deliveryAddress;
    }

    public void setDeliveryAddress(String deliveryAddress) {
        this.deliveryAddress = deliveryAddress;
    }

    public String getDeliveryCity() {
        return deliveryCity;
    }

    public void setDeliveryCity(String deliveryCity) {
        this.deliveryCity = deliveryCity;
    }

    public String getDeliveryState() {
        return deliveryState;
    }

    public void setDeliveryState(String deliveryState) {
        this.deliveryState = deliveryState;
    }

    public String getDeliveryPincode() {
        return deliveryPincode;
    }

    public void setDeliveryPincode(String deliveryPincode) {
        this.deliveryPincode = deliveryPincode;
    }
}