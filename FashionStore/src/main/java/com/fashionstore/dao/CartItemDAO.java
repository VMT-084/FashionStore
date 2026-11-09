
package com.fashionstore.dao;

import com.fashionstore.model.CartItem;
import java.math.BigDecimal;
import java.sql.SQLException;
import java.util.List;

public interface CartItemDAO {

    // Add a product variant to the cart
    // If it already exists, increase its quantity
    boolean addItemToCart(int cartId, int variantId, int quantity)
            throws SQLException;

    // Retrieve all items belonging to a cart
    List<CartItem> getCartItems(int cartId) throws SQLException;

    // Retrieve a specific cart item
    CartItem getCartItemById(int cartItemId) throws SQLException;

    // Retrieve a cart item using cart ID and variant ID
    CartItem getCartItemByVariant(int cartId, int variantId)
            throws SQLException;

    // Update the quantity of an existing cart item
    boolean updateItemQuantity(int cartItemId, int quantity)
            throws SQLException;

    // Remove an individual item from the cart
    boolean removeItem(int cartItemId) throws SQLException;

    // Remove an item using cart ID and variant ID
    boolean removeItemByVariant(int cartId, int variantId)
            throws SQLException;

    // Remove all items belonging to a cart
    boolean clearCartItems(int cartId) throws SQLException;

    // Check whether a product variant exists in the cart
    boolean itemExists(int cartId, int variantId) throws SQLException;

    // Count the total number of units in the cart
    int getTotalItemCount(int cartId) throws SQLException;

    // Calculate the total price of all items in the cart
    BigDecimal getCartTotal(int cartId) throws SQLException;

    // Check whether all requested quantities are available
    boolean validateCartStock(int cartId) throws SQLException;
}
