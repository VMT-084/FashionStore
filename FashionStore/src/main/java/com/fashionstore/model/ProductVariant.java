package com.fashionstore.model;

public class ProductVariant {

    private int variantId;
    private int productId;
    private String size;
    private String color;
    private int stockQuantity;
    private String sku;

    // Default constructor
    public ProductVariant() {
    }

    // Parameterized constructor
    public ProductVariant(int variantId, int productId, String size,
                          String color, int stockQuantity, String sku) {
        this.variantId = variantId;
        this.productId = productId;
        this.size = size;
        this.color = color;
        this.stockQuantity = stockQuantity;
        this.sku = sku;
    }

    // Getters and Setters
    public int getVariantId() {
        return variantId;
    }

    public void setVariantId(int variantId) {
        this.variantId = variantId;
    }

    public int getProductId() {
        return productId;
    }

    public void setProductId(int productId) {
        this.productId = productId;
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

    public int getStockQuantity() {
        return stockQuantity;
    }

    public void setStockQuantity(int stockQuantity) {
        this.stockQuantity = stockQuantity;
    }

    public String getSku() {
        return sku;
    }

    public void setSku(String sku) {
        this.sku = sku;
    }
}