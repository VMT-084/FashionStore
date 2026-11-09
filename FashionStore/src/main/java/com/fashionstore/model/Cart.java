package com.fashionstore.model;

import java.time.LocalDateTime;

public class Cart {

    private int cartId;
    private int userId;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    // Default constructor
    public Cart() {
    }

    // Parameterized constructor
    public Cart(int cartId, int userId,
                LocalDateTime createdAt, LocalDateTime updatedAt) {
        this.cartId = cartId;
        this.userId = userId;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

    // Getters and Setters
    public int getCartId() {
        return cartId;
    }

    public void setCartId(int cartId) {
        this.cartId = cartId;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(LocalDateTime updatedAt) {
        this.updatedAt = updatedAt;
    }
}