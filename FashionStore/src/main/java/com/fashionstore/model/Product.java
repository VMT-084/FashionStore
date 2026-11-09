
package com.fashionstore.model;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public class Product {

    private int productId;
    private int categoryId;
    private String productName;
    private String description;
    private BigDecimal price;
    private String imageUrl;
    private boolean isActive;
    private LocalDateTime createdAt;

    // Default Constructor
    public Product() {
    }

    // Parameterized Constructor
    public Product(int productId, int categoryId,
                   String productName, String description,
                   BigDecimal price, String imageUrl,
                   boolean isActive, LocalDateTime createdAt) {

        this.productId = productId;
        this.categoryId = categoryId;
        this.productName = productName;
        this.description = description;
        this.price = price;
        this.imageUrl = imageUrl;
        this.isActive = isActive;
        this.createdAt = createdAt;
    }

    // Getters and Setters

    public int getProductId() {
        return productId;
    }

    public void setProductId(int productId) {
        this.productId = productId;
    }

    public int getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(int categoryId) {
        this.categoryId = categoryId;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public boolean isActive() {
        return isActive;
    }

    public void setActive(boolean active) {
        isActive = active;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }
}
