
<%@ page contentType="text/html; charset=UTF-8"
         pageEncoding="UTF-8" %>

<%@ page import="java.util.List" %>
<%@ page import="java.util.ArrayList" %>
<%@ page import="java.util.Collections" %>
<%@ page import="java.text.NumberFormat" %>
<%@ page import="java.util.Locale" %>

<%@ page import="com.fashionstore.model.Product" %>
<%@ page import="com.fashionstore.model.Category" %>

<%
    String contextPath = request.getContextPath();

    List<Product> products =
        (List<Product>) request.getAttribute("products");

    List<Category> categories =
        (List<Category>) request.getAttribute("categories");

    if (products == null) {
        products = Collections.emptyList();
    }

    if (categories == null) {
        categories = Collections.emptyList();
    }

    String keyword = (String) request.getAttribute("keyword");

    Integer selectedCategory =
        (Integer) request.getAttribute("categoryId");

    java.math.BigDecimal minPrice =
        (java.math.BigDecimal) request.getAttribute("minPrice");

    java.math.BigDecimal maxPrice =
        (java.math.BigDecimal) request.getAttribute("maxPrice");

    String selectedSort = (String) request.getAttribute("sort");

    Integer currentPage =
        (Integer) request.getAttribute("currentPage");

    Boolean hasNextPage =
        (Boolean) request.getAttribute("hasNextPage");

    if (currentPage == null) {
        currentPage = 1;
    }

    if (hasNextPage == null) {
        hasNextPage = false;
    }

    NumberFormat currencyFormat =
        NumberFormat.getCurrencyInstance(new Locale("en", "IN"));

    StringBuilder queryString = new StringBuilder();

    if (keyword != null && !keyword.isEmpty()) {
        queryString.append("&keyword=")
                   .append(java.net.URLEncoder.encode(keyword, "UTF-8"));
    }

    if (selectedCategory != null) {
        queryString.append("&category=").append(selectedCategory);
    }

    if (minPrice != null) {
        queryString.append("&minPrice=").append(minPrice);
    }

    if (maxPrice != null) {
        queryString.append("&maxPrice=").append(maxPrice);
    }

    if (selectedSort != null && !selectedSort.isEmpty()) {
        queryString.append("&sort=").append(
            java.net.URLEncoder.encode(selectedSort, "UTF-8")
        );
    }

    String previousUrl =
        contextPath + "/products?page=" + (currentPage - 1)
        + queryString.toString();

    String nextUrl =
        contextPath + "/products?page=" + (currentPage + 1)
        + queryString.toString();
%>

<!DOCTYPE html>
<html lang="en">

<head>

    <meta charset="UTF-8">

    <meta name="viewport"
          content="width=device-width, initial-scale=1.0">

    <title>Shop Products | FashionStore</title>

    <link rel="stylesheet"
          href="<%= contextPath %>/assets/css/common.css">

    <link rel="stylesheet"
          href="<%= contextPath %>/assets/css/header.css">

    <link rel="stylesheet"
          href="<%= contextPath %>/assets/css/navbar.css">

    <link rel="stylesheet"
          href="<%= contextPath %>/assets/css/footer.css">

    <link rel="stylesheet"
          href="<%= contextPath %>/assets/css/products.css">

</head>

<body>

    <!-- HEADER -->
    <jsp:include page="/WEB-INF/views/partials/header.jsp" />

    <!-- NAVBAR -->
    <jsp:include page="/WEB-INF/views/partials/navbar.jsp" />


    <main class="products-page">

        <!-- PAGE HEADING -->
        <section class="products-heading">

            <div>
                <h1>Explore Our Collection</h1>

                <p>
                    Discover clothing, footwear and accessories
                    for your everyday style.
                </p>
            </div>

            <span class="product-count">
                <%= products.size() %> products displayed
            </span>

        </section>


        <!-- FILTER AND PRODUCT LAYOUT -->
        <div class="products-layout">


            <!-- FILTER SIDEBAR -->
            <aside class="filter-sidebar">

                <div class="filter-heading">
                    <h2>Filters</h2>

                    <a href="<%= contextPath %>/products">
                        Reset All
                    </a>
                </div>


                <!-- SEARCH -->
                <form action="<%= contextPath %>/products"
                      method="get"
                      class="filter-form">

                    <div class="filter-group">

                        <h3>Search Products</h3>

                        <input
                            type="text"
                            name="keyword"
                            placeholder="Search products..."
                            value="<%= keyword != null ? keyword : "" %>"
                        >

                    </div>


                    <!-- CATEGORY FILTER -->
                    <div class="filter-group">

                        <h3>Categories</h3>

                        <select name="category">

                            <option value="">All Categories</option>

                            <% for (Category category : categories) { %>

                                <option
                                    value="<%= category.getCategoryId() %>"
                                    <%= selectedCategory != null
                                        && selectedCategory == category.getCategoryId()
                                        ? "selected" : "" %>
                                >

                                    <%= category.getCategoryName() %>

                                </option>

                            <% } %>

                        </select>

                    </div>


                    <!-- PRICE FILTER -->
                    <div class="filter-group">

                        <h3>Price Range (₹)</h3>

                        <div class="price-inputs">

                            <input
                                type="number"
                                name="minPrice"
                                placeholder="Min"
                                min="0"
                                step="0.01"
                                value="<%= minPrice != null ? minPrice : "" %>"
                            >

                            <input
                                type="number"
                                name="maxPrice"
                                placeholder="Max"
                                min="0"
                                step="0.01"
                                value="<%= maxPrice != null ? maxPrice : "" %>"
                            >

                        </div>

                    </div>


                    <button type="submit" class="filter-button">
                        Apply Filters
                    </button>

                </form>

            </aside>


            <!-- PRODUCT CONTENT -->
            <section class="products-content">


                <!-- TOOLBAR -->
                <div class="products-toolbar">

                    <div>
                        <h2>All Products</h2>

                        <p>
                            Find your perfect style.
                        </p>
                    </div>


                    <!-- SORTING -->
                    <form action="<%= contextPath %>/products"
                          method="get"
                          class="sort-form">

                        <% if (keyword != null && !keyword.isEmpty()) { %>
                            <input type="hidden"
                                   name="keyword"
                                   value="<%= keyword %>">
                        <% } %>

                        <% if (selectedCategory != null) { %>
                            <input type="hidden"
                                   name="category"
                                   value="<%= selectedCategory %>">
                        <% } %>

                        <% if (minPrice != null) { %>
                            <input type="hidden"
                                   name="minPrice"
                                   value="<%= minPrice %>">
                        <% } %>

                        <% if (maxPrice != null) { %>
                            <input type="hidden"
                                   name="maxPrice"
                                   value="<%= maxPrice %>">
                        <% } %>

                        <label for="sort">Sort by:</label>

                        <select id="sort"
                                name="sort"
                                onchange="this.form.submit()">

                            <option value="newest"
                                <%= "newest".equals(selectedSort)
                                    || selectedSort == null
                                    ? "selected" : "" %>>
                                Newest First
                            </option>

                            <option value="price_low"
                                <%= "price_low".equals(selectedSort)
                                    ? "selected" : "" %>>
                                Price: Low to High
                            </option>

                            <option value="price_high"
                                <%= "price_high".equals(selectedSort)
                                    ? "selected" : "" %>>
                                Price: High to Low
                            </option>

                            <option value="name_asc"
                                <%= "name_asc".equals(selectedSort)
                                    ? "selected" : "" %>>
                                Name: A to Z
                            </option>

                        </select>

                    </form>

                </div>


                <!-- PRODUCT GRID -->
                <div class="product-grid">

                    <% if (products.isEmpty()) { %>

                        <div class="empty-message">

                            <h3>No products found</h3>

                            <p>
                                Try changing your search or filters.
                            </p>

                            <a href="<%= contextPath %>/products">
                                View All Products
                            </a>

                        </div>

                    <% } else { %>

                        <% for (Product product : products) { %>

                            <article class="home-product-card">

                                <div class="product-image">

                                    <img
                                        src="<%= contextPath %>/<%= product.getImageUrl() %>"
                                        alt="<%= product.getProductName() %>"
                                        loading="lazy"
                                    >

                                </div>


                                <div class="product-information">

                                    <h3>
                                        <%= product.getProductName() %>
                                    </h3>

                                    <p class="product-price">
                                        <%= currencyFormat.format(
                                            product.getPrice()
                                        ) %>
                                    </p>

                                    <a
                                        class="product-details-link"
                                        href="<%= contextPath %>/product-details?id=<%= product.getProductId() %>"
                                    >
                                        View Details
                                    </a>

                                </div>

                            </article>

                        <% } %>

                    <% } %>

                </div>


                <!-- PAGINATION -->
                <div class="pagination">

                    <% if (currentPage > 1) { %>

                        <a href="<%= previousUrl %>"
                           class="pagination-button">
                            Previous
                        </a>

                    <% } else { %>

                        <span class="pagination-button disabled">
                            Previous
                        </span>

                    <% } %>


                    <span class="current-page">
                        Page <%= currentPage %>
                    </span>


                    <% if (hasNextPage) { %>

                        <a href="<%= nextUrl %>"
                           class="pagination-button">
                            Next
                        </a>

                    <% } else { %>

                        <span class="pagination-button disabled">
                            Next
                        </span>

                    <% } %>

                </div>

            </section>

        </div>

    </main>


    <!-- FOOTER -->
    <jsp:include page="/WEB-INF/views/partials/footer.jsp" />

</body>

</html>
