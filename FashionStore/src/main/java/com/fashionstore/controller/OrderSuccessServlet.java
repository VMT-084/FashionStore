
package com.fashionstore.controller;

import java.io.IOException;
import java.sql.SQLException;

import com.fashionstore.dao.OrderDAO;
import com.fashionstore.dao.impl.OrderDAOImpl;
import com.fashionstore.model.Order;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

public class OrderSuccessServlet extends HttpServlet {

    private static final long serialVersionUID = 1L;

    private OrderDAO orderDAO;

    @Override
    public void init() throws ServletException {
        orderDAO = new OrderDAOImpl();
    }

    // Display order confirmation
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

            String orderIdParameter = request.getParameter("id");

            if (orderIdParameter == null
                    || !orderIdParameter.matches("[0-9]+")) {

                response.sendRedirect(
                    request.getContextPath() + "/home"
                );
                return;
            }

            int orderId = Integer.parseInt(orderIdParameter);

            if (orderId <= 0) {
                response.sendRedirect(
                    request.getContextPath() + "/home"
                );
                return;
            }

            // Retrieve order only if it belongs to this customer
            Order order = orderDAO.getOrderByIdAndUserId(
                orderId, userId
            );

            if (order == null) {
                response.sendRedirect(
                    request.getContextPath() + "/home"
                );
                return;
            }

            request.setAttribute("order", order);

            request.getRequestDispatcher(
                "/WEB-INF/views/order-success.jsp"
            ).forward(request, response);

        } catch (NumberFormatException e) {

            response.sendRedirect(
                request.getContextPath() + "/home"
            );

        } catch (SQLException e) {

            throw new ServletException(
                "Unable to retrieve order confirmation.", e
            );
        }
    }
}
