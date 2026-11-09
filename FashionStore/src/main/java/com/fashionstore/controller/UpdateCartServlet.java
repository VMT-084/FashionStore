
package com.fashionstore.controller;

import java.io.IOException;
import java.sql.SQLException;

import com.fashionstore.dao.CartDAO;
import com.fashionstore.dao.CartItemDAO;

import com.fashionstore.dao.impl.CartDAOImpl;
import com.fashionstore.dao.impl.CartItemDAOImpl;

import com.fashionstore.model.Cart;
import com.fashionstore.model.CartItem;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

public class UpdateCartServlet extends HttpServlet {

    private static final long serialVersionUID = 1L;

    private CartDAO cartDAO;
    private CartItemDAO cartItemDAO;

    @Override
    public void init() throws ServletException {
        cartDAO = new CartDAOImpl();
        cartItemDAO = new CartItemDAOImpl();
    }

    @Override
    protected void doPost(HttpServletRequest request,
                          HttpServletResponse response)
            throws ServletException, IOException {

        HttpSession session = request.getSession(false);

        if (session == null || session.getAttribute("userId") == null) {
            response.sendRedirect(request.getContextPath() + "/login");
            return;
        }

        try {
            int userId = Integer.parseInt(
                session.getAttribute("userId").toString()
            );

            int cartItemId = Integer.parseInt(
                request.getParameter("cartItemId")
            );

            int quantity = Integer.parseInt(
                request.getParameter("quantity")
            );

            if (userId <= 0 || cartItemId <= 0
                    || quantity < 1 || quantity > 10) {

                session.setAttribute(
                    "cartMessage",
                    "Please enter a valid quantity between 1 and 10."
                );

                response.sendRedirect(request.getContextPath() + "/cart");
                return;
            }

            // Retrieve the customer's cart
            Cart cart = cartDAO.getCartByUserId(userId);

            if (cart == null) {
                response.sendError(
                    HttpServletResponse.SC_NOT_FOUND,
                    "Shopping cart not found."
                );
                return;
            }

            // Verify that this cart item belongs to the customer
            CartItem cartItem =
                cartItemDAO.getCartItemById(cartItemId);

            if (cartItem == null
                    || cartItem.getCartId() != cart.getCartId()) {

                response.sendError(
                    HttpServletResponse.SC_NOT_FOUND,
                    "Cart item not found."
                );
                return;
            }

            // Update quantity with stock validation
            boolean updated = cartItemDAO.updateItemQuantity(
                cartItemId, quantity
            );

            if (updated) {
                session.setAttribute(
                    "cartMessage",
                    "Cart quantity updated successfully."
                );
            } else {
                session.setAttribute(
                    "cartMessage",
                    "Unable to update quantity. Please check available stock."
                );
            }

            response.sendRedirect(request.getContextPath() + "/cart");

        } catch (NumberFormatException e) {

            response.sendError(
                HttpServletResponse.SC_BAD_REQUEST,
                "Invalid cart details."
            );

        } catch (SQLException e) {

            throw new ServletException(
                "Unable to update cart quantity.", e
            );
        }
    }
}
