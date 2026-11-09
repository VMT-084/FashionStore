
<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8" isELIgnored="false" %>

<%@ page import="java.util.List" %>
<%@ page import="java.text.NumberFormat" %>
<%@ page import="java.time.format.DateTimeFormatter" %>
<%@ page import="java.util.Locale" %>
<%@ page import="com.fashionstore.model.Order" %>

<%
    List<Order> orders =
        (List<Order>) request.getAttribute("orders");

    Integer currentPage =
        (Integer) request.getAttribute("currentPage");

    Integer totalPages =
        (Integer) request.getAttribute("totalPages");

    Integer totalOrders =
        (Integer) request.getAttribute("totalOrders");

    if (orders == null) {
        orders = java.util.Collections.emptyList();
    }

    if (currentPage == null) currentPage = 1;
    if (totalPages == null) totalPages = 0;
    if (totalOrders == null) totalOrders = 0;

    String contextPath = request.getContextPath();

    NumberFormat currencyFormat =
        NumberFormat.getCurrencyInstance(new Locale("en", "IN"));

    DateTimeFormatter dateFormatter =
        DateTimeFormatter.ofPattern("dd MMM yyyy, hh:mm a");
%>

<!DOCTYPE html>
<html lang="en">

<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">

    <title>My Orders | FashionStore</title>

    <link rel="stylesheet"
        href="<%= contextPath %>/assets/css/common.css">

        <link rel="stylesheet"
          href="<%= contextPath %>/assets/css/header.css">

    <link rel="stylesheet"
          href="<%= contextPath %>/assets/css/navbar.css">

    <link rel="stylesheet"
          href="<%= contextPath %>/assets/css/footer.css">

    <link rel="stylesheet"
        href="<%= contextPath %>/assets/css/orders.css">
</head>

<body>

    <jsp:include page="/WEB-INF/views/partials/header.jsp" />
    <jsp:include page="/WEB-INF/views/partials/navbar.jsp" />

    <main class="orders-container">

        <div class="orders-heading">
            <div>
                <h1>My Orders</h1>
                <p>View and track your previous purchases.</p>
            </div>

            <span class="order-count">
                <%= totalOrders %> Orders
            </span>
        </div>

        <% if (orders.isEmpty()) { %>

            <section class="empty-orders">

                <div class="empty-orders-icon">
                    &#128722;
                </div>

                <h2>No Orders Yet</h2>

                <p>
                    You haven't placed any orders yet.
                    Explore our collection and find something you love.
                </p>

                <a href="<%= contextPath %>/products"
                   class="shop-now-btn">
                    Start Shopping
                </a>

            </section>

        <% } else { %>

            <section class="orders-list">

                <% for (Order order : orders) { %>

                    <article class="order-card">

                        <div class="order-card-header">

                            <div>
                                <span class="order-label">Order ID</span>
                                <strong>
                                    #<%= order.getOrderId() %>
                                </strong>
                            </div>

                            <span class="order-status
                                <%= order.getOrderStatus()
                                    .toLowerCase().replace(" ", "-") %>">
                                <%= order.getOrderStatus() %>
                            </span>

                        </div>

                        <div class="order-card-details">

                            <div class="order-detail">
                                <span>Order Date</span>

                                <strong>
                                    <%= order.getOrderDate() == null
                                        ? "N/A"
                                        : order.getOrderDate()
                                            .format(dateFormatter) %>
                                </strong>
                            </div>

                            <div class="order-detail">
                                <span>Total Amount</span>

                                <strong>
                                    <%= currencyFormat.format(
                                        order.getTotalAmount()) %>
                                </strong>
                            </div>

                            <div class="order-detail">
                                <span>Payment Method</span>

                                <strong>Cash on Delivery</strong>
                            </div>

                            <div class="order-detail">
                                <span>Payment Status</span>

                                <strong>
                                    <%= order.getPaymentStatus() %>
                                </strong>
                            </div>

                        </div>

                        <div class="order-card-footer">

                            <span>
                                Delivery to: <%= order.getDeliveryCity() %>
                            </span>

                            <a href="<%= contextPath %>/order-details?id=<%= order.getOrderId() %>"
                               class="view-order-btn">
                                View Details
                            </a>

                        </div>

                    </article>

                <% } %>

            </section>

            <!-- Pagination -->

            <% if (totalPages > 1) { %>

                <nav class="pagination">

                    <% if (currentPage > 1) { %>

                        <a href="<%= contextPath %>/orders?page=<%= currentPage - 1 %>"
                           class="page-btn">
                            Previous
                        </a>

                    <% } %>

                    <% for (int i = 1; i <= totalPages; i++) { %>

                        <a href="<%= contextPath %>/orders?page=<%= i %>"
                           class="page-number
                           <%= currentPage == i ? "active" : "" %>">
                            <%= i %>
                        </a>

                    <% } %>

                    <% if (currentPage < totalPages) { %>

                        <a href="<%= contextPath %>/orders?page=<%= currentPage + 1 %>"
                           class="page-btn">
                            Next
                        </a>

                    <% } %>

                </nav>

            <% } %>

        <% } %>

    </main>

    <jsp:include page="/WEB-INF/views/partials/footer.jsp" />

</body>
</html>
