
<%@ page contentType="text/html; charset=UTF-8"
         pageEncoding="UTF-8"
         isELIgnored="false" %>

<%
    String contextPath = request.getContextPath();
    String selectedCategory = request.getParameter("category");

    if (selectedCategory == null) {
        selectedCategory = "";
    }
%>

<nav class="category-navbar" aria-label="Product categories">

    <div class="category-container">

        <a href="<%= contextPath %>/products"
           class="category-link <%= selectedCategory.isEmpty() ? "active" : "" %>">
            All Products
        </a>

        <a href="<%= contextPath %>/products?category=1"
           class="category-link <%= "1".equals(selectedCategory) ? "active" : "" %>">
            Men
        </a>

        <a href="<%= contextPath %>/products?category=2"
           class="category-link <%= "2".equals(selectedCategory) ? "active" : "" %>">
            Women
        </a>

        <a href="<%= contextPath %>/products?category=3"
           class="category-link <%= "3".equals(selectedCategory) ? "active" : "" %>">
            Footwear
        </a>

        <a href="<%= contextPath %>/products?category=4"
           class="category-link <%= "4".equals(selectedCategory) ? "active" : "" %>">
            Accessories
        </a>

    </div>

</nav>
