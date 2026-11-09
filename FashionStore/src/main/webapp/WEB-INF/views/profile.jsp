
<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8" isELIgnored="false" %>

<%@ page import="com.fashionstore.model.User" %>

<%
    User user = (User) request.getAttribute("user");

    String contextPath = request.getContextPath();

    String profileMessage =
        (String) session.getAttribute("profileMessage");

    if (profileMessage != null) {
        session.removeAttribute("profileMessage");
    }
%>

<!DOCTYPE html>
<html lang="en">

<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">

    <title>My Profile | FashionStore</title>

    <link rel="stylesheet"
        href="<%= contextPath %>/assets/css/common.css">
        
            <link rel="stylesheet"
          href="<%= contextPath %>/assets/css/header.css">

    <link rel="stylesheet"
          href="<%= contextPath %>/assets/css/navbar.css">

    <link rel="stylesheet"
          href="<%= contextPath %>/assets/css/footer.css">

    <link rel="stylesheet"
        href="<%= contextPath %>/assets/css/profile.css">
</head>

<body>

    <jsp:include page="/WEB-INF/views/partials/header.jsp" />
    <jsp:include page="/WEB-INF/views/partials/navbar.jsp" />

    <main class="profile-container">

        <div class="profile-heading">
            <h1>My Profile</h1>
            <p>Manage your personal information and delivery address.</p>
        </div>

        <% if (profileMessage != null) { %>

            <div class="profile-message">
                <%= profileMessage %>
            </div>

        <% } %>

        <div class="profile-content">

            <!-- Personal Information -->

            <section class="profile-card">

                <div class="profile-card-heading">
                    <h2>Personal Information</h2>
                    <p>Update your basic account information.</p>
                </div>

                <form action="<%= contextPath %>/profile"
                      method="post">

                    <input type="hidden"
                           name="action"
                           value="personal">

                    <div class="form-group">

                        <label for="fullName">Full Name</label>

                        <input
                            type="text"
                            id="fullName"
                            name="fullName"
                            value="<%= user.getFullName() %>"
                            maxlength="100"
                            required>

                    </div>

                    <div class="form-group">

                        <label for="email">Email Address</label>

                        <input
                            type="email"
                            id="email"
                            value="<%= user.getEmail() %>"
                            readonly>

                        <small>Email address cannot be changed.</small>

                    </div>

                    <div class="form-group">

                        <label for="phone">Phone Number</label>

                        <input
                            type="tel"
                            id="phone"
                            name="phone"
                            value="<%= user.getPhone() == null
                                ? "" : user.getPhone() %>"
                            pattern="[0-9]{10}"
                            maxlength="10"
                            required>

                    </div>

                    <button type="submit" class="save-btn">
                        Save Personal Information
                    </button>

                </form>

            </section>

            <!-- Delivery Address -->

            <section class="profile-card">

                <div class="profile-card-heading">
                    <h2>Saved Delivery Address</h2>
                    <p>Manage the address used during checkout.</p>
                </div>

                <form action="<%= contextPath %>/profile"
                      method="post">

                    <input type="hidden"
                           name="action"
                           value="address">

                    <div class="form-group">

                        <label for="address">Address</label>

                        <textarea
                            id="address"
                            name="address"
                            rows="3"
                            maxlength="500"
                            required><%= user.getAddress() == null
                                ? "" : user.getAddress() %></textarea>

                    </div>

                    <div class="form-group">

                        <label for="city">City</label>

                        <input
                            type="text"
                            id="city"
                            name="city"
                            value="<%= user.getCity() == null
                                ? "" : user.getCity() %>"
                            maxlength="100"
                            required>

                    </div>

                    <div class="form-group">

                        <label for="state">State</label>

                        <input
                            type="text"
                            id="state"
                            name="state"
                            value="<%= user.getState() == null
                                ? "" : user.getState() %>"
                            maxlength="100"
                            required>

                    </div>

                    <div class="form-group">

                        <label for="pincode">Pincode</label>

                        <input
                            type="text"
                            id="pincode"
                            name="pincode"
                            value="<%= user.getPincode() == null
                                ? "" : user.getPincode() %>"
                            pattern="[0-9]{6}"
                            maxlength="6"
                            required>

                    </div>

                    <button type="submit" class="save-btn">
                        Save Delivery Address
                    </button>

                </form>

            </section>

            <!-- Account Security -->

            <section class="profile-card security-card">

                <div class="profile-card-heading">
                    <h2>Account Security</h2>
                    <p>Keep your account secure by updating your password.</p>
                </div>

                <a href="<%= contextPath %>/change-password"
                   class="security-link">
                    Change Password
                    <span>&#8594;</span>
                </a>

            </section>

        </div>

    </main>

    <jsp:include page="/WEB-INF/views/partials/footer.jsp" />

</body>
</html>
