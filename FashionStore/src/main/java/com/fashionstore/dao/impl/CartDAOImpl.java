
package com.fashionstore.dao.impl;

import com.fashionstore.dao.CartDAO;
import com.fashionstore.model.Cart;
import com.fashionstore.util.DBConnection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;

public class CartDAOImpl implements CartDAO {

    // Create a cart for a customer
    @Override
    public int createCart(int userId) throws SQLException {

        String sql = "INSERT INTO carts (user_id) VALUES (?)";

        try (Connection connection = DBConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(
                     sql, Statement.RETURN_GENERATED_KEYS)) {

            statement.setInt(1, userId);

            int rowsAffected = statement.executeUpdate();

            if (rowsAffected > 0) {
                try (ResultSet resultSet = statement.getGeneratedKeys()) {
                    if (resultSet.next()) {
                        return resultSet.getInt(1);
                    }
                }
            }
        }

        return -1;
    }

    // Retrieve a cart using its ID
    @Override
    public Cart getCartById(int cartId) throws SQLException {

        String sql = "SELECT * FROM carts WHERE cart_id = ?";

        try (Connection connection = DBConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setInt(1, cartId);

            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    return mapCart(resultSet);
                }
            }
        }

        return null;
    }

    // Retrieve a customer's cart using their user ID
    @Override
    public Cart getCartByUserId(int userId) throws SQLException {

        String sql = "SELECT * FROM carts WHERE user_id = ?";

        try (Connection connection = DBConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setInt(1, userId);

            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    return mapCart(resultSet);
                }
            }
        }

        return null;
    }

    // Check whether a customer already has a cart
    @Override
    public boolean cartExists(int userId) throws SQLException {

        String sql = "SELECT 1 FROM carts WHERE user_id = ?";

        try (Connection connection = DBConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setInt(1, userId);

            try (ResultSet resultSet = statement.executeQuery()) {
                return resultSet.next();
            }
        }
    }

    // Update the cart's last modified timestamp
    @Override
    public boolean updateCartTimestamp(int cartId) throws SQLException {

        String sql = "UPDATE carts SET updated_at = CURRENT_TIMESTAMP "
                   + "WHERE cart_id = ?";

        try (Connection connection = DBConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setInt(1, cartId);

            return statement.executeUpdate() > 0;
        }
    }

    // Clear all items from a customer's cart
    @Override
    public boolean clearCart(int cartId) throws SQLException {

        String sql = "DELETE FROM cart_items WHERE cart_id = ?";

        try (Connection connection = DBConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setInt(1, cartId);

            statement.executeUpdate();

            // Update the cart timestamp even if it was already empty
            updateCartTimestamp(cartId);

            return true;
        }
    }

    // Delete a customer's cart using its ID
    @Override
    public boolean deleteCart(int cartId) throws SQLException {

        String sql = "DELETE FROM carts WHERE cart_id = ?";

        try (Connection connection = DBConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setInt(1, cartId);

            return statement.executeUpdate() > 0;
        }
    }

    // Delete a customer's cart using their user ID
    @Override
    public boolean deleteCartByUserId(int userId) throws SQLException {

        String sql = "DELETE FROM carts WHERE user_id = ?";

        try (Connection connection = DBConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setInt(1, userId);

            return statement.executeUpdate() > 0;
        }
    }

    // Convert a database record into a Cart object
    private Cart mapCart(ResultSet resultSet) throws SQLException {

        Cart cart = new Cart();

        cart.setCartId(resultSet.getInt("cart_id"));
        cart.setUserId(resultSet.getInt("user_id"));

        Timestamp createdAt = resultSet.getTimestamp("created_at");
        Timestamp updatedAt = resultSet.getTimestamp("updated_at");

        if (createdAt != null) {
            cart.setCreatedAt(createdAt.toLocalDateTime());
        }

        if (updatedAt != null) {
            cart.setUpdatedAt(updatedAt.toLocalDateTime());
        }

        return cart;
    }
}
