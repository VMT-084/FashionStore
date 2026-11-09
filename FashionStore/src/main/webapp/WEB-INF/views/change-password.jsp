
<%@ page contentType="text/html; charset=UTF-8"
         pageEncoding="UTF-8"
         isELIgnored="false" %>

<!DOCTYPE html>
<html lang="en">

<head>
    <meta charset="UTF-8">
    <meta name="viewport"
          content="width=device-width, initial-scale=1.0">

    <title>Change Password | FashionStore</title>

    <link rel="stylesheet"
          href="${pageContext.request.contextPath}/assets/css/common.css">
          
    <link rel="stylesheet"
          href="${pageContext.request.contextPath}/assets/css/header.css">
          
    <link rel="stylesheet"
          href="${pageContext.request.contextPath}/assets/css/footer.css">
          
    <link rel="stylesheet"
          href="${pageContext.request.contextPath}/assets/css/navbar.css">

    <link rel="stylesheet"
          href="${pageContext.request.contextPath}/assets/css/change-password.css">
</head>

<body>

    <jsp:include page="/WEB-INF/views/partials/header.jsp" />
    <jsp:include page="/WEB-INF/views/partials/navbar.jsp" />

    <main class="password-page">

        <div class="password-container">

            <div class="password-heading">
                <h1>Change Password</h1>
                <p>Update your account password to keep your account secure.</p>
            </div>

            <% if (request.getAttribute("errorMessage") != null) { %>

                <div class="password-message error-message">
                    <%= request.getAttribute("errorMessage") %>
                </div>

            <% } %>

            <form action="${pageContext.request.contextPath}/change-password"
                  method="post"
                  class="password-form">

                <div class="form-group">
                    <label for="currentPassword">
                        Current Password
                    </label>

                    <input type="password"
                           id="currentPassword"
                           name="currentPassword"
                           placeholder="Enter current password"
                           autocomplete="current-password"
                           required>
                </div>

                <div class="form-group">
                    <label for="newPassword">
                        New Password
                    </label>

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

                <button type="submit" class="change-password-btn">
                    Update Password
                </button>

                <a href="${pageContext.request.contextPath}/profile"
                   class="back-profile-link">
                    Back to Profile
                </a>

            </form>

        </div>

    </main>

    <jsp:include page="/WEB-INF/views/partials/footer.jsp" />

</body>
</html>
