
package com.fashionstore.controller;

import java.io.IOException;

import com.fashionstore.dao.CategoryDAO;
import com.fashionstore.dao.impl.CategoryDAOImpl;
import com.fashionstore.model.Category;
import java.math.BigDecimal;
import java.sql.SQLException;
import java.util.List;

import com.fashionstore.dao.ProductDAO;
import com.fashionstore.dao.impl.ProductDAOImpl;
import com.fashionstore.model.Product;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

public class ProductsServlet extends HttpServlet {

    private static final long serialVersionUID = 1L;

    private static final int PAGE_SIZE = 12;
    
    private CategoryDAO categoryDAO;

    private ProductDAO productDAO;

    @Override
    public void init() throws ServletException {
        productDAO = new ProductDAOImpl();
        categoryDAO = new CategoryDAOImpl();
    }

    @Override
    protected void doGet(HttpServletRequest request,
                         HttpServletResponse response)
            throws ServletException, IOException {

        try {
            // 1. Read search keyword
            String keyword = request.getParameter("keyword");
            List<Category> categories = categoryDAO.getAllCategories();

            request.setAttribute("categories", categories);

            if (keyword != null) {
                keyword = keyword.trim();

                if (keyword.isEmpty()) {
                    keyword = null;
                }
            }

            // 2. Read category filter
            Integer categoryId = parseInteger(
                    request.getParameter("category")
            );

            // 3. Read minimum and maximum price
            BigDecimal minPrice = parsePrice(
                    request.getParameter("minPrice")
            );

            BigDecimal maxPrice = parsePrice(
                    request.getParameter("maxPrice")
            );

            // Validate price range
            if (minPrice != null && maxPrice != null
                    && minPrice.compareTo(maxPrice) > 0) {

                BigDecimal temp = minPrice;
                minPrice = maxPrice;
                maxPrice = temp;
            }

            // 4. Read sorting option
            String sort = request.getParameter("sort");

            String sortBy = "created_at";
            String sortOrder = "DESC";

            if ("price_low".equals(sort)) {
                sortBy = "price";
                sortOrder = "ASC";

            } else if ("price_high".equals(sort)) {
                sortBy = "price";
                sortOrder = "DESC";

            } else if ("name_asc".equals(sort)) {
                sortBy = "name";
                sortOrder = "ASC";

            } else if ("newest".equals(sort)) {
                sortBy = "created_at";
                sortOrder = "DESC";
            }

            // 5. Read current page
            int page = 1;

            try {
                page = Integer.parseInt(
                        request.getParameter("page")
                );

                if (page < 1) {
                    page = 1;
                }

            } catch (NumberFormatException e) {
                page = 1;
            }

            // 6. Fetch one extra product to detect next page
            List<Product> products = productDAO.getProducts(
                    keyword,
                    categoryId,
                    minPrice,
                    maxPrice,
                    sortBy,
                    sortOrder,
                    page,
                    PAGE_SIZE + 1
            );

            boolean hasNextPage = products.size() > PAGE_SIZE;

            if (hasNextPage) {
                products.remove(products.size() - 1);
            }

            // 7. Send data to JSP
            request.setAttribute("products", products);
            request.setAttribute("keyword", keyword);
            request.setAttribute("categoryId", categoryId);
            request.setAttribute("minPrice", minPrice);
            request.setAttribute("maxPrice", maxPrice);
            request.setAttribute("sort", sort);
            request.setAttribute("currentPage", page);
            request.setAttribute("hasNextPage", hasNextPage);

            // 8. Forward to product listing page
            request.getRequestDispatcher(
                    "/WEB-INF/views/products.jsp"
            ).forward(request, response);

        } catch (SQLException e) {

            throw new ServletException(
                    "Unable to load product listing.",
                    e
            );
        }
    }

    private Integer parseInteger(String value) {

        if (value == null || value.trim().isEmpty()) {
            return null;
        }

        try {
            int number = Integer.parseInt(value.trim());
            return number > 0 ? number : null;

        } catch (NumberFormatException e) {
            return null;
        }
    }

    private BigDecimal parsePrice(String value) {

        if (value == null || value.trim().isEmpty()) {
            return null;
        }

        try {
            BigDecimal price = new BigDecimal(value.trim());

            if (price.compareTo(BigDecimal.ZERO) < 0) {
                return null;
            }

            return price;

        } catch (NumberFormatException e) {
            return null;
        }
    }
}
