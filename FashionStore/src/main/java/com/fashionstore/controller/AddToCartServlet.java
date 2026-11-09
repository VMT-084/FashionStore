
package com.fashionstore.controller;

import java.io.IOException;
import java.sql.SQLException;

import com.fashionstore.dao.CartDAO;
import com.fashionstore.dao.CartItemDAO;
import com.fashionstore.dao.ProductVariantDAO;

import com.fashionstore.dao.impl.CartDAOImpl;
import com.fashionstore.dao.impl.CartItemDAOImpl;
import com.fashionstore.dao.impl.ProductVariantDAOImpl;

import com.fashionstore.model.Cart;
import com.fashionstore.model.ProductVariant;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

public class AddToCartServlet extends HttpServlet {

    private static final long serialVersionUID = 1L;

    private CartDAO cartDAO;
    private CartItemDAO cartItemDAO;
    private ProductVariantDAO productVariantDAO;

    @Override
    public void init() throws ServletException {

        cartDAO = new CartDAOImpl();
        cartItemDAO = new CartItemDAOImpl();
        productVariantDAO = new ProductVariantDAOImpl();
    }

    @Override
    protected void doPost(HttpServletRequest request,
                          HttpServletResponse response)
            throws ServletException, IOException {

    	HttpSession session = request.getSession(false);

    	if (session == null ||
    	        session.getAttribute("userId") == null) {

    	    response.sendRedirect(
    	            request.getContextPath() + "/login"
    	    );

    	    return;
    	}

        int userId;
        int variantId;
        int quantity;

        try {

            userId = Integer.parseInt(
                session.getAttribute("userId").toString()
            );

            variantId = Integer.parseInt(
                request.getParameter("variantId")
            );

            quantity = Integer.parseInt(
                request.getParameter("quantity")
            );

        } catch (NumberFormatException | NullPointerException e) {

            response.sendError(
                HttpServletResponse.SC_BAD_REQUEST,
                "Invalid cart details."
            );
            return;
        }

        if (userId <= 0 || variantId <= 0
                || quantity < 1 || quantity > 10) {

            response.sendError(
                HttpServletResponse.SC_BAD_REQUEST,
                "Invalid product variant or quantity."
            );
            return;
        }

        try {

            // Check whether the selected variant exists
            ProductVariant variant =
                productVariantDAO.getVariantById(variantId);

            if (variant == null) {
                response.sendError(
                    HttpServletResponse.SC_NOT_FOUND,
                    "Selected product variant not found."
                );
                return;
            }

            // Check available stock
            if (!productVariantDAO.isStockAvailable(
                    variantId, quantity)) {

                session.setAttribute(
                    "cartMessage",
                    "Requested quantity is not available in stock."
                );

                response.sendRedirect(
                    request.getContextPath()
                    + "/product-details?id="
                    + variant.getProductId()
                );
                return;
            }

            // Retrieve customer's cart
            Cart cart = cartDAO.getCartByUserId(userId);

            // Create a cart if one does not exist
            if (cart == null) {

                int cartId = cartDAO.createCart(userId);

                if (cartId <= 0) {
                    throw new SQLException(
                        "Unable to create customer cart."
                    );
                }

                cart = cartDAO.getCartById(cartId);
            }

            if (cart == null) {
                throw new SQLException(
                    "Unable to retrieve customer cart."
                );
            }

            // Add product variant to cart
            boolean added = cartItemDAO.addItemToCart(
                cart.getCartId(),
                variantId,
                quantity
            );

            if (!added) {
                throw new SQLException(
                    "Unable to add product to cart."
                );
            }

            session.setAttribute(
                "cartMessage",
                "Product added to your cart successfully!"
            );

            // Return to product details
            response.sendRedirect(
                request.getContextPath()
                + "/product-details?id="
                + variant.getProductId()
            );

        } catch (SQLException e) {

            throw new ServletException(
                "Unable to add product to cart.", e
            );
        }
    }
}
