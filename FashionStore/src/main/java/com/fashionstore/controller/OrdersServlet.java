
package com.fashionstore.controller;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

import com.fashionstore.dao.OrderDAO;
import com.fashionstore.dao.impl.OrderDAOImpl;
import com.fashionstore.model.Order;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

public class OrdersServlet extends HttpServlet {

    private static final long serialVersionUID = 1L;

    private static final int PAGE_SIZE = 5;

    private OrderDAO orderDAO;

    @Override
    public void init() throws ServletException {
        orderDAO = new OrderDAOImpl();
    }

    // Display customer's order history
    @Override
    protected void doGet(HttpServletRequest request,
                         HttpServletResponse response)
            throws ServletException, IOException {

        HttpSession session = request.getSession(false);

        // Check customer login
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

            // Read requested page number
            int currentPage = 1;

            String pageParameter = request.getParameter("page");

            if (pageParameter != null
                    && pageParameter.matches("[0-9]+")) {

                currentPage = Integer.parseInt(pageParameter);
            }

            if (currentPage < 1) {
                currentPage = 1;
            }

            // Count customer's total orders
            int totalOrders =
                    orderDAO.countOrdersByUserId(userId);

            int totalPages =
                    (int) Math.ceil((double) totalOrders / PAGE_SIZE);

            if (totalPages > 0 && currentPage > totalPages) {
                currentPage = totalPages;
            }

            // Retrieve orders for the requested page
            List<Order> orders =
                    orderDAO.getOrdersByUserId(
                        userId,
                        currentPage,
                        PAGE_SIZE
                    );

            request.setAttribute("orders", orders);
            request.setAttribute("currentPage", currentPage);
            request.setAttribute("totalPages", totalPages);
            request.setAttribute("totalOrders", totalOrders);

            request.getRequestDispatcher(
                "/WEB-INF/views/orders.jsp"
            ).forward(request, response);

        } catch (NumberFormatException e) {

            response.sendRedirect(
                request.getContextPath() + "/login"
            );

        } catch (SQLException e) {

            throw new ServletException(
                "Unable to retrieve customer orders.", e
            );
        }
    }
}
