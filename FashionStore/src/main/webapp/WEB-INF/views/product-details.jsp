
<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8" isELIgnored="false" %>

<%@ page import="com.fashionstore.model.Product" %>
<%@ page import="com.fashionstore.model.ProductVariant" %>
<%@ page import="java.util.List" %>
<%@ page import="java.text.NumberFormat" %>
<%@ page import="java.util.Locale" %>

<%
    Product product = (Product) request.getAttribute("product");

    List<ProductVariant> variants =
        (List<ProductVariant>) request.getAttribute("variants");

    NumberFormat currencyFormat =
        NumberFormat.getCurrencyInstance(new Locale("en", "IN"));

    String contextPath = request.getContextPath();
%>

<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">

    <title><%= product.getProductName() %> | FashionStore</title>

    <link rel="stylesheet"
          href="<%= contextPath %>/assets/css/common.css">

    <link rel="stylesheet"
          href="<%= contextPath %>/assets/css/header.css">

    <link rel="stylesheet"
          href="<%= contextPath %>/assets/css/navbar.css">

    <link rel="stylesheet"
          href="<%= contextPath %>/assets/css/footer.css">
          

    <link rel="stylesheet"
          href="<%= contextPath %>/assets/css/product-details.css">
</head>

<body>

    <jsp:include page="/WEB-INF/views/partials/header.jsp" />

    <jsp:include page="/WEB-INF/views/partials/navbar.jsp" />

    <main class="product-details-page">

        <div class="breadcrumb">
            <a href="<%= contextPath %>/home">Home</a>
            <span>/</span>
            <a href="<%= contextPath %>/products">Products</a>
            <span>/</span>
            <span><%= product.getProductName() %></span>
        </div>

        <div class="product-details-container">

            <!-- Product Image -->
            <div class="product-image-section">

                <img
                    src="<%= contextPath %>/<%= product.getImageUrl() %>"
                    alt="<%= product.getProductName() %>"
                    class="main-product-image">

            </div>

            <!-- Product Information -->
            <div class="product-info-section">

                <h1><%= product.getProductName() %></h1>

                <p class="product-price">
                    <%= currencyFormat.format(product.getPrice()) %>
                </p>

                <div class="product-description">
                    <h3>Product Description</h3>

                    <p>
                        <%= product.getDescription() != null
                            ? product.getDescription()
                            : "No description available." %>
                    </p>
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
				
                <div class="product-availability">

                    <h3>Select Size and Colour</h3>

                    <% if (variants != null && !variants.isEmpty()) { %>

                        <form action="<%= contextPath %>/cart/add"
                              method="post">

                            <div class="variant-group">

                                <label for="variantId">
                                    Available Options
                                </label>

                                <select name="variantId"
                                        id="variantId"
                                        required>

                                    <option value="">
                                        Select size and colour
                                    </option>

                                    <% for (ProductVariant variant : variants) { %>

                                        <option
                                            value="<%= variant.getVariantId() %>"
                                            <%= variant.getStockQuantity() <= 0
                                                ? "disabled" : "" %>>

                                            <%= variant.getSize() %>
                                            -
                                            <%= variant.getColor() %>

                                            <% if (variant.getStockQuantity() <= 0) { %>
                                                (Out of Stock)
                                            <% } else { %>
                                                (Available: <%= variant.getStockQuantity() %>)
                                            <% } %>

                                        </option>

                                    <% } %>

                                </select>

                            </div>

                            <div class="quantity-group">

                                <label for="quantity">Quantity</label>

                                <input
                                    type="number"
                                    id="quantity"
                                    name="quantity"
                                    value="1"
                                    min="1"
                                    max="10"
                                    required>

                            </div>

                            <button type="submit"
                                    class="add-to-cart-button">

                                Add to Cart

                            </button>

                        </form>

                    <% } else { %>

                        <p class="out-of-stock-message">
                            Currently unavailable
                        </p>

                    <% } %>

                </div>

                <div class="product-extra-info">

                    <p>✓ Quality products</p>
                    <p>✓ Cash on Delivery available</p>
                    <p>✓ Secure shopping experience</p>

                </div>

            </div>

        </div>

    </main>

    <jsp:include page="/WEB-INF/views/partials/footer.jsp" />

</body>
</html>
