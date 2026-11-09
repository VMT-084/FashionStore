
<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8" isELIgnored="false" %>

<%@ page import="java.util.List" %>
<%@ page import="java.math.BigDecimal" %>
<%@ page import="java.text.NumberFormat" %>
<%@ page import="java.util.Locale" %>
<%@ page import="com.fashionstore.model.CartItem" %>

<%
    List<CartItem> cartItems =
        (List<CartItem>) request.getAttribute("cartItems");

    BigDecimal cartTotal =
        (BigDecimal) request.getAttribute("cartTotal");

    Integer totalItemCount =
        (Integer) request.getAttribute("totalItemCount");

    if (cartItems == null) {
        cartItems = java.util.Collections.emptyList();
    }

    if (cartTotal == null) {
        cartTotal = BigDecimal.ZERO;
    }

    if (totalItemCount == null) {
        totalItemCount = 0;
    }

    NumberFormat currencyFormat =
        NumberFormat.getCurrencyInstance(new Locale("en", "IN"));

    String contextPath = request.getContextPath();
%>

<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">

    <title>Shopping Cart | FashionStore</title>

    <link rel="stylesheet"
          href="<%= contextPath %>/assets/css/common.css">

    <link rel="stylesheet"
          href="<%= contextPath %>/assets/css/cart.css">
          
    <link rel="stylesheet"
          href="<%= contextPath %>/assets/css/header.css">

    <link rel="stylesheet"
          href="<%= contextPath %>/assets/css/navbar.css">

    <link rel="stylesheet"
          href="<%= contextPath %>/assets/css/footer.css">
    
</head>

<body>

    <jsp:include page="/WEB-INF/views/partials/header.jsp" />

    <jsp:include page="/WEB-INF/views/partials/navbar.jsp" />

    <main class="cart-page">

        <div class="cart-heading">
            <h1>Shopping Cart</h1>
            <p><%= totalItemCount %> item(s) in your cart</p>
        </div>
		
		<%
    	String cartMessage =
        	(String) session.getAttribute("cartMessage");

    	if (cartMessage != null) {
        	session.removeAttribute("cartMessage");
    	}
		%>

		<% if (cartMessage != null) { %>

    	<div class="cart-message">
        <%= cartMessage %>
    	</div>

		<% } %>
		
        <% if (cartItems.isEmpty()) { %>

            <div class="empty-cart">

                <h2>Your cart is empty</h2>

                <p>
                    Looks like you haven't added anything to your cart yet.
                </p>

                <a href="<%= contextPath %>/products"
                   class="continue-shopping-button">
                    Continue Shopping
                </a>

            </div>

        <% } else { %>

            <div class="cart-layout">

                <!-- Cart Items -->

                <section class="cart-items-section">

                    <% for (CartItem item : cartItems) { %>

                        <div class="cart-item">

                            <div class="cart-item-image">

                                <img
                                    src="<%= contextPath %>/<%= item.getImageUrl() %>"
                                    alt="<%= item.getProductName() %>">

                            </div>

                            <div class="cart-item-details">

                                <h3>
                                    <%= item.getProductName() %>
                                </h3>

                                <p class="cart-variant">
                                    Size: <%= item.getSize() %>
                                </p>

                                <p class="cart-variant">
                                    Colour: <%= item.getColor() %>
                                </p>

                                <p class="cart-item-price">
                                    <%= currencyFormat.format(item.getPrice()) %>
                                </p>

                                <div class="cart-item-actions">

                                    <form action="<%= contextPath %>/cart/update"
                                          method="post"
                                          class="quantity-form">

                                        <input type="hidden"
                                               name="cartItemId"
                                               value="<%= item.getCartItemId() %>">

                                        <label for="quantity-<%= item.getCartItemId() %>">
                                            Quantity:
                                        </label>

                                        <input
                                            type="number"
                                            id="quantity-<%= item.getCartItemId() %>"
                                            name="quantity"
                                            value="<%= item.getQuantity() %>"
                                            min="1"
                                            max="<%= Math.min(10, item.getStockQuantity()) %>"
                                            required>

                                        <button type="submit">
                                            Update
                                        </button>

                                    </form>

                                    <form action="<%= contextPath %>/cart/remove"
                                          method="post">

                                        <input type="hidden"
                                               name="cartItemId"
                                               value="<%= item.getCartItemId() %>">

                                        <button type="submit"
                                                class="remove-item-button">
                                            Remove
                                        </button>

                                    </form>

                                </div>

                            </div>

                            <div class="cart-item-subtotal">

                                <span>Subtotal</span>

                                <strong>
                                    <%= currencyFormat.format(
                                        item.getSubtotal()
                                    ) %>
                                </strong>

                            </div>

                        </div>

                    <% } %>

                    <a href="<%= contextPath %>/products"
                       class="continue-shopping-link">
                        ← Continue Shopping
                    </a>

                </section>

                <!-- Order Summary -->

                <aside class="cart-summary">

                    <h2>Order Summary</h2>

                    <div class="summary-row">
                        <span>Total Items</span>
                        <span><%= totalItemCount %></span>
                    </div>

                    <div class="summary-row">
                        <span>Subtotal</span>
                        <span>
                            <%= currencyFormat.format(cartTotal) %>
                        </span>
                    </div>

                    <div class="summary-row">
                        <span>Shipping</span>
                        <span>Calculated at checkout</span>
                    </div>

                    <div class="summary-divider"></div>

                    <div class="summary-total">
                        <span>Total</span>
                        <strong>
                            <%= currencyFormat.format(cartTotal) %>
                        </strong>
                    </div>

                    <a href="<%= contextPath %>/checkout"
                       class="checkout-button">
                        Proceed to Checkout
                    </a>

                    <p class="payment-note">
                        Cash on Delivery available
                    </p>

                </aside>

            </div>

        <% } %>

    </main>

    <jsp:include page="/WEB-INF/views/partials/footer.jsp" />

</body>
</html>
