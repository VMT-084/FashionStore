
<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8" isELIgnored="false" %>

<%@ page import="java.util.List" %>
<%@ page import="java.math.BigDecimal" %>
<%@ page import="com.fashionstore.model.User" %>
<%@ page import="com.fashionstore.model.CartItem" %>

<%
    User user = (User) request.getAttribute("user");
    List<CartItem> cartItems =
        (List<CartItem>) request.getAttribute("cartItems");

    BigDecimal cartTotal =
        (BigDecimal) request.getAttribute("cartTotal");

    String contextPath = request.getContextPath();

    String checkoutMessage =
        (String) session.getAttribute("checkoutMessage");

    if (checkoutMessage != null) {
        session.removeAttribute("checkoutMessage");
    }
%>

<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">

    <title>Checkout | FashionStore</title>

    <link rel="stylesheet"
        href="<%= contextPath %>/assets/css/common.css">
        
        <link rel="stylesheet"
          href="<%= contextPath %>/assets/css/header.css">

    <link rel="stylesheet"
          href="<%= contextPath %>/assets/css/navbar.css">

    <link rel="stylesheet"
          href="<%= contextPath %>/assets/css/footer.css">

    <link rel="stylesheet"
        href="<%= contextPath %>/assets/css/checkout.css">
</head>

<body>

    <jsp:include page="/WEB-INF/views/partials/header.jsp" />
    <jsp:include page="/WEB-INF/views/partials/navbar.jsp" />

    <main class="checkout-container">

        <div class="checkout-heading">
            <h1>Checkout</h1>
            <p>Complete your delivery details to place your order.</p>
        </div>

        <% if (checkoutMessage != null) { %>
            <div class="checkout-message">
                <%= checkoutMessage %>
            </div>
        <% } %>

        <form action="<%= contextPath %>/checkout"
              method="post"
              class="checkout-form">

            <div class="checkout-content">

                <!-- Delivery Details -->
                <section class="delivery-section">

                    <h2>Delivery Information</h2>

                    <div class="form-group">
                        <label for="deliveryName">Full Name</label>
                        <input
                            type="text"
                            id="deliveryName"
                            name="deliveryName"
                            value="<%= user.getFullName() %>"
                            maxlength="100"
                            required>
                    </div>

                    <div class="form-group">
                        <label for="deliveryPhone">Phone Number</label>
                        <input
                            type="tel"
                            id="deliveryPhone"
                            name="deliveryPhone"
                            value="<%= user.getPhone() == null ? "" : user.getPhone() %>"
                            pattern="[0-9]{10}"
                            maxlength="10"
                            required>
                    </div>

                    <div class="form-group">
                        <label for="deliveryAddress">Address</label>
                        <textarea
                            id="deliveryAddress"
                            name="deliveryAddress"
                            rows="3"
                            maxlength="500"
                            required><%= user.getAddress() == null ? "" : user.getAddress() %></textarea>
                    </div>

                    <div class="form-row">

                        <div class="form-group">
                            <label for="deliveryCity">City</label>
                            <input
                                type="text"
                                id="deliveryCity"
                                name="deliveryCity"
                                value="<%= user.getCity() == null ? "" : user.getCity() %>"
                                maxlength="100"
                                required>
                        </div>

                        <div class="form-group">
                            <label for="deliveryState">State</label>
                            <input
                                type="text"
                                id="deliveryState"
                                name="deliveryState"
                                value="<%= user.getState() == null ? "" : user.getState() %>"
                                maxlength="100"
                                required>
                        </div>

                    </div>

                    <div class="form-group">
                        <label for="deliveryPincode">Pincode</label>
                        <input
                            type="text"
                            id="deliveryPincode"
                            name="deliveryPincode"
                            value="<%= user.getPincode() == null ? "" : user.getPincode() %>"
                            pattern="[0-9]{6}"
                            maxlength="6"
                            required>
                    </div>

                    <p class="address-note">
                        Your saved address is pre-filled. You can edit
                        these details for this order.
                    </p>

                </section>

                <!-- Order Summary -->
                <aside class="order-summary">

                    <h2>Order Summary</h2>

                    <div class="summary-products">

                        <% for (CartItem item : cartItems) { %>

                            <div class="summary-product">

                                <img
                                    src="<%= contextPath %>/<%= item.getImageUrl() %>"
                                    alt="<%= item.getProductName() %>">

                                <div class="summary-product-details">

                                    <h3>
                                        <%= item.getProductName() %>
                                    </h3>

                                    <p>
                                        Size: <%= item.getSize() %>
                                    </p>

                                    <p>
                                        Colour: <%= item.getColor() %>
                                    </p>

                                    <p>
                                        Quantity: <%= item.getQuantity() %>
                                    </p>

                                    <strong>
                                        ₹<%= item.getSubtotal() %>
                                    </strong>

                                </div>

                            </div>

                        <% } %>

                    </div>

                    <div class="summary-total">
                        <span>Total Amount</span>
                        <strong>₹<%= cartTotal %></strong>
                    </div>

                    <div class="payment-method">

                        <h3>Payment Method</h3>

                        <div class="cod-option">
                            <input
                                type="radio"
                                id="cod"
                                name="paymentMethod"
                                value="COD"
                                checked
                                disabled>

                            <label for="cod">
                                Cash on Delivery
                            </label>
                        </div>

                        <p>
                            Pay in cash when your order is delivered.
                        </p>

                    </div>

                    <button type="submit" class="place-order-btn">
                        Place Order
                    </button>

                    <a href="<%= contextPath %>/cart"
                       class="back-to-cart">
                        Back to Cart
                    </a>

                </aside>

            </div>

        </form>

    </main>

    <jsp:include page="/WEB-INF/views/partials/footer.jsp" />

</body>
</html>
