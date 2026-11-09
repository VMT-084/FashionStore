
<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8" isELIgnored="false" %>

<%@ page import="java.util.List" %>
<%@ page import="java.text.NumberFormat" %>
<%@ page import="java.time.format.DateTimeFormatter" %>
<%@ page import="java.util.Locale" %>
<%@ page import="com.fashionstore.model.Order" %>
<%@ page import="com.fashionstore.model.OrderItem" %>

<%
    Order order = (Order) request.getAttribute("order");

    List<OrderItem> orderItems =
        (List<OrderItem>) request.getAttribute("orderItems");

    String contextPath = request.getContextPath();

    String orderMessage =
        (String) session.getAttribute("orderMessage");

    if (orderMessage != null) {
        session.removeAttribute("orderMessage");
    }

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

    <title>Order Details | FashionStore</title>

    <link rel="stylesheet"
        href="<%= contextPath %>/assets/css/common.css">
        
        <link rel="stylesheet"
          href="<%= contextPath %>/assets/css/header.css">

    <link rel="stylesheet"
          href="<%= contextPath %>/assets/css/navbar.css">

    <link rel="stylesheet"
          href="<%= contextPath %>/assets/css/footer.css">

    <link rel="stylesheet"
        href="<%= contextPath %>/assets/css/order-details.css">
</head>

<body>

    <jsp:include page="/WEB-INF/views/partials/header.jsp" />
    <jsp:include page="/WEB-INF/views/partials/navbar.jsp" />

    <main class="order-details-container">

        <div class="page-heading">
            <div>
                <h1>Order Details</h1>
                <p>Review your purchase and delivery information.</p>
            </div>

            <a href="<%= contextPath %>/orders"
               class="back-link">
                Back to My Orders
            </a>
        </div>

        <% if (orderMessage != null) { %>

            <div class="order-message">
                <%= orderMessage %>
            </div>

        <% } %>

        <!-- Order Information -->

        <section class="order-info-card">

            <div class="order-info-header">

                <div>
                    <span>Order ID</span>
                    <h2>#<%= order.getOrderId() %></h2>
                </div>

                <span class="order-status
                    <%= order.getOrderStatus()
                        .toLowerCase().replace(" ", "-") %>">
                    <%= order.getOrderStatus() %>
                </span>

            </div>

            <div class="order-info-grid">

                <div>
                    <span>Order Date</span>
                    <strong>
                        <%= order.getOrderDate() == null
                            ? "N/A"
                            : order.getOrderDate()
                                .format(dateFormatter) %>
                    </strong>
                </div>

                <div>
                    <span>Payment Method</span>
                    <strong>Cash on Delivery</strong>
                </div>

                <div>
                    <span>Payment Status</span>
                    <strong><%= order.getPaymentStatus() %></strong>
                </div>

                <div>
                    <span>Order Total</span>
                    <strong>
                        <%= currencyFormat.format(
                            order.getTotalAmount()) %>
                    </strong>
                </div>

            </div>

        </section>

        <!-- Purchased Products -->

        <section class="ordered-products">

            <h2>Items Ordered</h2>

            <% if (orderItems != null && !orderItems.isEmpty()) { %>

                <% for (OrderItem item : orderItems) { %>

                    <div class="ordered-product-card">

                        <div class="ordered-product-info">

                            <h3><%= item.getProductName() %></h3>

                            <p>Size: <%= item.getSize() %></p>

                            <p>Colour: <%= item.getColor() %></p>

                            <p>Quantity: <%= item.getQuantity() %></p>

                        </div>

                        <div class="ordered-product-price">

                            <span>Unit Price</span>

                            <strong>
                                <%= currencyFormat.format(
                                    item.getUnitPrice()) %>
                            </strong>

                            <span>Subtotal</span>

                            <strong>
                                <%= currencyFormat.format(
                                    item.getSubtotal()) %>
                            </strong>

                        </div>

                    </div>

                <% } %>

            <% } else { %>

                <p>No order items found.</p>

            <% } %>

        </section>

        <!-- Delivery Information -->

        <section class="delivery-info">

            <h2>Delivery Information</h2>

            <div class="delivery-address">

                <strong><%= order.getDeliveryName() %></strong>

                <p><%= order.getDeliveryAddress() %></p>

                <p>
                    <%= order.getDeliveryCity() %>,
                    <%= order.getDeliveryState() %> -
                    <%= order.getDeliveryPincode() %>
                </p>

                <p>Phone: <%= order.getDeliveryPhone() %></p>

            </div>

        </section>

        <!-- Order Total -->

        <section class="order-total-section">

            <span>Total Amount</span>

            <strong>
                <%= currencyFormat.format(order.getTotalAmount()) %>
            </strong>

        </section>

        <!-- Cancellation -->

        <% if ("Placed".equals(order.getOrderStatus())) { %>

            <section class="cancel-order-section">

                <div>
                    <h3>Cancel Order</h3>

                    <p>
                        You can cancel this order before it is shipped.
                    </p>
                </div>

                <form action="<%= contextPath %>/order-details"
                      method="post"
                      onsubmit="return confirm('Are you sure you want to cancel this order?');">

                    <input
                        type="hidden"
                        name="id"
                        value="<%= order.getOrderId() %>">

                    <button type="submit" class="cancel-order-btn">
                        Cancel Order
                    </button>

                </form>

            </section>

        <% } %>

    </main>

    <jsp:include page="/WEB-INF/views/partials/footer.jsp" />

</body>
</html>
