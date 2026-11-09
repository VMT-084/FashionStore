
package com.fashionstore.dao;

import com.fashionstore.model.ProductVariant;
import java.sql.SQLException;
import java.util.List;

public interface ProductVariantDAO {

    // Retrieve all variants belonging to a product
    List<ProductVariant> getVariantsByProductId(int productId)
            throws SQLException;

    // Retrieve a variant using its ID
    ProductVariant getVariantById(int variantId) throws SQLException;

    // Retrieve a variant using product, size and colour
    ProductVariant getVariantByAttributes(
            int productId, String size, String color
    ) throws SQLException;

    // Retrieve a variant using its SKU
    ProductVariant getVariantBySKU(String sku) throws SQLException;

    // Retrieve available sizes for a product
    List<String> getAvailableSizes(int productId) throws SQLException;

    // Retrieve available colours for a product
    List<String> getAvailableColors(int productId) throws SQLException;

    // Check whether a variant has sufficient stock
    boolean isStockAvailable(int variantId, int quantity)
            throws SQLException;

    // Retrieve the available stock quantity
    int getStockQuantity(int variantId) throws SQLException;

    // Add a new product variant
    boolean addVariant(ProductVariant variant) throws SQLException;

    // Update variant details
    boolean updateVariant(ProductVariant variant) throws SQLException;

    // Update the stock quantity
    boolean updateStock(int variantId, int quantity)
            throws SQLException;

    // Increase stock quantity
    boolean increaseStock(int variantId, int quantity)
            throws SQLException;

    // Decrease stock quantity
    boolean decreaseStock(int variantId, int quantity)
            throws SQLException;

    // Check whether an SKU already exists
    boolean skuExists(String sku) throws SQLException;

    // Delete a product variant
    boolean deleteVariant(int variantId) throws SQLException;
}
