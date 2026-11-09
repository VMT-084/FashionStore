
<%@ page contentType="text/html; charset=UTF-8"
         pageEncoding="UTF-8"
         isELIgnored="false" %>
<%@ page import="java.util.Objects" %>

<%!
    private String escapeHtml(String value) {
        if (value == null) return "";
        return value.replace("&", "&amp;")
                    .replace("<", "&lt;")
                    .replace(">", "&gt;")
                    .replace("\"", "&quot;")
                    .replace("'", "&#39;");
    }
%>

<%
    String errorMessage =
            (String) request.getAttribute("errorMessage");

    String fullName = Objects.toString(
            request.getParameter("fullName"), "");

    String email = Objects.toString(
            request.getParameter("email"), "");

    String phone = Objects.toString(
            request.getParameter("phone"), "");
%>

<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport"
          content="width=device-width, initial-scale=1.0">

    <title>Create Account | FashionStore</title>

    <link rel="stylesheet"
          href="${pageContext.request.contextPath}/assets/css/common.css">
          
         <link rel="stylesheet"
          href="${pageContext.request.contextPath}/assets/css/header.css">
          
    <link rel="stylesheet"
          href="${pageContext.request.contextPath}/assets/css/footer.css">
          
    <link rel="stylesheet"
          href="${pageContext.request.contextPath}/assets/css/navbar.css">

    <link rel="stylesheet"
          href="${pageContext.request.contextPath}/assets/css/register.css">
</head>

<body>

    <jsp:include page="/WEB-INF/views/partials/header.jsp" />
    <jsp:include page="/WEB-INF/views/partials/navbar.jsp" />

    <main class="register-page">

        <section class="register-container">

            <div class="register-heading">
                <h1>Create an Account</h1>
                <p>Join FashionStore and start shopping.</p>
            </div>

            <% if (errorMessage != null) { %>
                <div class="register-error" role="alert">
                    <%= escapeHtml(errorMessage) %>
                </div>
            <% } %>

            <form action="${pageContext.request.contextPath}/register"
                  method="post"
                  class="register-form">

                <div class="form-group">
                    <label for="fullName">Full Name</label>
                    <input type="text"
                           id="fullName"
                           name="fullName"
                           value="<%= escapeHtml(fullName) %>"
                           maxlength="100"
                           autocomplete="name"
                           placeholder="Enter your full name"
                           required>
                </div>

                <div class="form-group">
                    <label for="email">Email Address</label>
                    <input type="email"
                           id="email"
                           name="email"
                           value="<%= escapeHtml(email) %>"
                           maxlength="254"
                           autocomplete="email"
                           placeholder="Enter your email"
                           required>
                </div>

                <div class="form-group">
                    <label for="phone">Phone Number</label>
                    <input type="tel"
                           id="phone"
                           name="phone"
                           value="<%= escapeHtml(phone) %>"
                           maxlength="10"
                           pattern="[0-9]{10}"
                           autocomplete="tel"
                           placeholder="Enter 10-digit phone number"
                           required>
                </div>

                <div class="form-group">
                    <label for="password">Password</label>
                    <input type="password"
                           id="password"
                           name="password"
                           minlength="8"
                           maxlength="72"
                           autocomplete="new-password"
                           placeholder="Create a password"
                           required>
                    <small>Use 8–72 characters.</small>
                </div>

                <div class="form-group">
                    <label for="confirmPassword">
                        Confirm Password
                    </label>
                    <input type="password"
                           id="confirmPassword"
                           name="confirmPassword"
                           minlength="8"
                           maxlength="72"
                           autocomplete="new-password"
                           placeholder="Re-enter your password"
                           required>
                </div>

                <button type="submit" class="register-btn">
                    Create Account
                </button>

            </form>

            <p class="register-login">
                Already have an account?
                <a href="${pageContext.request.contextPath}/login">
                    Login here
                </a>
            </p>

        </section>

    </main>

    <jsp:include page="/WEB-INF/views/partials/footer.jsp" />

</body>
</html>
