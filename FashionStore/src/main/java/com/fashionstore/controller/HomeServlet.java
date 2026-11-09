
package com.fashionstore.controller;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

import com.fashionstore.dao.CategoryDAO;
import com.fashionstore.dao.ProductDAO;

import com.fashionstore.dao.impl.CategoryDAOImpl;
import com.fashionstore.dao.impl.ProductDAOImpl;

import com.fashionstore.model.Category;
import com.fashionstore.model.Product;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;


public class HomeServlet extends HttpServlet {

    private static final long serialVersionUID = 1L;

    private CategoryDAO categoryDAO;
    private ProductDAO productDAO;

    @Override
    public void init() throws ServletException {

        categoryDAO = new CategoryDAOImpl();
        productDAO = new ProductDAOImpl();

    }

    @Override
    protected void doGet(
            HttpServletRequest request,
            HttpServletResponse response)
            throws ServletException, IOException {

        try {

            // 1. Retrieve all categories
            List<Category> categories =
                    categoryDAO.getAllCategories();


            // 2. Retrieve featured products
            List<Product> featuredProducts =
                    productDAO.getFeaturedProducts(4);


            // 3. Retrieve latest products
            List<Product> latestProducts =
                    productDAO.getLatestProducts(4);


            // 4. Store data in request attributes
            request.setAttribute("categories", categories);

            request.setAttribute(
                    "featuredProducts", featuredProducts);

            request.setAttribute(
                    "latestProducts", latestProducts);


            // 5. Forward request to homepage JSP
            request.getRequestDispatcher(
                    "/WEB-INF/views/home.jsp")
                    .forward(request, response);

        } catch (SQLException e) {

            throw new ServletException(
                    "Unable to load FashionStore homepage data.",
                    e
            );

        }

    }

}
