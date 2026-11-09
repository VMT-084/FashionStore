
package com.fashionstore.controller;

import java.io.IOException;
import java.sql.SQLException;
import java.math.BigDecimal;
import java.util.List;

import com.fashionstore.dao.UserDAO;
import com.fashionstore.dao.CartDAO;
import com.fashionstore.dao.CartItemDAO;

import com.fashionstore.dao.OrderDAO;

import com.fashionstore.dao.impl.UserDAOImpl;
import com.fashionstore.dao.impl.CartDAOImpl;
import com.fashionstore.dao.impl.CartItemDAOImpl;
import com.fashionstore.dao.impl.OrderDAOImpl;

import com.fashionstore.model.User;
import com.fashionstore.model.Cart;
import com.fashionstore.model.CartItem;
import com.fashionstore.model.Order;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

public class CheckoutServlet extends HttpServlet {

    private static final long serialVersionUID = 1L;

    private UserDAO userDAO;
    private CartDAO cartDAO;
    private CartItemDAO cartItemDAO;
    private OrderDAO orderDAO;

    @Override
    public void init() throws ServletException {

        userDAO = new UserDAOImpl();
        cartDAO = new CartDAOImpl();
        cartItemDAO = new CartItemDAOImpl();
        orderDAO = new OrderDAOImpl();
    }

    // Display checkout page
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

            User user = userDAO.getUserById(userId);

            if (user == null) {
                response.sendRedirect(
                    request.getContextPath() + "/login"
                );
                return;
            }

            Cart cart = cartDAO.getCartByUserId(userId);

            if (cart == null) {
                response.sendRedirect(
                    request.getContextPath() + "/cart"
                );
                return;
            }

            List<CartItem> cartItems =
                    cartItemDAO.getCartItems(cart.getCartId());

            if (cartItems == null || cartItems.isEmpty()) {
                response.sendRedirect(
                    request.getContextPath() + "/cart"
                );
                return;
            }

            BigDecimal cartTotal =
                    cartItemDAO.getCartTotal(cart.getCartId());

            request.setAttribute("user", user);
            request.setAttribute("cartItems", cartItems);
            request.setAttribute("cartTotal", cartTotal);

            request.getRequestDispatcher(
                "/WEB-INF/views/checkout.jsp"
            ).forward(request, response);

        } catch (NumberFormatException e) {

            response.sendRedirect(
                request.getContextPath() + "/login"
            );

        } catch (SQLException e) {

            throw new ServletException(
                "Unable to load checkout page.", e
            );
        }
    }

    // Place customer order
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
            

User user = userDAO.getUserById(userId);

if (user == null) {
    session.invalidate();

    response.sendRedirect(
        request.getContextPath() + "/login"
    );
    return;
}


            String deliveryName =
                    request.getParameter("deliveryName");

            String deliveryPhone =
                    request.getParameter("deliveryPhone");

            String deliveryAddress =
                    request.getParameter("deliveryAddress");

            String deliveryCity =
                    request.getParameter("deliveryCity");

            String deliveryState =
                    request.getParameter("deliveryState");

            String deliveryPincode =
                    request.getParameter("deliveryPincode");

            // Validate delivery information
            if (isBlank(deliveryName)
                    || isBlank(deliveryPhone)
                    || isBlank(deliveryAddress)
                    || isBlank(deliveryCity)
                    || isBlank(deliveryState)
                    || isBlank(deliveryPincode)) {

                session.setAttribute(
                    "checkoutMessage",
                    "Please complete all delivery details."
                );

                response.sendRedirect(
                    request.getContextPath() + "/checkout"
                );
                return;
            }

            if (!deliveryPhone.trim().matches("[0-9]{10}")
                    || !deliveryPincode.trim().matches("[0-9]{6}")) {

                session.setAttribute(
                    "checkoutMessage",
                    "Please enter a valid 10-digit phone number "
                    + "and 6-digit pincode."
                );

                response.sendRedirect(
                    request.getContextPath() + "/checkout"
                );
                return;
            }


if (deliveryName.trim().length() > 100
        || deliveryPhone.trim().length() > 15
        || deliveryAddress.trim().length() > 500
        || deliveryCity.trim().length() > 100
        || deliveryState.trim().length() > 100) {

    session.setAttribute(
        "checkoutMessage",
        "One or more delivery details exceed the allowed length."
    );

    response.sendRedirect(
        request.getContextPath() + "/checkout"
    );
    return;
}

            
            Cart cart = cartDAO.getCartByUserId(userId);

            if (cart == null) {
                response.sendRedirect(
                    request.getContextPath() + "/cart"
                );
                return;
            }

            // Validate stock before placing order
            if (!cartItemDAO.validateCartStock(cart.getCartId())) {

                session.setAttribute(
                    "checkoutMessage",
                    "Some cart items are unavailable or have insufficient stock."
                );

                response.sendRedirect(
                    request.getContextPath() + "/cart"
                );
                return;
            }

            // Create order object
            Order order = new Order();

            order.setPaymentMethod("COD");
            order.setPaymentStatus("Pending");
            order.setOrderStatus("Placed");

            order.setDeliveryName(deliveryName.trim());
            order.setDeliveryPhone(deliveryPhone.trim());
            order.setDeliveryAddress(deliveryAddress.trim());
            order.setDeliveryCity(deliveryCity.trim());
            order.setDeliveryState(deliveryState.trim());
            order.setDeliveryPincode(deliveryPincode.trim());

            // Place order transactionally
            int orderId = orderDAO.placeOrder(userId, order);

            if (orderId > 0) {

                session.setAttribute("cartItemCount", 0);

                response.sendRedirect(
                    request.getContextPath()
                    + "/order-success?id=" + orderId
                );

            } else {

                session.setAttribute(
                    "checkoutMessage",
                    "Unable to place your order. Please try again."
                );

                response.sendRedirect(
                    request.getContextPath() + "/checkout"
                );
            }

        } catch (NumberFormatException e) {

            response.sendRedirect(
                request.getContextPath() + "/login"
            );

        } catch (SQLException e) {

        	String errorMessage = e.getMessage();

            if (errorMessage != null &&
                    (errorMessage.contains("Insufficient stock")
                    || errorMessage.contains("no longer available"))) {

                session.setAttribute(
                    "checkoutMessage",
                    "Some products are no longer available in the requested quantity. Please review your cart."
                );

                response.sendRedirect(
                    request.getContextPath() + "/cart"
                );

            } else {

                throw new ServletException(
                    "Unable to place your order.", e
                );
            }
        }
    }

    private boolean isBlank(String value) {
        return value == null || value.trim().isEmpty();
    }
}
