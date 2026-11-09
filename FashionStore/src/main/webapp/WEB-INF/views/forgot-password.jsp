
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

    String email = request.getParameter("email");
%>

<!DOCTYPE html>
<html lang="en">

<head>
    <meta charset="UTF-8">
    <meta name="viewport"
          content="width=device-width, initial-scale=1.0">

    <title>Reset Password | FashionStore</title>

    <link rel="stylesheet"
          href="${pageContext.request.contextPath}/assets/css/common.css">
          
         <link rel="stylesheet"
          href="${pageContext.request.contextPath}/assets/css/header.css">
          
    <link rel="stylesheet"
          href="${pageContext.request.contextPath}/assets/css/footer.css">
          
    <link rel="stylesheet"
          href="${pageContext.request.contextPath}/assets/css/navbar.css">

    <link rel="stylesheet"
          href="${pageContext.request.contextPath}/assets/css/forgot-password.css">
</head>

<body>

    <jsp:include page="/WEB-INF/views/partials/header.jsp" />
    <jsp:include page="/WEB-INF/views/partials/navbar.jsp" />

    <main class="forgot-password-page">

        <section class="forgot-password-container">

            <div class="forgot-password-heading">
                <h1>Reset Your Password</h1>

                <p>
                    Enter your registered email address
                    and choose a new password.
                </p>
            </div>

            <% if (errorMessage != null) { %>

                <div class="forgot-error" role="alert">
                    <%= escapeHtml(errorMessage) %>
                </div>

            <% } %>

            <form action="${pageContext.request.contextPath}/forgot-password"
                  method="post"
                  class="forgot-password-form">

                <div class="form-group">

                    <label for="email">Registered Email</label>

                    <input type="email"
                           id="email"
                           name="email"
                           value="<%= escapeHtml(email) %>"
                           placeholder="Enter your registered email"
                           autocomplete="email"
                           required>

                </div>

                <div class="form-group">

                    <label for="newPassword">New Password</label>

                    <input type="password"
                           id="newPassword"
                           name="newPassword"
                           placeholder="Enter new password"
                           minlength="8"
                           maxlength="72"
                           autocomplete="new-password"
                           required>

                    <small>Password must contain 8–72 characters.</small>

                </div>

                <div class="form-group">

                    <label for="confirmPassword">
                        Confirm New Password
                    </label>

                    <input type="password"
                           id="confirmPassword"
                           name="confirmPassword"
                           placeholder="Confirm new password"
                           minlength="8"
                           maxlength="72"
                           autocomplete="new-password"
                           required>

                </div>

                <button type="submit" class="reset-password-btn">
                    Reset Password
                </button>

                <a href="${pageContext.request.contextPath}/login"
                   class="back-login-link">
                    Back to Login
                </a>

            </form>

        </section>

    </main>

    <jsp:include page="/WEB-INF/views/partials/footer.jsp" />

</body>
</html>
