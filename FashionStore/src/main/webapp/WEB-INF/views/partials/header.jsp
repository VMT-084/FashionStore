
<%@ page contentType="text/html; charset=UTF-8"
         pageEncoding="UTF-8"
         isELIgnored="false" %>

<%
    String contextPath = request.getContextPath();

    Object countObject = session.getAttribute("cartItemCount");
    int cartItemCount = 0;

    if (countObject instanceof Number) {
        cartItemCount = ((Number) countObject).intValue();
    }

    Integer loggedInUserId =
            (Integer) session.getAttribute("userId");
%>

<header class="site-header">

    <div class="header-container">

        <a href="<%= contextPath %>/home"
           class="brand-logo">
            <span class="brand-name">FASHION</span>
            <span class="brand-highlight">STORE</span>
        </a>

        <form class="header-search"
              action="<%= contextPath %>/search"
              method="get">

            <span class="search-icon" aria-hidden="true">
                &#128269;
            </span>

            <input type="search"
                   name="keyword"
                   placeholder="Search for products..."
                   aria-label="Search products" />

            <button type="submit">Search</button>

        </form>

        <nav class="header-actions"
             aria-label="Main navigation">

            <% if (loggedInUserId != null) { %>

                <a href="<%= contextPath %>/orders"
                   class="header-link">
                    <span class="nav-icon">&#128230;</span>
                    <span>Orders</span>
                </a>

                <a href="<%= contextPath %>/cart"
                   class="header-link cart-link">
                    <span class="nav-icon">&#128722;</span>
                    <span>Cart</span>
                    <span class="cart-count">
                        <%= cartItemCount %>
                    </span>
                </a>

                <a href="<%= contextPath %>/profile"
                   class="header-link account-link">
                    <span class="nav-icon">&#128100;</span>
                    <span>My Account</span>
                </a>

                <form action="<%= contextPath %>/logout"
                      method="post"
                      class="header-logout-form">

                    <button type="submit"
                            class="header-link logout-button">
                        Logout
                    </button>

                </form>

            <% } else { %>

                <a href="<%= contextPath %>/login"
                   class="header-link">
                    Login
                </a>

                <a href="<%= contextPath %>/register"
                   class="header-link">
                    Register
                </a>

            <% } %>

        </nav>

    </div>

</header>
