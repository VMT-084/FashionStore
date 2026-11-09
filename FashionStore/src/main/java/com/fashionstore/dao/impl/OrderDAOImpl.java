
package com.fashionstore.dao.impl;

import com.fashionstore.dao.OrderDAO;
import com.fashionstore.model.Order;
import com.fashionstore.model.OrderItem;
import com.fashionstore.util.DBConnection;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class OrderDAOImpl implements OrderDAO {

    // Place an order using the customer's cart.
    // Order creation, item insertion, stock deduction and cart clearing
    // happen inside one transaction.
    @Override
    public int placeOrder(int userId, Order order) throws SQLException {

        String cartSql =
                "SELECT cart_id FROM carts WHERE user_id = ? FOR UPDATE";

        String cartItemsSql =
                "SELECT ci.variant_id, ci.quantity, "
              + "pv.product_id, pv.size, pv.color, pv.stock_quantity, "
              + "p.product_name, p.price, p.is_active "
              + "FROM cart_items ci "
              + "JOIN product_variants pv "
              + "ON ci.variant_id = pv.variant_id "
              + "JOIN products p ON pv.product_id = p.product_id "
              + "WHERE ci.cart_id = ? "
              + "ORDER BY ci.variant_id FOR UPDATE";

        String insertOrderSql =
                "INSERT INTO orders "
              + "(user_id, total_amount, payment_method, payment_status, "
              + "order_status, delivery_name, delivery_phone, "
              + "delivery_address, delivery_city, delivery_state, "
              + "delivery_pincode) "
              + "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

        String insertOrderItemSql =
                "INSERT INTO order_items "
              + "(order_id, product_id, variant_id, product_name, "
              + "size, color, quantity, unit_price, subtotal) "
              + "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";

        String decreaseStockSql =
                "UPDATE product_variants "
              + "SET stock_quantity = stock_quantity - ? "
              + "WHERE variant_id = ? AND stock_quantity >= ?";

        String clearCartSql =
                "DELETE FROM cart_items WHERE cart_id = ?";

        String updateCartSql =
                "UPDATE carts SET updated_at = CURRENT_TIMESTAMP "
              + "WHERE cart_id = ?";

        try (Connection connection = DBConnection.getConnection()) {

            connection.setAutoCommit(false);

            try {
                // 1. Lock and retrieve the customer's cart
                int cartId;

                try (PreparedStatement statement =
                             connection.prepareStatement(cartSql)) {

                    statement.setInt(1, userId);

                    try (ResultSet resultSet = statement.executeQuery()) {

                        if (!resultSet.next()) {
                            throw new SQLException(
                                    "Shopping cart not found.");
                        }

                        cartId = resultSet.getInt("cart_id");
                    }
                }

                // 2. Retrieve and lock cart items and product variants
                List<OrderItem> orderItems = new ArrayList<>();
                BigDecimal totalAmount = BigDecimal.ZERO;

                try (PreparedStatement statement =
                             connection.prepareStatement(cartItemsSql)) {

                    statement.setInt(1, cartId);

                    try (ResultSet resultSet = statement.executeQuery()) {

                        while (resultSet.next()) {

                            int quantity = resultSet.getInt("quantity");
                            int stock = resultSet.getInt("stock_quantity");

                            boolean active =
                                    resultSet.getBoolean("is_active");

                            if (!active) {
                                throw new SQLException(
                                        "A product in your cart is no longer available.");
                            }

                            if (quantity <= 0 || quantity > stock) {
                                throw new SQLException(
                                        "Insufficient stock for product: "
                                        + resultSet.getString("product_name"));
                            }

                            BigDecimal unitPrice =
                                    resultSet.getBigDecimal("price");

                            BigDecimal subtotal =
                                    unitPrice.multiply(
                                            BigDecimal.valueOf(quantity));

                            OrderItem item = new OrderItem();

                            item.setProductId(
                                    resultSet.getInt("product_id"));

                            item.setVariantId(
                                    resultSet.getInt("variant_id"));

                            item.setProductName(
                                    resultSet.getString("product_name"));

                            item.setSize(
                                    resultSet.getString("size"));

                            item.setColor(
                                    resultSet.getString("color"));

                            item.setQuantity(quantity);
                            item.setUnitPrice(unitPrice);
                            item.setSubtotal(subtotal);

                            orderItems.add(item);

                            totalAmount = totalAmount.add(subtotal);
                        }
                    }
                }

                if (orderItems.isEmpty()) {
                    throw new SQLException(
                            "Cannot place an order with an empty cart.");
                }

                // 3. Insert the order and retrieve generated order ID
                int orderId;

                try (PreparedStatement statement =
                             connection.prepareStatement(
                                     insertOrderSql,
                                     Statement.RETURN_GENERATED_KEYS)) {

                    statement.setInt(1, userId);
                    statement.setBigDecimal(2, totalAmount);
                    statement.setString(3, "COD");
                    statement.setString(4, "Pending");
                    statement.setString(5, "Placed");

                    statement.setString(6, order.getDeliveryName());
                    statement.setString(7, order.getDeliveryPhone());
                    statement.setString(8, order.getDeliveryAddress());
                    statement.setString(9, order.getDeliveryCity());
                    statement.setString(10, order.getDeliveryState());
                    statement.setString(11, order.getDeliveryPincode());

                    statement.executeUpdate();

                    try (ResultSet keys = statement.getGeneratedKeys()) {

                        if (!keys.next()) {
                            throw new SQLException(
                                    "Failed to generate order ID.");
                        }

                        orderId = keys.getInt(1);
                    }
                }

                // 4. Insert all order items
                try (PreparedStatement statement =
                             connection.prepareStatement(insertOrderItemSql)) {

                    for (OrderItem item : orderItems) {

                        statement.setInt(1, orderId);
                        statement.setInt(2, item.getProductId());
                        statement.setInt(3, item.getVariantId());
                        statement.setString(4, item.getProductName());
                        statement.setString(5, item.getSize());
                        statement.setString(6, item.getColor());
                        statement.setInt(7, item.getQuantity());
                        statement.setBigDecimal(8, item.getUnitPrice());
                        statement.setBigDecimal(9, item.getSubtotal());

                        statement.addBatch();
                    }

                    statement.executeBatch();
                }

                // 5. Reduce stock for each purchased variant
                try (PreparedStatement statement =
                             connection.prepareStatement(decreaseStockSql)) {

                    for (OrderItem item : orderItems) {

                        statement.setInt(1, item.getQuantity());
                        statement.setInt(2, item.getVariantId());
                        statement.setInt(3, item.getQuantity());

                        int rowsAffected = statement.executeUpdate();

                        if (rowsAffected != 1) {
                            throw new SQLException(
                                    "Stock update failed for variant ID: "
                                    + item.getVariantId());
                        }
                    }
                }

                // 6. Clear the customer's cart
                try (PreparedStatement statement =
                             connection.prepareStatement(clearCartSql)) {

                    statement.setInt(1, cartId);
                    statement.executeUpdate();
                }

                // 7. Update cart timestamp
                try (PreparedStatement statement =
                             connection.prepareStatement(updateCartSql)) {

                    statement.setInt(1, cartId);
                    statement.executeUpdate();
                }

                // 8. Commit all operations
                connection.commit();

                return orderId;

            } catch (SQLException | RuntimeException e) {

                connection.rollback();
                throw e;

            } finally {
                connection.setAutoCommit(true);
            }
        }
    }

    // Retrieve an order using its ID
    @Override
    public Order getOrderById(int orderId) throws SQLException {

        String sql = "SELECT * FROM orders WHERE order_id = ?";

        try (Connection connection = DBConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setInt(1, orderId);

            try (ResultSet resultSet = statement.executeQuery()) {

                if (resultSet.next()) {
                    return mapOrder(resultSet);
                }
            }
        }

        return null;
    }

    // Retrieve an order belonging to a particular customer
    @Override
    public Order getOrderByIdAndUserId(int orderId, int userId)
            throws SQLException {

        String sql = "SELECT * FROM orders "
                   + "WHERE order_id = ? AND user_id = ?";

        try (Connection connection = DBConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setInt(1, orderId);
            statement.setInt(2, userId);

            try (ResultSet resultSet = statement.executeQuery()) {

                if (resultSet.next()) {
                    return mapOrder(resultSet);
                }
            }
        }

        return null;
    }

    // Retrieve all orders placed by a customer
    @Override
    public List<Order> getOrdersByUserId(int userId)
            throws SQLException {

        List<Order> orders = new ArrayList<>();

        String sql = "SELECT * FROM orders "
                   + "WHERE user_id = ? ORDER BY order_date DESC";

        try (Connection connection = DBConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setInt(1, userId);

            try (ResultSet resultSet = statement.executeQuery()) {

                while (resultSet.next()) {
                    orders.add(mapOrder(resultSet));
                }
            }
        }

        return orders;
    }

    // Retrieve customer orders with pagination
    @Override
    public List<Order> getOrdersByUserId(
            int userId, int page, int pageSize) throws SQLException {

        if (page < 1 || pageSize < 1 || pageSize > 100) {
            throw new IllegalArgumentException(
                    "Invalid pagination values.");
        }

        List<Order> orders = new ArrayList<>();

        int offset = (page - 1) * pageSize;

        String sql = "SELECT * FROM orders "
                   + "WHERE user_id = ? "
                   + "ORDER BY order_date DESC LIMIT ? OFFSET ?";

        try (Connection connection = DBConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setInt(1, userId);
            statement.setInt(2, pageSize);
            statement.setInt(3, offset);

            try (ResultSet resultSet = statement.executeQuery()) {

                while (resultSet.next()) {
                    orders.add(mapOrder(resultSet));
                }
            }
        }

        return orders;
    }

    // Count the total orders placed by a customer
    @Override
    public int countOrdersByUserId(int userId) throws SQLException {

        String sql = "SELECT COUNT(*) FROM orders WHERE user_id = ?";

        try (Connection connection = DBConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setInt(1, userId);

            try (ResultSet resultSet = statement.executeQuery()) {

                if (resultSet.next()) {
                    return resultSet.getInt(1);
                }
            }
        }

        return 0;
    }

    // Retrieve orders by their status
    @Override
    public List<Order> getOrdersByStatus(String orderStatus)
            throws SQLException {

        List<Order> orders = new ArrayList<>();

        String sql = "SELECT * FROM orders "
                   + "WHERE order_status = ? ORDER BY order_date DESC";

        try (Connection connection = DBConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setString(1, orderStatus);

            try (ResultSet resultSet = statement.executeQuery()) {

                while (resultSet.next()) {
                    orders.add(mapOrder(resultSet));
                }
            }
        }

        return orders;
    }

    // Retrieve orders placed within a date range
    @Override
    public List<Order> getOrdersByDateRange(
            LocalDateTime startDate, LocalDateTime endDate)
            throws SQLException {

        if (startDate == null || endDate == null
                || startDate.isAfter(endDate)) {

            throw new IllegalArgumentException(
                    "Invalid order date range.");
        }

        List<Order> orders = new ArrayList<>();

        String sql = "SELECT * FROM orders "
                   + "WHERE order_date >= ? AND order_date <= ? "
                   + "ORDER BY order_date DESC";

        try (Connection connection = DBConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setTimestamp(1, Timestamp.valueOf(startDate));
            statement.setTimestamp(2, Timestamp.valueOf(endDate));

            try (ResultSet resultSet = statement.executeQuery()) {

                while (resultSet.next()) {
                    orders.add(mapOrder(resultSet));
                }
            }
        }

        return orders;
    }

    // Update an order's status using valid transitions
    @Override
    public boolean updateOrderStatus(int orderId, String orderStatus)
            throws SQLException {

        if (orderStatus == null) {
            throw new IllegalArgumentException(
                    "Order status cannot be null.");
        }


        String sql =
        		"UPDATE orders SET order_status = ? "
        				+ "WHERE order_id = ? "
        				+ "AND ("
        				+ "(order_status = 'Placed' AND ? = 'Shipped') "
        				+ "OR (order_status = 'Shipped' AND ? = 'Delivered')"
        				+ ")";


        try (Connection connection = DBConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setString(1, orderStatus);
            statement.setInt(2, orderId);
            statement.setString(3, orderStatus);
            statement.setString(4, orderStatus);

            return statement.executeUpdate() > 0;
        }
    }

    // Cancel an eligible order and restore stock transactionally
    @Override
    public boolean cancelOrder(int orderId, int userId)
            throws SQLException {

        String orderSql =
                "SELECT order_status FROM orders "
              + "WHERE order_id = ? AND user_id = ? FOR UPDATE";

        String itemsSql =
                "SELECT variant_id, quantity FROM order_items "
              + "WHERE order_id = ? ORDER BY variant_id";

        String restoreStockSql =
                "UPDATE product_variants "
              + "SET stock_quantity = stock_quantity + ? "
              + "WHERE variant_id = ?";

        String cancelSql =
                "UPDATE orders SET order_status = 'Cancelled' "
              + "WHERE order_id = ? AND user_id = ? "
              + "AND order_status = 'Placed'";

        try (Connection connection = DBConnection.getConnection()) {

            connection.setAutoCommit(false);

            try {
                // Lock and verify order ownership and status
                try (PreparedStatement statement =
                             connection.prepareStatement(orderSql)) {

                    statement.setInt(1, orderId);
                    statement.setInt(2, userId);

                    try (ResultSet resultSet = statement.executeQuery()) {

                        if (!resultSet.next()) {
                            connection.rollback();
                            return false;
                        }

                        String status =
                                resultSet.getString("order_status");

                        if (!"Placed".equals(status)) {
                            connection.rollback();
                            return false;
                        }
                    }
                }

                // Retrieve all purchased variants
                List<OrderItem> items = new ArrayList<>();

                try (PreparedStatement statement =
                             connection.prepareStatement(itemsSql)) {

                    statement.setInt(1, orderId);

                    try (ResultSet resultSet = statement.executeQuery()) {

                        while (resultSet.next()) {

                            OrderItem item = new OrderItem();

                            item.setVariantId(
                                    resultSet.getInt("variant_id"));

                            item.setQuantity(
                                    resultSet.getInt("quantity"));

                            items.add(item);
                        }
                    }
                }

                // Restore inventory
                try (PreparedStatement statement =
                             connection.prepareStatement(restoreStockSql)) {

                    for (OrderItem item : items) {

                        statement.setInt(1, item.getQuantity());
                        statement.setInt(2, item.getVariantId());

                        if (statement.executeUpdate() != 1) {
                            throw new SQLException(
                                    "Unable to restore stock for variant: "
                                    + item.getVariantId());
                        }
                    }
                }

                // Mark order as cancelled
                try (PreparedStatement statement =
                             connection.prepareStatement(cancelSql)) {

                    statement.setInt(1, orderId);
                    statement.setInt(2, userId);

                    if (statement.executeUpdate() != 1) {
                        throw new SQLException(
                                "Order cancellation failed.");
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

    // Check whether an order belongs to a customer
    @Override
    public boolean orderBelongsToUser(int orderId, int userId)
            throws SQLException {

        String sql = "SELECT 1 FROM orders "
                   + "WHERE order_id = ? AND user_id = ?";

        try (Connection connection = DBConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setInt(1, orderId);
            statement.setInt(2, userId);

            try (ResultSet resultSet = statement.executeQuery()) {
                return resultSet.next();
            }
        }
    }

    // Calculate the total amount of a customer's orders
    @Override
    public BigDecimal getTotalOrderAmount(int userId)
            throws SQLException {

        String sql =
                "SELECT COALESCE(SUM(total_amount), 0.00) "
              + "FROM orders WHERE user_id = ? "
              + "AND order_status != 'Cancelled'";

        try (Connection connection = DBConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setInt(1, userId);

            try (ResultSet resultSet = statement.executeQuery()) {

                if (resultSet.next()) {
                    return resultSet.getBigDecimal(1);
                }
            }
        }

        return BigDecimal.ZERO;
    }

    // Convert a database record into an Order object
    private Order mapOrder(ResultSet resultSet) throws SQLException {

        Order order = new Order();

        order.setOrderId(resultSet.getInt("order_id"));
        order.setUserId(resultSet.getInt("user_id"));

        Timestamp orderDate = resultSet.getTimestamp("order_date");

        if (orderDate != null) {
            order.setOrderDate(orderDate.toLocalDateTime());
        }

        order.setTotalAmount(resultSet.getBigDecimal("total_amount"));
        order.setPaymentMethod(resultSet.getString("payment_method"));
        order.setPaymentStatus(resultSet.getString("payment_status"));
        order.setOrderStatus(resultSet.getString("order_status"));

        order.setDeliveryName(resultSet.getString("delivery_name"));
        order.setDeliveryPhone(resultSet.getString("delivery_phone"));
        order.setDeliveryAddress(resultSet.getString("delivery_address"));
        order.setDeliveryCity(resultSet.getString("delivery_city"));
        order.setDeliveryState(resultSet.getString("delivery_state"));
        order.setDeliveryPincode(resultSet.getString("delivery_pincode"));

        return order;
    }
}
