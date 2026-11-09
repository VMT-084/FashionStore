
<%@ page contentType="text/html; charset=UTF-8"
         pageEncoding="UTF-8"
         isELIgnored="false" %>

<%
    String contextPath = request.getContextPath();
%>

<footer class="site-footer">

    <div class="footer-container">

        <!-- BRAND INFORMATION -->
        <div class="footer-brand">

            <a href="<%= contextPath %>/home" class="footer-logo">
                FASHION<span>STORE</span>
            </a>

            <p>
                Discover your style with FashionStore.
                Explore clothing, footwear and accessories
                designed for your everyday fashion needs.
            </p>

        </div>


        <!-- SHOPPING LINKS -->
        <div class="footer-section">

            <h3>Shop</h3>

            <ul>
                <li>
                    <a href="<%= contextPath %>/products">
                        All Products
                    </a>
                </li>

                <li>
                    <a href="<%= contextPath %>/products?category=1">
                        Men
                    </a>
                </li>

                <li>
                    <a href="<%= contextPath %>/products?category=2">
                        Women
                    </a>
                </li>

                <li>
                    <a href="<%= contextPath %>/products?category=3">
                        Footwear
                    </a>
                </li>

                <li>
                    <a href="<%= contextPath %>/products?category=4">
                        Accessories
                    </a>
                </li>
            </ul>

        </div>


        <!-- CUSTOMER LINKS -->
        <div class="footer-section">

            <h3>My Account</h3>

            <ul>
                <li>
                    <a href="<%= contextPath %>/profile">
                        My Profile
                    </a>
                </li>

                <li>
                    <a href="<%= contextPath %>/orders">
                        My Orders
                    </a>
                </li>

                <li>
                    <a href="<%= contextPath %>/cart">
                        Shopping Cart
                    </a>
                </li>

                <li>
                    <a href="<%= contextPath %>/login">
                        Login
                    </a>
                </li>
            </ul>

        </div>


        <!-- SHOPPING INFORMATION -->
        <div class="footer-section">

            <h3>Shopping Information</h3>

            <ul>
                <li>Secure Customer Accounts</li>
                <li>Cash on Delivery</li>
                <li>Easy Order Tracking</li>
                <li>Quality Fashion Products</li>
            </ul>

        </div>

    </div>


    <!-- COPYRIGHT -->
    <div class="footer-bottom">

        <p>
            &copy; 2026 FashionStore.
            All rights reserved.
        </p>

    </div>

</footer>
