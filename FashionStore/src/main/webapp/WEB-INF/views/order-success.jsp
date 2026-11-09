
<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8" isELIgnored="false" %>

<%@ page import="com.fashionstore.model.Order" %>
<%@ page import="java.text.NumberFormat" %>
<%@ page import="java.util.Locale" %>

<%
    Order order = (Order) request.getAttribute("order");

    String contextPath = request.getContextPath();

    NumberFormat currencyFormat =
        NumberFormat.getCurrencyInstance(new Locale("en", "IN"));
%>

<!DOCTYPE html>
<html lang="en">

<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">

    <title>Order Confirmed | FashionStore</title>

    <link rel="stylesheet"
        href="<%= contextPath %>/assets/css/common.css">
        
        <link rel="stylesheet"
          href="<%= contextPath %>/assets/css/header.css">

    <link rel="stylesheet"
          href="<%= contextPath %>/assets/css/navbar.css">

    <link rel="stylesheet"
          href="<%= contextPath %>/assets/css/footer.css">

    <link rel="stylesheet"
        href="<%= contextPath %>/assets/css/order-success.css">
</head>

<body>

    <jsp:include page="/WEB-INF/views/partials/header.jsp" />
    <jsp:include page="/WEB-INF/views/partials/navbar.jsp" />

    <main class="success-container">

        <section class="success-card">

            <div class="success-icon">
                &#10003;
            </div>

            <h1>Order Placed Successfully!</h1>

            <p class="success-message">
                Thank you for shopping with FashionStore.
                Your order has been received and is being processed.
            </p>

            <div class="order-id">
                <span>Order ID</span>
                <strong>#<%= order.getOrderId() %></strong>
            </div>

            <div class="order-details">

                <div class="detail-row">
                    <span>Order Status</span>
                    <strong><%= order.getOrderStatus() %></strong>
                </div>

                <div class="detail-row">
                    <span>Payment Method</span>
                    <strong>Cash on Delivery</strong>
                </div>

                <div class="detail-row">
                    <span>Payment Status</span>
                    <strong><%= order.getPaymentStatus() %></strong>
                </div>

                <div class="detail-row">
                    <span>Total Amount</span>
                    <strong>
                        <%= currencyFormat.format(order.getTotalAmount()) %>
                    </strong>
                </div>

            </div>

            <div class="delivery-details">

                <h2>Delivery Address</h2>

                <p><%= order.getDeliveryName() %></p>

                <p><%= order.getDeliveryAddress() %></p>

                <p>
                    <%= order.getDeliveryCity() %>,
                    <%= order.getDeliveryState() %> -
                    <%= order.getDeliveryPincode() %>
                </p>

                <p>Phone: <%= order.getDeliveryPhone() %></p>

            </div>

            <div class="success-actions">

                <a href="<%= contextPath %>/orders"
                   class="primary-btn">
                    View My Orders
                </a>

                <a href="<%= contextPath %>/products"
                   class="secondary-btn">
                    Continue Shopping
                </a>

            </div>

        </section>

    </main>

    <jsp:include page="/WEB-INF/views/partials/footer.jsp" />

</body>
</html>
