
package com.fashionstore.dao;

import com.fashionstore.model.OrderItem;
import java.math.BigDecimal;
import java.sql.SQLException;
import java.util.List;

public interface OrderItemDAO {

    // Retrieve all items belonging to an order
    List<OrderItem> getOrderItems(int orderId) throws SQLException;

    // Retrieve a particular order item
    OrderItem getOrderItemById(int orderItemId) throws SQLException;

    // Retrieve an order item using order ID and variant ID
    OrderItem getOrderItemByVariant(int orderId, int variantId)
            throws SQLException;

    // Add an individual item to an order
    boolean addOrderItem(OrderItem orderItem) throws SQLException;

    // Add multiple items to an order
    boolean addOrderItems(List<OrderItem> orderItems)
            throws SQLException;

    // Delete an individual order item
    boolean deleteOrderItem(int orderItemId) throws SQLException;

    // Delete all items belonging to an order
    boolean deleteOrderItems(int orderId) throws SQLException;

    // Count the number of distinct items in an order
    int getOrderItemCount(int orderId) throws SQLException;

    // Calculate the total quantity of products in an order
    int getTotalQuantity(int orderId) throws SQLException;

    // Calculate the total amount of an order's items
    BigDecimal getOrderItemsTotal(int orderId) throws SQLException;
}
