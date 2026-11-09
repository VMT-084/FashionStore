
package com.fashionstore.controller;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

import com.fashionstore.dao.ProductDAO;
import com.fashionstore.dao.impl.ProductDAOImpl;
import com.fashionstore.model.Product;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

public class SearchServlet extends HttpServlet {

    private static final long serialVersionUID = 1L;

    private ProductDAO productDAO;

    @Override
    public void init() throws ServletException {
        productDAO = new ProductDAOImpl();
    }

    @Override
    protected void doGet(HttpServletRequest request,
                         HttpServletResponse response)
                         throws ServletException, IOException {

        request.setCharacterEncoding("UTF-8");

        String keyword = request.getParameter("keyword");

        if (keyword == null || keyword.trim().isEmpty()) {
            response.sendRedirect(
                request.getContextPath() + "/products"
            );
            return;
        }

        keyword = keyword.trim();

        try {
            List<Product> products = productDAO.getProducts(
                keyword,
                null,
                null,
                null,
                "newest",
                "DESC",
                1,
                100
            );

            request.setAttribute("products", products);
            request.setAttribute("keyword", keyword);
            request.setAttribute("searchResults", true);

            request.getRequestDispatcher(
                "/WEB-INF/views/search.jsp"
            ).forward(request, response);

        } catch (SQLException e) {
            throw new ServletException(
                "Unable to search products.", e
            );
        }
    }
}
