
package com.fashionstore.dao.impl;

import com.fashionstore.dao.OrderItemDAO;
import com.fashionstore.model.OrderItem;
import com.fashionstore.util.DBConnection;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class OrderItemDAOImpl implements OrderItemDAO {

    // Retrieve all items belonging to an order
    @Override
    public List<OrderItem> getOrderItems(int orderId)
            throws SQLException {

        List<OrderItem> items = new ArrayList<>();

        String sql = "SELECT * FROM order_items "
                   + "WHERE order_id = ? ORDER BY order_item_id";

        try (Connection connection = DBConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setInt(1, orderId);

            try (ResultSet resultSet = statement.executeQuery()) {

                while (resultSet.next()) {
                    items.add(mapOrderItem(resultSet));
                }
            }
        }

        return items;
    }

    // Retrieve a particular order item
    @Override
    public OrderItem getOrderItemById(int orderItemId)
            throws SQLException {

        String sql = "SELECT * FROM order_items "
                   + "WHERE order_item_id = ?";

        try (Connection connection = DBConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setInt(1, orderItemId);

            try (ResultSet resultSet = statement.executeQuery()) {

                if (resultSet.next()) {
                    return mapOrderItem(resultSet);
                }
            }
        }

        return null;
    }

    // Retrieve an order item using order ID and variant ID
    @Override
    public OrderItem getOrderItemByVariant(int orderId, int variantId)
            throws SQLException {

        String sql = "SELECT * FROM order_items "
                   + "WHERE order_id = ? AND variant_id = ?";

        try (Connection connection = DBConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setInt(1, orderId);
            statement.setInt(2, variantId);

            try (ResultSet resultSet = statement.executeQuery()) {

                if (resultSet.next()) {
                    return mapOrderItem(resultSet);
                }
            }
        }

        return null;
    }

    // Add an individual item to an order
    @Override
    public boolean addOrderItem(OrderItem orderItem)
            throws SQLException {

        String sql = "INSERT INTO order_items "
                   + "(order_id, product_id, variant_id, product_name, "
                   + "size, color, quantity, unit_price, subtotal) "
                   + "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";

        try (Connection connection = DBConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            setOrderItemParameters(statement, orderItem);

            return statement.executeUpdate() > 0;
        }
    }

    // Add multiple items to an order using batch insertion
    @Override
    public boolean addOrderItems(List<OrderItem> orderItems)
            throws SQLException {

        if (orderItems == null || orderItems.isEmpty()) {
            return false;
        }

        String sql = "INSERT INTO order_items "
                   + "(order_id, product_id, variant_id, product_name, "
                   + "size, color, quantity, unit_price, subtotal) "
                   + "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";

        try (Connection connection = DBConnection.getConnection()) {

            connection.setAutoCommit(false);

            try (PreparedStatement statement =
                         connection.prepareStatement(sql)) {

                for (OrderItem item : orderItems) {

                    setOrderItemParameters(statement, item);

                    statement.addBatch();
                }

                int[] results = statement.executeBatch();

                for (int result : results) {

                    if (result == PreparedStatement.EXECUTE_FAILED) {
                        throw new SQLException(
                                "Failed to insert one or more order items.");
                    }
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

    // Delete an individual order item
    @Override
    public boolean deleteOrderItem(int orderItemId)
            throws SQLException {

        String sql = "DELETE FROM order_items WHERE order_item_id = ?";

        try (Connection connection = DBConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setInt(1, orderItemId);

            return statement.executeUpdate() > 0;
        }
    }

    // Delete all items belonging to an order
    @Override
    public boolean deleteOrderItems(int orderId)
            throws SQLException {

        String sql = "DELETE FROM order_items WHERE order_id = ?";

        try (Connection connection = DBConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setInt(1, orderId);

            statement.executeUpdate();

            return true;
        }
    }

    // Count the number of distinct items in an order
    @Override
    public int getOrderItemCount(int orderId)
            throws SQLException {

        String sql = "SELECT COUNT(*) FROM order_items WHERE order_id = ?";

        try (Connection connection = DBConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setInt(1, orderId);

            try (ResultSet resultSet = statement.executeQuery()) {

                if (resultSet.next()) {
                    return resultSet.getInt(1);
                }
            }
        }

        return 0;
    }

    // Calculate the total quantity of products in an order
    @Override
    public int getTotalQuantity(int orderId)
            throws SQLException {

        String sql = "SELECT COALESCE(SUM(quantity), 0) "
                   + "FROM order_items WHERE order_id = ?";

        try (Connection connection = DBConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setInt(1, orderId);

            try (ResultSet resultSet = statement.executeQuery()) {

                if (resultSet.next()) {
                    return resultSet.getInt(1);
                }
            }
        }

        return 0;
    }

    // Calculate the total amount of an order's items
    @Override
    public BigDecimal getOrderItemsTotal(int orderId)
            throws SQLException {

        String sql = "SELECT COALESCE(SUM(subtotal), 0.00) "
                   + "FROM order_items WHERE order_id = ?";

        try (Connection connection = DBConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setInt(1, orderId);

            try (ResultSet resultSet = statement.executeQuery()) {

                if (resultSet.next()) {
                    return resultSet.getBigDecimal(1);
                }
            }
        }

        return BigDecimal.ZERO;
    }

    // Helper: Set PreparedStatement parameters
    private void setOrderItemParameters(
            PreparedStatement statement, OrderItem item)
            throws SQLException {

        statement.setInt(1, item.getOrderId());
        statement.setInt(2, item.getProductId());
        statement.setInt(3, item.getVariantId());
        statement.setString(4, item.getProductName());
        statement.setString(5, item.getSize());
        statement.setString(6, item.getColor());
        statement.setInt(7, item.getQuantity());
        statement.setBigDecimal(8, item.getUnitPrice());
        statement.setBigDecimal(9, item.getSubtotal());
    }

    // Helper: Convert database record into OrderItem object
    private OrderItem mapOrderItem(ResultSet resultSet)
            throws SQLException {

        OrderItem item = new OrderItem();

        item.setOrderItemId(resultSet.getInt("order_item_id"));
        item.setOrderId(resultSet.getInt("order_id"));
        item.setProductId(resultSet.getInt("product_id"));
        item.setVariantId(resultSet.getInt("variant_id"));

        item.setProductName(resultSet.getString("product_name"));
        item.setSize(resultSet.getString("size"));
        item.setColor(resultSet.getString("color"));

        item.setQuantity(resultSet.getInt("quantity"));

        item.setUnitPrice(resultSet.getBigDecimal("unit_price"));
        item.setSubtotal(resultSet.getBigDecimal("subtotal"));

        return item;
    }
}
