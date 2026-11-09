
<%@ page contentType="text/html; charset=UTF-8"
         pageEncoding="UTF-8"
         isELIgnored="false" %>

<%@ page import="java.util.List" %>
<%@ page import="com.fashionstore.model.Product" %>
<%@ page import="com.fashionstore.model.Category" %>

<%
    String contextPath = request.getContextPath();

    List<Category> categories =
        (List<Category>) request.getAttribute("categories");

    List<Product> featuredProducts =
        (List<Product>) request.getAttribute("featuredProducts");

    List<Product> latestProducts =
        (List<Product>) request.getAttribute("latestProducts");
%>

<!DOCTYPE html>
<html lang="en">

<head>

    <meta charset="UTF-8">

    <meta name="viewport"
          content="width=device-width, initial-scale=1.0">

    <title>FashionStore | Discover Your Style</title>

    <link rel="stylesheet"
          href="<%= contextPath %>/assets/css/common.css">

    <link rel="stylesheet"
          href="<%= contextPath %>/assets/css/header.css">

    <link rel="stylesheet"
          href="<%= contextPath %>/assets/css/navbar.css">

    <link rel="stylesheet"
          href="<%= contextPath %>/assets/css/home.css">

    <link rel="stylesheet"
          href="<%= contextPath %>/assets/css/footer.css">

</head>

<body>

    <!-- HEADER -->
    <jsp:include page="partials/header.jsp" />

    <!-- CATEGORY NAVIGATION -->
    <jsp:include page="partials/navbar.jsp" />


    <main>

        <!-- HERO SECTION -->
        <section class="hero-section">

            <div class="hero-container">

                <div class="hero-content">

                    <span class="hero-tag">
                        NEW SEASON COLLECTION
                    </span>

                    <h1>
                        Discover Your
                        <span>Unique Style</span>
                    </h1>

                    <p>
                        Explore the latest fashion trends,
                        discover your favourites and express
                        your style with FashionStore.
                    </p>

                    <a href="<%= contextPath %>/products"
                       class="btn hero-button">
                        Explore Collection
                    </a>

                </div>

                <div class="hero-image">

                    <img
                        src="<%= contextPath %>/assets/images/banners/hero-banner.jpg"
                        alt="FashionStore latest collection"
                    >

                </div>

            </div>

        </section>


        <!-- CATEGORY SECTION -->
        <section class="home-section container">

            <div class="section-header">

                <div>
                    <h2 class="section-heading">
                        Shop by Category
                    </h2>

                    <p class="section-subheading">
                        Find the perfect style for every occasion.
                    </p>
                </div>

                <a href="<%= contextPath %>/products"
                   class="view-all-link">
                    View All
                </a>

            </div>


            <div class="category-grid">

                <% if (categories != null && !categories.isEmpty()) { %>

                    <% for (Category category : categories) { %>

                        <a class="home-category-card"
                           href="<%= contextPath %>/products?category=<%= category.getCategoryId() %>">

                            <div class="category-card-content">

                                <h3>
                                    <%= category.getCategoryName() %>
                                </h3>

                                <span>Explore Collection →</span>

                            </div>

                        </a>

                    <% } %>

                <% } else { %>

                    <p class="empty-message">
                        Categories will appear here soon.
                    </p>

                <% } %>

            </div>

        </section>


        <!-- FEATURED PRODUCTS -->
        <section class="home-section container">

            <div class="section-header">

                <div>
                    <h2 class="section-heading">
                        Featured Products
                    </h2>

                    <p class="section-subheading">
                        Handpicked styles worth exploring.
                    </p>
                </div>

                <a href="<%= contextPath %>/products"
                   class="view-all-link">
                    View All
                </a>

            </div>


            <div class="product-grid">

                <% if (featuredProducts != null
                       && !featuredProducts.isEmpty()) { %>

                    <% for (Product product : featuredProducts) { %>

                        <article class="home-product-card">

                            <a href="<%= contextPath %>/product-details?id=<%= product.getProductId() %>">

                                <div class="product-image">

                                    <img
                                        src="<%= contextPath %>/<%= product.getImageUrl() %>"
                                        alt="<%= product.getProductName() %>"
                                    >

                                </div>

                                <div class="product-information">

                                    <h3>
                                        <%= product.getProductName() %>
                                    </h3>

                                    <p class="product-price">
                                        ₹<%= product.getPrice() %>
                                    </p>

                                </div>

                            </a>

                        </article>

                    <% } %>

                <% } else { %>

                    <p class="empty-message">
                        Featured products will appear here soon.
                    </p>

                <% } %>

            </div>

        </section>


        <!-- LATEST ARRIVALS -->
        <section class="home-section container">

            <div class="section-header">

                <div>
                    <h2 class="section-heading">
                        Latest Arrivals
                    </h2>

                    <p class="section-subheading">
                        Fresh additions to your wardrobe.
                    </p>
                </div>

                <a href="<%= contextPath %>/products"
                   class="view-all-link">
                    View All
                </a>

            </div>


            <div class="product-grid">

                <% if (latestProducts != null
                       && !latestProducts.isEmpty()) { %>

                    <% for (Product product : latestProducts) { %>

                        <article class="home-product-card">

                            <a href="<%= contextPath %>/product-details?id=<%= product.getProductId() %>">

                                <div class="product-image">

                                    <img
                                        src="<%= contextPath %>/<%= product.getImageUrl() %>"
                                        alt="<%= product.getProductName() %>"
                                    >

                                </div>

                                <div class="product-information">

                                    <h3>
                                        <%= product.getProductName() %>
                                    </h3>

                                    <p class="product-price">
                                        ₹<%= product.getPrice() %>
                                    </p>

                                </div>

                            </a>

                        </article>

                    <% } %>

                <% } else { %>

                    <p class="empty-message">
                        New arrivals will appear here soon.
                    </p>

                <% } %>

            </div>

        </section>

    </main>


    <!-- FOOTER -->
    <jsp:include page="partials/footer.jsp" />

</body>

</html>
