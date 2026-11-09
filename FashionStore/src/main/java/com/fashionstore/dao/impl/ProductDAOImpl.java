
package com.fashionstore.dao.impl;

import com.fashionstore.dao.ProductDAO;
import com.fashionstore.model.Product;
import com.fashionstore.util.DBConnection;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

public class ProductDAOImpl implements ProductDAO {

    // Retrieve all active products
    @Override
    public List<Product> getAllProducts() throws SQLException {

        String sql = "SELECT * FROM products "
                   + "WHERE is_active = TRUE ORDER BY created_at DESC";

        return executeProductQuery(sql, new ArrayList<>());
    }

    // Retrieve a product using its ID
    @Override
    public Product getProductById(int productId) throws SQLException {

        String sql = "SELECT * FROM products "
                   + "WHERE product_id = ? AND is_active = TRUE";

        List<Object> parameters = new ArrayList<>();
        parameters.add(productId);

        List<Product> products = executeProductQuery(sql, parameters);

        return products.isEmpty() ? null : products.get(0);
    }

    // Retrieve products belonging to a category
    @Override
    public List<Product> getProductsByCategory(int categoryId)
            throws SQLException {

        return filterProducts(categoryId, null, null);
    }

    // Search products by name or description
    @Override
    public List<Product> searchProducts(String keyword)
            throws SQLException {

        String sql = "SELECT * FROM products "
                   + "WHERE is_active = TRUE "
                   + "AND (product_name LIKE ? OR description LIKE ?) "
                   + "ORDER BY created_at DESC";

        String searchTerm = "%" + keyword.trim() + "%";

        List<Object> parameters = new ArrayList<>();
        parameters.add(searchTerm);
        parameters.add(searchTerm);

        return executeProductQuery(sql, parameters);
    }

    // Filter products by category and price range
    @Override
    public List<Product> filterProducts(
            Integer categoryId,
            BigDecimal minPrice,
            BigDecimal maxPrice) throws SQLException {

        StringBuilder sql = new StringBuilder(
                "SELECT * FROM products WHERE is_active = TRUE");

        List<Object> parameters = new ArrayList<>();

        if (categoryId != null) {
            sql.append(" AND category_id = ?");
            parameters.add(categoryId);
        }

        if (minPrice != null) {
            sql.append(" AND price >= ?");
            parameters.add(minPrice);
        }

        if (maxPrice != null) {
            sql.append(" AND price <= ?");
            parameters.add(maxPrice);
        }

        sql.append(" ORDER BY created_at DESC");

        return executeProductQuery(sql.toString(), parameters);
    }

    // Retrieve products with search, filters, sorting and pagination
    @Override
    public List<Product> getProducts(
            String keyword,
            Integer categoryId,
            BigDecimal minPrice,
            BigDecimal maxPrice,
            String sortBy,
            String sortOrder,
            int page,
            int pageSize) throws SQLException {

        StringBuilder sql = new StringBuilder(
                "SELECT * FROM products WHERE is_active = TRUE");

        List<Object> parameters = new ArrayList<>();

        // Search condition
        if (keyword != null && !keyword.trim().isEmpty()) {
            sql.append(" AND (product_name LIKE ? OR description LIKE ?)");

            String searchTerm = "%" + keyword.trim() + "%";

            parameters.add(searchTerm);
            parameters.add(searchTerm);
        }

        // Category condition
        if (categoryId != null) {
            sql.append(" AND category_id = ?");
            parameters.add(categoryId);
        }

        // Minimum price condition
        if (minPrice != null) {
            sql.append(" AND price >= ?");
            parameters.add(minPrice);
        }

        // Maximum price condition
        if (maxPrice != null) {
            sql.append(" AND price <= ?");
            parameters.add(maxPrice);
        }

        // Allow only predefined sorting columns
        String sortColumn;

        if (sortBy == null) {
            sortColumn = "created_at";
        } else {
            switch (sortBy.toLowerCase()) {
                case "price":
                    sortColumn = "price";
                    break;

                case "name":
                    sortColumn = "product_name";
                    break;

                case "newest":
                case "created_at":
                    sortColumn = "created_at";
                    break;

                default:
                    sortColumn = "created_at";
            }
        }

        // Validate sorting direction
        String direction = "ASC";

        if (sortOrder != null
                && sortOrder.equalsIgnoreCase("DESC")) {
            direction = "DESC";
        }

        sql.append(" ORDER BY ")
           .append(sortColumn)
           .append(" ")
           .append(direction);

        // Validate pagination values
        if (page < 1) {
            throw new IllegalArgumentException(
                    "Page number must be at least 1");
        }

        if (pageSize < 1 || pageSize > 100) {
            throw new IllegalArgumentException(
                    "Page size must be between 1 and 100");
        }

        int offset = (page - 1) * pageSize;

        sql.append(" LIMIT ? OFFSET ?");

        parameters.add(pageSize);
        parameters.add(offset);

        return executeProductQuery(sql.toString(), parameters);
    }

    // Count products matching search and filter conditions
    @Override
    public int countProducts(
            String keyword,
            Integer categoryId,
            BigDecimal minPrice,
            BigDecimal maxPrice) throws SQLException {

        StringBuilder sql = new StringBuilder(
                "SELECT COUNT(*) FROM products WHERE is_active = TRUE");

        List<Object> parameters = new ArrayList<>();

        if (keyword != null && !keyword.trim().isEmpty()) {
            sql.append(" AND (product_name LIKE ? OR description LIKE ?)");

            String searchTerm = "%" + keyword.trim() + "%";

            parameters.add(searchTerm);
            parameters.add(searchTerm);
        }

        if (categoryId != null) {
            sql.append(" AND category_id = ?");
            parameters.add(categoryId);
        }

        if (minPrice != null) {
            sql.append(" AND price >= ?");
            parameters.add(minPrice);
        }

        if (maxPrice != null) {
            sql.append(" AND price <= ?");
            parameters.add(maxPrice);
        }

        try (Connection connection = DBConnection.getConnection();
             PreparedStatement statement =
                     connection.prepareStatement(sql.toString())) {

            setParameters(statement, parameters);

            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    return resultSet.getInt(1);
                }
            }
        }

        return 0;
    }

    // Retrieve featured products
    @Override
    public List<Product> getFeaturedProducts(int limit)
            throws SQLException {

        validateLimit(limit);

        String sql = "SELECT * FROM products "
                   + "WHERE is_active = TRUE "
                   + "ORDER BY created_at DESC LIMIT ?";

        List<Object> parameters = new ArrayList<>();
        parameters.add(limit);

        return executeProductQuery(sql, parameters);
    }

    // Retrieve recently added products
    @Override
    public List<Product> getLatestProducts(int limit)
            throws SQLException {

        validateLimit(limit);

        String sql = "SELECT * FROM products "
                   + "WHERE is_active = TRUE "
                   + "ORDER BY created_at DESC LIMIT ?";

        List<Object> parameters = new ArrayList<>();
        parameters.add(limit);

        return executeProductQuery(sql, parameters);
    }

    // Add a new product
    @Override
    public boolean addProduct(Product product) throws SQLException {

        String sql = "INSERT INTO products "
                   + "(category_id, product_name, description, "
                   + "price, image_url, is_active) "
                   + "VALUES (?, ?, ?, ?, ?, ?)";

        try (Connection connection = DBConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setInt(1, product.getCategoryId());
            statement.setString(2, product.getProductName());
            statement.setString(3, product.getDescription());
            statement.setBigDecimal(4, product.getPrice());
            statement.setString(5, product.getImageUrl());
            statement.setBoolean(6, product.isActive());

            return statement.executeUpdate() > 0;
        }
    }

    // Update existing product details
    @Override
    public boolean updateProduct(Product product) throws SQLException {

        String sql = "UPDATE products SET category_id = ?, "
                   + "product_name = ?, description = ?, price = ?, "
                   + "image_url = ?, is_active = ? "
                   + "WHERE product_id = ?";

        try (Connection connection = DBConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setInt(1, product.getCategoryId());
            statement.setString(2, product.getProductName());
            statement.setString(3, product.getDescription());
            statement.setBigDecimal(4, product.getPrice());
            statement.setString(5, product.getImageUrl());
            statement.setBoolean(6, product.isActive());
            statement.setInt(7, product.getProductId());

            return statement.executeUpdate() > 0;
        }
    }

    // Delete a product
    @Override
    public boolean deleteProduct(int productId) throws SQLException {

        String sql = "DELETE FROM products WHERE product_id = ?";

        try (Connection connection = DBConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setInt(1, productId);

            return statement.executeUpdate() > 0;
        }
    }

    // Activate or deactivate a product
    @Override
    public boolean updateProductStatus(
            int productId, boolean isActive) throws SQLException {

        String sql = "UPDATE products SET is_active = ? "
                   + "WHERE product_id = ?";

        try (Connection connection = DBConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setBoolean(1, isActive);
            statement.setInt(2, productId);

            return statement.executeUpdate() > 0;
        }
    }

    // Check whether a product exists
    @Override
    public boolean productExists(int productId) throws SQLException {

        String sql = "SELECT 1 FROM products WHERE product_id = ?";

        try (Connection connection = DBConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setInt(1, productId);

            try (ResultSet resultSet = statement.executeQuery()) {
                return resultSet.next();
            }
        }
    }

    // Helper method: Execute product SELECT queries
    private List<Product> executeProductQuery(
            String sql, List<Object> parameters) throws SQLException {

        List<Product> products = new ArrayList<>();

        try (Connection connection = DBConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            setParameters(statement, parameters);

            try (ResultSet resultSet = statement.executeQuery()) {

                while (resultSet.next()) {
                    products.add(mapProduct(resultSet));
                }
            }
        }

        return products;
    }

    // Helper method: Set PreparedStatement parameters
    private void setParameters(
            PreparedStatement statement, List<Object> parameters)
            throws SQLException {

        for (int i = 0; i < parameters.size(); i++) {

            Object parameter = parameters.get(i);

            if (parameter instanceof Integer) {
                statement.setInt(i + 1, (Integer) parameter);

            } else if (parameter instanceof BigDecimal) {
                statement.setBigDecimal(i + 1, (BigDecimal) parameter);

            } else if (parameter instanceof String) {
                statement.setString(i + 1, (String) parameter);
            }
        }
    }

    // Helper method: Convert database record into Product object
    private Product mapProduct(ResultSet resultSet) throws SQLException {

        Product product = new Product();

        product.setProductId(resultSet.getInt("product_id"));
        product.setCategoryId(resultSet.getInt("category_id"));
        product.setProductName(resultSet.getString("product_name"));
        product.setDescription(resultSet.getString("description"));
        product.setPrice(resultSet.getBigDecimal("price"));
        product.setImageUrl(resultSet.getString("image_url"));
        product.setActive(resultSet.getBoolean("is_active"));

        Timestamp timestamp = resultSet.getTimestamp("created_at");

        if (timestamp != null) {
            product.setCreatedAt(timestamp.toLocalDateTime());
        }

        return product;
    }

    // Validate the requested result limit
    private void validateLimit(int limit) {

        if (limit < 1 || limit > 100) {
            throw new IllegalArgumentException(
                    "Limit must be between 1 and 100");
        }
    }
}
