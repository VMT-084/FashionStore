
package com.fashionstore.dao;

import com.fashionstore.model.Cart;
import java.sql.SQLException;

public interface CartDAO {

    // Create a cart for a customer
    int createCart(int userId) throws SQLException;

    // Retrieve a cart using its ID
    Cart getCartById(int cartId) throws SQLException;

    // Retrieve a customer's cart using their user ID
    Cart getCartByUserId(int userId) throws SQLException;

    // Check whether a customer already has a cart
    boolean cartExists(int userId) throws SQLException;

    // Update the cart's last modified timestamp
    boolean updateCartTimestamp(int cartId) throws SQLException;

    // Clear all items from a customer's cart
    boolean clearCart(int cartId) throws SQLException;

    // Delete a customer's cart
    boolean deleteCart(int cartId) throws SQLException;

    // Delete a customer's cart using their user ID
    boolean deleteCartByUserId(int userId) throws SQLException;
}

