<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8" isELIgnored="false" %>

<%@ page import="java.util.List" %>
<%@ page import="java.text.NumberFormat" %>
<%@ page import="java.util.Locale" %>
<%@ page import="com.fashionstore.model.Product" %>

<%
    String contextPath = request.getContextPath();

    List<Product> products =
        (List<Product>) request.getAttribute("products");

    String keyword = (String) request.getAttribute("keyword");

    if (products == null) {
        products = java.util.Collections.emptyList();
    }

    NumberFormat currencyFormat =
        NumberFormat.getCurrencyInstance(new Locale("en", "IN"));
%>

<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">

    <title>Search Results | FashionStore</title>

    <link rel="stylesheet"
        href="<%= contextPath %>/assets/css/common.css">

    <link rel="stylesheet"
        href="<%= contextPath %>/assets/css/header.css">

    <link rel="stylesheet"
        href="<%= contextPath %>/assets/css/navbar.css">

    <link rel="stylesheet"
        href="<%= contextPath %>/assets/css/footer.css">

    <link rel="stylesheet"
        href="<%= contextPath %>/assets/css/search.css">
</head>

<body>

    <jsp:include page="/WEB-INF/views/partials/header.jsp" />
    <jsp:include page="/WEB-INF/views/partials/navbar.jsp" />

    <main class="search-page">

        <div class="search-heading">
            <h1>Search Results</h1>

            <p>
                Results for:
                <strong><%= keyword %></strong>
            </p>

            <span>
                <%= products.size() %> product(s) found
            </span>
        </div>

        <% if (products.isEmpty()) { %>

            <div class="no-search-results">
                <h2>No products found</h2>

                <p>
                    We couldn't find any products matching
                    "<%= keyword %>".
                </p>

                <a href="<%= contextPath %>/products">
                    Browse All Products
                </a>
            </div>

        <% } else { %>

            <div class="search-product-grid">

                <% for (Product product : products) { %>

                    <div class="search-product-card">

                        <a href="<%= contextPath %>/product-details?id=<%= product.getProductId() %>"
                            class="search-product-image">

                            <img
                                src="<%= contextPath %>/<%= product.getImageUrl() %>"
                                alt="<%= product.getProductName() %>">
                        </a>

                        <div class="search-product-info">

                            <h3>
                                <%= product.getProductName() %>
                            </h3>

                            <p class="search-product-price">
                                <%= currencyFormat.format(
                                    product.getPrice()) %>
                            </p>

                            <a
                                href="<%= contextPath %>/product-details?id=<%= product.getProductId() %>"
                                class="view-product-btn">
                                View Product
                            </a>

                        </div>

                    </div>

                <% } %>

            </div>

        <% } %>

    </main>

    <jsp:include page="/WEB-INF/views/partials/footer.jsp" />

</body>
</html>