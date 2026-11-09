
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

public class RemoveCartItemServlet extends HttpServlet {

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

            int cartItemId = Integer.parseInt(
                request.getParameter("cartItemId")
            );

            if (userId <= 0 || cartItemId <= 0) {
                response.sendError(
                    HttpServletResponse.SC_BAD_REQUEST,
                    "Invalid cart item details."
                );
                return;
            }

            // Retrieve customer's cart
            Cart cart = cartDAO.getCartByUserId(userId);

            if (cart == null) {
                response.sendError(
                    HttpServletResponse.SC_NOT_FOUND,
                    "Shopping cart not found."
                );
                return;
            }

            // Verify that the item belongs to this customer's cart
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

            // Remove cart item
            boolean removed = cartItemDAO.removeItem(cartItemId);

            if (removed) {
                session.setAttribute(
                    "cartMessage",
                    "Product removed from your cart."
                );
            } else {
                session.setAttribute(
                    "cartMessage",
                    "Unable to remove the product."
                );
            }

            response.sendRedirect(
                request.getContextPath() + "/cart"
            );

        } catch (NumberFormatException e) {

            response.sendError(
                HttpServletResponse.SC_BAD_REQUEST,
                "Invalid cart item ID."
            );

        } catch (SQLException e) {

            throw new ServletException(
                "Unable to remove cart item.", e
            );
        }
    }
}
