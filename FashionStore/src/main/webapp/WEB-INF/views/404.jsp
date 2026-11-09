<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8" isELIgnored="false" %>

<%
    String contextPath = request.getContextPath();
%>

<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">

    <title>Page Not Found | FashionStore</title>

    <link rel="stylesheet"
        href="<%= contextPath %>/assets/css/common.css">
    <link rel="stylesheet"
    href="<%= contextPath %>/assets/css/error.css">
</head>

<body>

    <div class="error-container">

        <h1>404</h1>

        <h2>Oops! Page Not Found</h2>

        <p>
            The page you're looking for doesn't exist
            or may have been moved.
        </p>

        <a href="<%= contextPath %>/home">
            Back to Home
        </a>

    </div>

</body>
</html>