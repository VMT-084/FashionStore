
package com.fashionstore.dao;

import com.fashionstore.model.Product;
import java.math.BigDecimal;
import java.sql.SQLException;
import java.util.List;

public interface ProductDAO {

    // Retrieve all active products
    List<Product> getAllProducts() throws SQLException;

    // Retrieve a product using its ID
    Product getProductById(int productId) throws SQLException;

    // Retrieve products belonging to a category
    List<Product> getProductsByCategory(int categoryId)
            throws SQLException;

    // Search products by name or description
    List<Product> searchProducts(String keyword) throws SQLException;

    // Filter products by category and price range
    List<Product> filterProducts(
            Integer categoryId,
            BigDecimal minPrice,
            BigDecimal maxPrice
    ) throws SQLException;

    // Retrieve products with sorting and pagination
    List<Product> getProducts(
            String keyword,
            Integer categoryId,
            BigDecimal minPrice,
            BigDecimal maxPrice,
            String sortBy,
            String sortOrder,
            int page,
            int pageSize
    ) throws SQLException;

    // Count products matching the given search and filter criteria
    int countProducts(
            String keyword,
            Integer categoryId,
            BigDecimal minPrice,
            BigDecimal maxPrice
    ) throws SQLException;

    // Retrieve featured products for the homepage
    List<Product> getFeaturedProducts(int limit) throws SQLException;

    // Retrieve recently added products
    List<Product> getLatestProducts(int limit) throws SQLException;

    // Add a new product
    boolean addProduct(Product product) throws SQLException;

    // Update product details
    boolean updateProduct(Product product) throws SQLException;

    // Delete a product
    boolean deleteProduct(int productId) throws SQLException;

    // Activate or deactivate a product
    boolean updateProductStatus(int productId, boolean isActive)
            throws SQLException;

    // Check whether a product exists
    boolean productExists(int productId) throws SQLException;
}
