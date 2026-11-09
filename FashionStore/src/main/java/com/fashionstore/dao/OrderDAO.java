
package com.fashionstore.dao;

import com.fashionstore.model.Order;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.List;

public interface OrderDAO {

    // Place an order using the customer's cart and delivery details.
    // Creates the order, saves order items, reduces stock,
    // and clears the cart within one database transaction.
    int placeOrder(int userId, Order order) throws SQLException;

    // Retrieve an order using its ID
    Order getOrderById(int orderId) throws SQLException;

    // Retrieve an order belonging to a particular customer
    Order getOrderByIdAndUserId(int orderId, int userId)
            throws SQLException;

    // Retrieve all orders placed by a customer
    List<Order> getOrdersByUserId(int userId) throws SQLException;

    // Retrieve customer orders with pagination
    List<Order> getOrdersByUserId(
            int userId, int page, int pageSize
    ) throws SQLException;

    // Count the total orders placed by a customer
    int countOrdersByUserId(int userId) throws SQLException;

    // Retrieve orders by their status
    List<Order> getOrdersByStatus(String orderStatus)
            throws SQLException;

    // Retrieve orders placed within a date range
    List<Order> getOrdersByDateRange(
            LocalDateTime startDate, LocalDateTime endDate
    ) throws SQLException;

    // Update an order's status
    boolean updateOrderStatus(int orderId, String orderStatus)
            throws SQLException;

    // Cancel an eligible order belonging to a customer
    // Restores stock and updates order status transactionally.
    boolean cancelOrder(int orderId, int userId) throws SQLException;

    // Check whether an order belongs to a customer
    boolean orderBelongsToUser(int orderId, int userId)
            throws SQLException;

    // Calculate the total amount of a customer's orders
    java.math.BigDecimal getTotalOrderAmount(int userId)
            throws SQLException;
}
