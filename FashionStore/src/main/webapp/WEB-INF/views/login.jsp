
<%@ page contentType="text/html; charset=UTF-8"
         pageEncoding="UTF-8"
         isELIgnored="false" %>

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

    String successMessage =
            (String) session.getAttribute("successMessage");

    session.removeAttribute("successMessage");

    String email = request.getParameter("email");
%>

<!DOCTYPE html>
<html lang="en">

<head>
    <meta charset="UTF-8">
    <meta name="viewport"
          content="width=device-width, initial-scale=1.0">

    <title>Login | FashionStore</title>

    <link rel="stylesheet"
          href="${pageContext.request.contextPath}/assets/css/common.css">
          
        <link rel="stylesheet"
          href="${pageContext.request.contextPath}/assets/css/header.css">
          
    <link rel="stylesheet"
          href="${pageContext.request.contextPath}/assets/css/footer.css">
          
    <link rel="stylesheet"
          href="${pageContext.request.contextPath}/assets/css/navbar.css">

    <link rel="stylesheet"
          href="${pageContext.request.contextPath}/assets/css/login.css">
</head>

<body>

    <jsp:include page="/WEB-INF/views/partials/header.jsp" />
    <jsp:include page="/WEB-INF/views/partials/navbar.jsp" />

    <main class="login-page">

        <section class="login-container">

            <div class="login-heading">
                <h1>Welcome Back</h1>
                <p>Login to your FashionStore account.</p>
            </div>

            <% if (successMessage != null) { %>
                <div class="login-success" role="status">
                    <%= escapeHtml(successMessage) %>
                </div>
            <% } %>

            <% if (errorMessage != null) { %>
                <div class="login-error" role="alert">
                    <%= escapeHtml(errorMessage) %>
                </div>
            <% } %>

            <form action="${pageContext.request.contextPath}/login"
                  method="post"
                  class="login-form">

                <div class="form-group">
                    <label for="email">Email Address</label>

                    <input type="email"
                           id="email"
                           name="email"
                           value="<%= escapeHtml(email) %>"
                           autocomplete="email"
                           placeholder="Enter your email address"
                           required>
                </div>

                <div class="form-group">
                    <label for="password">Password</label>

                    <input type="password"
                           id="password"
                           name="password"
                           autocomplete="current-password"
                           placeholder="Enter your password"
                           required>
                </div>

                <div class="forgot-password">
                    <a href="${pageContext.request.contextPath}/forgot-password">
                        Forgot Password?
                    </a>
                </div>

                <button type="submit" class="login-btn">
                    Login
                </button>

            </form>

            <p class="login-register">
                Don't have an account?
                <a href="${pageContext.request.contextPath}/register">
                    Create Account
                </a>
            </p>

        </section>

    </main>

    <jsp:include page="/WEB-INF/views/partials/footer.jsp" />

</body>
</html>
