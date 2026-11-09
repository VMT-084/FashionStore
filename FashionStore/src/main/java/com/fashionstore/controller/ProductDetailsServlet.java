
package com.fashionstore.controller;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

import com.fashionstore.dao.ProductDAO;
import com.fashionstore.dao.ProductVariantDAO;
import com.fashionstore.dao.impl.ProductDAOImpl;
import com.fashionstore.dao.impl.ProductVariantDAOImpl;
import com.fashionstore.model.Product;
import com.fashionstore.model.ProductVariant;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

public class ProductDetailsServlet extends HttpServlet {

    private static final long serialVersionUID = 1L;

    private ProductDAO productDAO;
    private ProductVariantDAO productVariantDAO;

    @Override
    public void init() throws ServletException {

        productDAO = new ProductDAOImpl();
        productVariantDAO = new ProductVariantDAOImpl();
    }

    @Override
    protected void doGet(HttpServletRequest request,
                         HttpServletResponse response)
            throws ServletException, IOException {

        try {

            // Get product ID from URL
            String idParameter = request.getParameter("id");

            if (idParameter == null || idParameter.trim().isEmpty()) {
                response.sendError(
                    HttpServletResponse.SC_BAD_REQUEST,
                    "Product ID is required."
                );
                return;
            }

            int productId;

            try {
                productId = Integer.parseInt(idParameter);
            } catch (NumberFormatException e) {
                response.sendError(
                    HttpServletResponse.SC_BAD_REQUEST,
                    "Invalid product ID."
                );
                return;
            }

            if (productId <= 0) {
                response.sendError(
                    HttpServletResponse.SC_BAD_REQUEST,
                    "Invalid product ID."
                );
                return;
            }

            // Retrieve product details
            Product product = productDAO.getProductById(productId);

            if (product == null) {
                response.sendError(
                    HttpServletResponse.SC_NOT_FOUND,
                    "Product not found."
                );
                return;
            }

            // Retrieve available product variants
            List<ProductVariant> variants =
                    productVariantDAO.getVariantsByProductId(productId);

            // Pass data to JSP
            request.setAttribute("product", product);
            request.setAttribute("variants", variants);

            // Forward to product details page
            request.getRequestDispatcher(
                "/WEB-INF/views/product-details.jsp"
            ).forward(request, response);

        } catch (SQLException e) {

            throw new ServletException(
                "Unable to load product details.", e
            );
        }
    }
}
