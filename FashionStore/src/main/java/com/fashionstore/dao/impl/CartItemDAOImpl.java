
package com.fashionstore.dao.impl;

import com.fashionstore.dao.CartItemDAO;
import com.fashionstore.model.CartItem;
import com.fashionstore.util.DBConnection;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

public class CartItemDAOImpl implements CartItemDAO {

    // Add a product variant to the cart.
    // If it already exists, increase its quantity.
    @Override
    public boolean addItemToCart(int cartId, int variantId, int quantity)
            throws SQLException {

        if (quantity <= 0) {
            throw new IllegalArgumentException(
                    "Quantity must be greater than zero");
        }

        String stockSql =
                "SELECT pv.stock_quantity "
              + "FROM product_variants pv "
              + "JOIN products p ON pv.product_id = p.product_id "
              + "WHERE pv.variant_id = ? AND p.is_active = TRUE "
              + "FOR UPDATE";

        String existingSql =
                "SELECT cart_item_id, quantity FROM cart_items "
              + "WHERE cart_id = ? AND variant_id = ? FOR UPDATE";

        String insertSql =
                "INSERT INTO cart_items (cart_id, variant_id, quantity) "
              + "VALUES (?, ?, ?)";

        String updateSql =
                "UPDATE cart_items SET quantity = ? "
              + "WHERE cart_item_id = ?";

        String timestampSql =
                "UPDATE carts SET updated_at = CURRENT_TIMESTAMP "
              + "WHERE cart_id = ?";

        try (Connection connection = DBConnection.getConnection()) {

            connection.setAutoCommit(false);

            try {
                // Verify cart exists
                if (!cartExists(connection, cartId)) {
                    throw new SQLException("Shopping cart not found.");
                }

                // Lock and retrieve variant stock
                int availableStock;

                try (PreparedStatement statement =
                             connection.prepareStatement(stockSql)) {

                    statement.setInt(1, variantId);

                    try (ResultSet resultSet = statement.executeQuery()) {

                        if (!resultSet.next()) {
                            throw new SQLException(
                                    "Product variant not found or inactive.");
                        }

                        availableStock =
                                resultSet.getInt("stock_quantity");
                    }
                }

                // Check whether this variant already exists in the cart
                int existingCartItemId = -1;
                int existingQuantity = 0;

                try (PreparedStatement statement =
                             connection.prepareStatement(existingSql)) {

                    statement.setInt(1, cartId);
                    statement.setInt(2, variantId);

                    try (ResultSet resultSet = statement.executeQuery()) {

                        if (resultSet.next()) {
                            existingCartItemId =
                                    resultSet.getInt("cart_item_id");

                            existingQuantity =
                                    resultSet.getInt("quantity");
                        }
                    }
                }

                int newQuantity = existingQuantity + quantity;

                if (newQuantity > availableStock) {
                    throw new SQLException(
                            "Requested quantity exceeds available stock.");
                }

                // Insert new item or update existing item
                if (existingCartItemId == -1) {

                    try (PreparedStatement statement =
                                 connection.prepareStatement(insertSql)) {

                        statement.setInt(1, cartId);
                        statement.setInt(2, variantId);
                        statement.setInt(3, quantity);

                        statement.executeUpdate();
                    }

                } else {

                    try (PreparedStatement statement =
                                 connection.prepareStatement(updateSql)) {

                        statement.setInt(1, newQuantity);
                        statement.setInt(2, existingCartItemId);

                        statement.executeUpdate();
                    }
                }

                // Update cart timestamp
                try (PreparedStatement statement =
                             connection.prepareStatement(timestampSql)) {

                    statement.setInt(1, cartId);
                    statement.executeUpdate();
                }

                connection.commit();
                return true;

            } catch (SQLException | RuntimeException e) {

                connection.rollback();
                throw e;

            } finally {
                connection.setAutoCommit(true);
            }
        }
    }

    // Retrieve all items belonging to a cart

@Override
public List<CartItem> getCartItems(int cartId) throws SQLException {

    List<CartItem> items = new ArrayList<>();

    String sql =
            "SELECT ci.cart_item_id, ci.cart_id, ci.variant_id, "
          + "ci.quantity, ci.added_at, "
          + "pv.product_id, pv.size, pv.color, pv.stock_quantity, "
          + "p.product_name, p.image_url, p.price "
          + "FROM cart_items ci "
          + "JOIN product_variants pv "
          + "ON ci.variant_id = pv.variant_id "
          + "JOIN products p "
          + "ON pv.product_id = p.product_id "
          + "WHERE ci.cart_id = ? "
          + "ORDER BY ci.added_at DESC";

    try (Connection connection = DBConnection.getConnection();
         PreparedStatement statement =
                 connection.prepareStatement(sql)) {

        statement.setInt(1, cartId);

        try (ResultSet resultSet = statement.executeQuery()) {

            while (resultSet.next()) {
                items.add(mapCartItemWithProduct(resultSet));
            }
        }
    }

    return items;
}


    // Retrieve a specific cart item
    @Override
    public CartItem getCartItemById(int cartItemId) throws SQLException {

        String sql = "SELECT * FROM cart_items WHERE cart_item_id = ?";

        try (Connection connection = DBConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setInt(1, cartItemId);

            try (ResultSet resultSet = statement.executeQuery()) {

                if (resultSet.next()) {
                    return mapCartItem(resultSet);
                }
            }
        }

        return null;
    }

    // Retrieve a cart item using cart ID and variant ID
    @Override
    public CartItem getCartItemByVariant(int cartId, int variantId)
            throws SQLException {

        String sql = "SELECT * FROM cart_items "
                   + "WHERE cart_id = ? AND variant_id = ?";

        try (Connection connection = DBConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setInt(1, cartId);
            statement.setInt(2, variantId);

            try (ResultSet resultSet = statement.executeQuery()) {

                if (resultSet.next()) {
                    return mapCartItem(resultSet);
                }
            }
        }

        return null;
    }

    // Update the quantity of an existing cart item
    @Override
    public boolean updateItemQuantity(int cartItemId, int quantity)
            throws SQLException {

        if (quantity <= 0) {
            throw new IllegalArgumentException(
                    "Quantity must be greater than zero");
        }

        String sql =
                "UPDATE cart_items ci "
              + "JOIN product_variants pv "
              + "ON ci.variant_id = pv.variant_id "
              + "JOIN products p ON pv.product_id = p.product_id "
              + "SET ci.quantity = ? "
              + "WHERE ci.cart_item_id = ? "
              + "AND pv.stock_quantity >= ? "
              + "AND p.is_active = TRUE";

        try (Connection connection = DBConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setInt(1, quantity);
            statement.setInt(2, cartItemId);
            statement.setInt(3, quantity);

            boolean updated = statement.executeUpdate() > 0;

            if (updated) {
                updateCartTimestamp(connection, cartItemId);
            }

            return updated;
        }
    }

    // Remove an individual item from the cart
    @Override
    public boolean removeItem(int cartItemId) throws SQLException {

        String sql = "DELETE FROM cart_items WHERE cart_item_id = ?";

        try (Connection connection = DBConnection.getConnection()) {

            int cartId = getCartId(connection, cartItemId);

            try (PreparedStatement statement =
                         connection.prepareStatement(sql)) {

                statement.setInt(1, cartItemId);

                boolean removed = statement.executeUpdate() > 0;

                if (removed && cartId != -1) {
                    updateCartTimestampById(connection, cartId);
                }

                return removed;
            }
        }
    }

    // Remove an item using cart ID and variant ID
    @Override
    public boolean removeItemByVariant(int cartId, int variantId)
            throws SQLException {

        String sql = "DELETE FROM cart_items "
                   + "WHERE cart_id = ? AND variant_id = ?";

        try (Connection connection = DBConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setInt(1, cartId);
            statement.setInt(2, variantId);

            boolean removed = statement.executeUpdate() > 0;

            if (removed) {
                updateCartTimestampById(connection, cartId);
            }

            return removed;
        }
    }

    // Remove all items belonging to a cart
    @Override
    public boolean clearCartItems(int cartId) throws SQLException {

        String sql = "DELETE FROM cart_items WHERE cart_id = ?";

        try (Connection connection = DBConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setInt(1, cartId);

            statement.executeUpdate();

            updateCartTimestampById(connection, cartId);

            return true;
        }
    }

    // Check whether a product variant exists in the cart
    @Override
    public boolean itemExists(int cartId, int variantId)
            throws SQLException {

        String sql = "SELECT 1 FROM cart_items "
                   + "WHERE cart_id = ? AND variant_id = ?";

        try (Connection connection = DBConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setInt(1, cartId);
            statement.setInt(2, variantId);

            try (ResultSet resultSet = statement.executeQuery()) {
                return resultSet.next();
            }
        }
    }

    // Count the total number of units in the cart
    @Override
    public int getTotalItemCount(int cartId) throws SQLException {

        String sql = "SELECT COALESCE(SUM(quantity), 0) "
                   + "FROM cart_items WHERE cart_id = ?";

        try (Connection connection = DBConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setInt(1, cartId);

            try (ResultSet resultSet = statement.executeQuery()) {

                if (resultSet.next()) {
                    return resultSet.getInt(1);
                }
            }
        }

        return 0;
    }

    // Calculate the total price of all items in the cart
    @Override
    public BigDecimal getCartTotal(int cartId) throws SQLException {

        String sql =
                "SELECT COALESCE(SUM(p.price * ci.quantity), 0.00) "
              + "FROM cart_items ci "
              + "JOIN product_variants pv "
              + "ON ci.variant_id = pv.variant_id "
              + "JOIN products p ON pv.product_id = p.product_id "
              + "WHERE ci.cart_id = ? AND p.is_active = TRUE";

        try (Connection connection = DBConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setInt(1, cartId);

            try (ResultSet resultSet = statement.executeQuery()) {

                if (resultSet.next()) {
                    return resultSet.getBigDecimal(1);
                }
            }
        }

        return BigDecimal.ZERO;
    }

    // Check whether all requested quantities are available
    @Override
    public boolean validateCartStock(int cartId) throws SQLException {

        String sql =
                "SELECT COUNT(*) "
              + "FROM cart_items ci "
              + "JOIN product_variants pv "
              + "ON ci.variant_id = pv.variant_id "
              + "JOIN products p ON pv.product_id = p.product_id "
              + "WHERE ci.cart_id = ? "
              + "AND (ci.quantity > pv.stock_quantity "
              + "OR p.is_active = FALSE)";

        try (Connection connection = DBConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setInt(1, cartId);

            try (ResultSet resultSet = statement.executeQuery()) {

                if (resultSet.next()) {
                    return resultSet.getInt(1) == 0;
                }
            }
        }

        return false;
    }

    // Helper: Check whether a cart exists
    private boolean cartExists(Connection connection, int cartId)
            throws SQLException {

        String sql = "SELECT 1 FROM carts WHERE cart_id = ?";

        try (PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setInt(1, cartId);

            try (ResultSet resultSet = statement.executeQuery()) {
                return resultSet.next();
            }
        }
    }

    // Helper: Map database record into CartItem object
    private CartItem mapCartItem(ResultSet resultSet) throws SQLException {

        CartItem item = new CartItem();

        item.setCartItemId(resultSet.getInt("cart_item_id"));
        item.setCartId(resultSet.getInt("cart_id"));
        item.setVariantId(resultSet.getInt("variant_id"));
        item.setQuantity(resultSet.getInt("quantity"));

        Timestamp timestamp = resultSet.getTimestamp("added_at");

        if (timestamp != null) {
            item.setAddedAt(timestamp.toLocalDateTime());
        }

        return item;
    }
    

private CartItem mapCartItemWithProduct(ResultSet resultSet)
        throws SQLException {

    CartItem item = new CartItem();

    // Cart information
    item.setCartItemId(resultSet.getInt("cart_item_id"));
    item.setCartId(resultSet.getInt("cart_id"));
    item.setVariantId(resultSet.getInt("variant_id"));
    item.setQuantity(resultSet.getInt("quantity"));

    Timestamp timestamp = resultSet.getTimestamp("added_at");

    if (timestamp != null) {
        item.setAddedAt(timestamp.toLocalDateTime());
    }

    // Product information
    item.setProductId(resultSet.getInt("product_id"));
    item.setProductName(resultSet.getString("product_name"));
    item.setImageUrl(resultSet.getString("image_url"));
    item.setPrice(resultSet.getBigDecimal("price"));

    // Variant information
    item.setSize(resultSet.getString("size"));
    item.setColor(resultSet.getString("color"));
    item.setStockQuantity(resultSet.getInt("stock_quantity"));

    return item;
}

    

    // Helper: Retrieve cart ID using cart item ID
    private int getCartId(Connection connection, int cartItemId)
            throws SQLException {

        String sql = "SELECT cart_id FROM cart_items WHERE cart_item_id = ?";

        try (PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setInt(1, cartItemId);

            try (ResultSet resultSet = statement.executeQuery()) {

                if (resultSet.next()) {
                    return resultSet.getInt("cart_id");
                }
            }
        }

        return -1;
    }

    // Helper: Update cart timestamp using cart item ID
    private void updateCartTimestamp(Connection connection, int cartItemId)
            throws SQLException {

        int cartId = getCartId(connection, cartItemId);

        if (cartId != -1) {
            updateCartTimestampById(connection, cartId);
        }
    }

    // Helper: Update cart timestamp using cart ID
    private void updateCartTimestampById(Connection connection, int cartId)
            throws SQLException {

        String sql = "UPDATE carts SET updated_at = CURRENT_TIMESTAMP "
                   + "WHERE cart_id = ?";

        try (PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setInt(1, cartId);
            statement.executeUpdate();
        }
    }
}
