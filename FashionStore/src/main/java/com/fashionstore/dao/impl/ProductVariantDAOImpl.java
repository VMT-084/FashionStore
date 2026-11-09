
package com.fashionstore.dao.impl;

import com.fashionstore.dao.ProductVariantDAO;
import com.fashionstore.model.ProductVariant;
import com.fashionstore.util.DBConnection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class ProductVariantDAOImpl implements ProductVariantDAO {

    // Retrieve all variants belonging to a product
    @Override
    public List<ProductVariant> getVariantsByProductId(int productId)
            throws SQLException {

        List<ProductVariant> variants = new ArrayList<>();

        String sql = "SELECT * FROM product_variants "
                   + "WHERE product_id = ? ORDER BY size, color";

        try (Connection connection = DBConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setInt(1, productId);

            try (ResultSet resultSet = statement.executeQuery()) {
                while (resultSet.next()) {
                    variants.add(mapVariant(resultSet));
                }
            }
        }

        return variants;
    }

    // Retrieve a variant using its ID
    @Override
    public ProductVariant getVariantById(int variantId)
            throws SQLException {

        String sql = "SELECT * FROM product_variants WHERE variant_id = ?";

        try (Connection connection = DBConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setInt(1, variantId);

            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    return mapVariant(resultSet);
                }
            }
        }

        return null;
    }

    // Retrieve a variant using product, size and colour
    @Override
    public ProductVariant getVariantByAttributes(
            int productId, String size, String color) throws SQLException {

        String sql = "SELECT * FROM product_variants "
                   + "WHERE product_id = ? AND size = ? AND color = ?";

        try (Connection connection = DBConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setInt(1, productId);
            statement.setString(2, size);
            statement.setString(3, color);

            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    return mapVariant(resultSet);
                }
            }
        }

        return null;
    }

    // Retrieve a variant using its SKU
    @Override
    public ProductVariant getVariantBySKU(String sku) throws SQLException {

        String sql = "SELECT * FROM product_variants WHERE sku = ?";

        try (Connection connection = DBConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setString(1, sku);

            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    return mapVariant(resultSet);
                }
            }
        }

        return null;
    }

    // Retrieve available sizes for a product
    @Override
    public List<String> getAvailableSizes(int productId)
            throws SQLException {

        List<String> sizes = new ArrayList<>();

        String sql = "SELECT DISTINCT size FROM product_variants "
                   + "WHERE product_id = ? AND stock_quantity > 0 "
                   + "ORDER BY size";

        try (Connection connection = DBConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setInt(1, productId);

            try (ResultSet resultSet = statement.executeQuery()) {
                while (resultSet.next()) {
                    sizes.add(resultSet.getString("size"));
                }
            }
        }

        return sizes;
    }

    // Retrieve available colours for a product
    @Override
    public List<String> getAvailableColors(int productId)
            throws SQLException {

        List<String> colors = new ArrayList<>();

        String sql = "SELECT DISTINCT color FROM product_variants "
                   + "WHERE product_id = ? AND stock_quantity > 0 "
                   + "ORDER BY color";

        try (Connection connection = DBConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setInt(1, productId);

            try (ResultSet resultSet = statement.executeQuery()) {
                while (resultSet.next()) {
                    colors.add(resultSet.getString("color"));
                }
            }
        }

        return colors;
    }

    // Check whether a variant has sufficient stock
    @Override
    public boolean isStockAvailable(int variantId, int quantity)
            throws SQLException {

        if (quantity <= 0) {
            return false;
        }

        String sql = "SELECT 1 FROM product_variants "
                   + "WHERE variant_id = ? AND stock_quantity >= ?";

        try (Connection connection = DBConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setInt(1, variantId);
            statement.setInt(2, quantity);

            try (ResultSet resultSet = statement.executeQuery()) {
                return resultSet.next();
            }
        }
    }

    // Retrieve the available stock quantity
    @Override
    public int getStockQuantity(int variantId) throws SQLException {

        String sql = "SELECT stock_quantity FROM product_variants "
                   + "WHERE variant_id = ?";

        try (Connection connection = DBConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setInt(1, variantId);

            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    return resultSet.getInt("stock_quantity");
                }
            }
        }

        return -1;
    }

    // Add a new product variant
    @Override
    public boolean addVariant(ProductVariant variant) throws SQLException {

        String sql = "INSERT INTO product_variants "
                   + "(product_id, size, color, stock_quantity, sku) "
                   + "VALUES (?, ?, ?, ?, ?)";

        try (Connection connection = DBConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setInt(1, variant.getProductId());
            statement.setString(2, variant.getSize());
            statement.setString(3, variant.getColor());
            statement.setInt(4, variant.getStockQuantity());
            statement.setString(5, variant.getSku());

            return statement.executeUpdate() > 0;
        }
    }

    // Update variant details
    @Override
    public boolean updateVariant(ProductVariant variant)
            throws SQLException {

        String sql = "UPDATE product_variants "
                   + "SET size = ?, color = ?, sku = ? "
                   + "WHERE variant_id = ?";

        try (Connection connection = DBConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setString(1, variant.getSize());
            statement.setString(2, variant.getColor());
            statement.setString(3, variant.getSku());
            statement.setInt(4, variant.getVariantId());

            return statement.executeUpdate() > 0;
        }
    }

    // Set stock to a specified quantity
    @Override
    public boolean updateStock(int variantId, int quantity)
            throws SQLException {

        if (quantity < 0) {
            throw new IllegalArgumentException(
                    "Stock quantity cannot be negative");
        }

        String sql = "UPDATE product_variants "
                   + "SET stock_quantity = ? WHERE variant_id = ?";

        try (Connection connection = DBConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setInt(1, quantity);
            statement.setInt(2, variantId);

            return statement.executeUpdate() > 0;
        }
    }

    // Increase stock quantity
    @Override
    public boolean increaseStock(int variantId, int quantity)
            throws SQLException {

        if (quantity <= 0) {
            throw new IllegalArgumentException(
                    "Quantity must be greater than zero");
        }

        String sql = "UPDATE product_variants "
                   + "SET stock_quantity = stock_quantity + ? "
                   + "WHERE variant_id = ?";

        try (Connection connection = DBConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setInt(1, quantity);
            statement.setInt(2, variantId);

            return statement.executeUpdate() > 0;
        }
    }

    // Decrease stock quantity without allowing negative stock
    @Override
    public boolean decreaseStock(int variantId, int quantity)
            throws SQLException {

        if (quantity <= 0) {
            throw new IllegalArgumentException(
                    "Quantity must be greater than zero");
        }

        String sql = "UPDATE product_variants "
                   + "SET stock_quantity = stock_quantity - ? "
                   + "WHERE variant_id = ? AND stock_quantity >= ?";

        try (Connection connection = DBConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setInt(1, quantity);
            statement.setInt(2, variantId);
            statement.setInt(3, quantity);

            return statement.executeUpdate() > 0;
        }
    }

    // Check whether an SKU already exists
    @Override
    public boolean skuExists(String sku) throws SQLException {

        String sql = "SELECT 1 FROM product_variants WHERE sku = ?";

        try (Connection connection = DBConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setString(1, sku);

            try (ResultSet resultSet = statement.executeQuery()) {
                return resultSet.next();
            }
        }
    }

    // Delete a product variant
    @Override
    public boolean deleteVariant(int variantId) throws SQLException {

        String sql = "DELETE FROM product_variants WHERE variant_id = ?";

        try (Connection connection = DBConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setInt(1, variantId);

            return statement.executeUpdate() > 0;
        }
    }

    // Convert a database record into a ProductVariant object
    private ProductVariant mapVariant(ResultSet resultSet)
            throws SQLException {

        ProductVariant variant = new ProductVariant();

        variant.setVariantId(resultSet.getInt("variant_id"));
        variant.setProductId(resultSet.getInt("product_id"));
        variant.setSize(resultSet.getString("size"));
        variant.setColor(resultSet.getString("color"));
        variant.setStockQuantity(resultSet.getInt("stock_quantity"));
        variant.setSku(resultSet.getString("sku"));

        return variant;
    }
}
