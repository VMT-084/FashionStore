
package com.fashionstore.controller;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

import com.fashionstore.dao.OrderDAO;
import com.fashionstore.dao.OrderItemDAO;

import com.fashionstore.dao.impl.OrderDAOImpl;
import com.fashionstore.dao.impl.OrderItemDAOImpl;

import com.fashionstore.model.Order;
import com.fashionstore.model.OrderItem;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

public class OrderDetailsServlet extends HttpServlet {

    private static final long serialVersionUID = 1L;

    private OrderDAO orderDAO;
    private OrderItemDAO orderItemDAO;

    @Override
    public void init() throws ServletException {
        orderDAO = new OrderDAOImpl();
        orderItemDAO = new OrderItemDAOImpl();
    }

    // Display individual order details
    @Override
    protected void doGet(HttpServletRequest request,
                         HttpServletResponse response)
            throws ServletException, IOException {

        HttpSession session = request.getSession(false);

        if (session == null || session.getAttribute("userId") == null) {
            response.sendRedirect(
                request.getContextPath() + "/login"
            );
            return;
        }

        try {

            int userId = Integer.parseInt(
                session.getAttribute("userId").toString()
            );

            String idParameter = request.getParameter("id");

            if (idParameter == null
                    || !idParameter.matches("[0-9]+")) {

                response.sendRedirect(
                    request.getContextPath() + "/orders"
                );
                return;
            }

            int orderId = Integer.parseInt(idParameter);

            if (orderId <= 0) {
                response.sendRedirect(
                    request.getContextPath() + "/orders"
                );
                return;
            }

            // Verify that the order belongs to this customer
            Order order = orderDAO.getOrderByIdAndUserId(
                orderId, userId
            );

            if (order == null) {
                response.sendRedirect(
                    request.getContextPath() + "/orders"
                );
                return;
            }

            // Retrieve products belonging to this order
            List<OrderItem> orderItems =
                    orderItemDAO.getOrderItems(orderId);

            request.setAttribute("order", order);
            request.setAttribute("orderItems", orderItems);

            request.getRequestDispatcher(
                "/WEB-INF/views/order-details.jsp"
            ).forward(request, response);

        } catch (NumberFormatException e) {

            response.sendRedirect(
                request.getContextPath() + "/orders"
            );

        } catch (SQLException e) {

            throw new ServletException(
                "Unable to retrieve order details.", e
            );
        }
    }

    // Cancel an order
    @Override
    protected void doPost(HttpServletRequest request,
                          HttpServletResponse response)
            throws ServletException, IOException {

        HttpSession session = request.getSession(false);

        if (session == null || session.getAttribute("userId") == null) {
            response.sendRedirect(
                request.getContextPath() + "/login"
            );
            return;
        }

        try {

            int userId = Integer.parseInt(
                session.getAttribute("userId").toString()
            );

            String idParameter = request.getParameter("id");

            if (idParameter == null
                    || !idParameter.matches("[0-9]+")) {

                response.sendRedirect(
                    request.getContextPath() + "/orders"
                );
                return;
            }

            int orderId = Integer.parseInt(idParameter);

            if (orderId <= 0) {
                response.sendRedirect(
                    request.getContextPath() + "/orders"
                );
                return;
            }

            // Cancellation verifies ownership and restores stock
            boolean cancelled = orderDAO.cancelOrder(
                orderId, userId
            );

            if (cancelled) {

                session.setAttribute(
                    "orderMessage",
                    "Your order has been cancelled successfully."
                );

            } else {

                session.setAttribute(
                    "orderMessage",
                    "This order cannot be cancelled. It may already be shipped, delivered or cancelled."
                );
            }

            response.sendRedirect(
                request.getContextPath()
                + "/order-details?id=" + orderId
            );

        } catch (NumberFormatException e) {

            response.sendRedirect(
                request.getContextPath() + "/orders"
            );

        } catch (SQLException e) {

            throw new ServletException(
                "Unable to cancel the order.", e
            );
        }
    }
}
