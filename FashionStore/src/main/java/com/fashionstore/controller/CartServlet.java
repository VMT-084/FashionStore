
package com.fashionstore.controller;

import java.io.IOException;
import java.math.BigDecimal;
import java.sql.SQLException;
import java.util.List;

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

public class CartServlet extends HttpServlet {

    private static final long serialVersionUID = 1L;

    private CartDAO cartDAO;
    private CartItemDAO cartItemDAO;

    @Override
    public void init() throws ServletException {
        cartDAO = new CartDAOImpl();
        cartItemDAO = new CartItemDAOImpl();
    }

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

            // Retrieve customer's cart
            Cart cart = cartDAO.getCartByUserId(userId);

            if (cart == null) {
                request.setAttribute("cartItems",
                        java.util.Collections.emptyList());
                request.setAttribute("cartTotal", BigDecimal.ZERO);
                request.setAttribute("totalItemCount", 0);
                session.setAttribute("cartItemCount", 0);

            } else {
                // Retrieve cart items
                List<CartItem> cartItems =
                        cartItemDAO.getCartItems(cart.getCartId());
                

                BigDecimal cartTotal =
                        cartItemDAO.getCartTotal(cart.getCartId());

                int totalItemCount =
                        cartItemDAO.getTotalItemCount(cart.getCartId());
                
                session.setAttribute("cartItemCount", totalItemCount);

                request.setAttribute("cartItems", cartItems);
                request.setAttribute("cartTotal", cartTotal);
                request.setAttribute("totalItemCount", totalItemCount);
            }

            request.getRequestDispatcher(
                "/WEB-INF/views/cart.jsp"
            ).forward(request, response);

        } catch (NumberFormatException e) {
            response.sendRedirect(
                request.getContextPath() + "/login"
            );

        } catch (SQLException e) {
            throw new ServletException(
                "Unable to retrieve shopping cart.", e
            );
        }
    }
}
