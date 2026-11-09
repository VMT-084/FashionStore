
package com.fashionstore.controller;

import java.io.IOException;
import java.sql.SQLException;

import com.fashionstore.dao.UserDAO;
import com.fashionstore.dao.impl.UserDAOImpl;
import com.fashionstore.util.PasswordUtil;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

public class ForgotPasswordServlet extends HttpServlet {

    private static final long serialVersionUID = 1L;

    private UserDAO userDAO;

    @Override
    public void init() {
        userDAO = new UserDAOImpl();
    }

    @Override
    protected void doGet(
            HttpServletRequest request,
            HttpServletResponse response)
            throws ServletException, IOException {

        request.getRequestDispatcher(
                "/WEB-INF/views/forgot-password.jsp"
        ).forward(request, response);
    }

    @Override
    protected void doPost(
            HttpServletRequest request,
            HttpServletResponse response)
            throws ServletException, IOException {

        request.setCharacterEncoding("UTF-8");

        String email = request.getParameter("email");
        String newPassword = request.getParameter("newPassword");
        String confirmPassword =
                request.getParameter("confirmPassword");

        if (email == null || newPassword == null ||
                confirmPassword == null ||
                email.isBlank() || newPassword.isBlank() ||
                confirmPassword.isBlank()) {

            request.setAttribute(
                    "errorMessage",
                    "Please fill in all fields."
            );

            request.getRequestDispatcher(
                    "/WEB-INF/views/forgot-password.jsp"
            ).forward(request, response);

            return;
        }

        email = email.trim().toLowerCase();

        if (!email.matches(
                "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$")) {

            request.setAttribute(
                    "errorMessage",
                    "Please enter a valid email address."
            );

            request.getRequestDispatcher(
                    "/WEB-INF/views/forgot-password.jsp"
            ).forward(request, response);

            return;
        }

        // Validate new password strength
        if (!isStrongPassword(newPassword)) {

            request.setAttribute(
                    "errorMessage",
                    "Password must contain 8 to 72 characters, "
                    + "including at least one uppercase letter, "
                    + "one lowercase letter, one digit, "
                    + "and one special character."
            );

            request.getRequestDispatcher(
                    "/WEB-INF/views/forgot-password.jsp"
            ).forward(request, response);

            return;
        }

        if (!newPassword.equals(confirmPassword)) {

            request.setAttribute(
                    "errorMessage",
                    "Passwords do not match."
            );

            request.getRequestDispatcher(
                    "/WEB-INF/views/forgot-password.jsp"
            ).forward(request, response);

            return;
        }

        try {

            if (!userDAO.emailExists(email)) {

                request.setAttribute(
                        "errorMessage",
                        "No account found with this email address."
                );

                request.getRequestDispatcher(
                        "/WEB-INF/views/forgot-password.jsp"
                ).forward(request, response);

                return;
            }

            String passwordHash =
                    PasswordUtil.hashPassword(newPassword);

            boolean updated = userDAO.updatePassword(
                    userDAO.getUserByEmail(email).getUserId(),
                    passwordHash
            );

            if (updated) {

                request.getSession().setAttribute(
                        "successMessage",
                        "Password reset successfully. Please log in."
                );

                response.sendRedirect(
                        request.getContextPath() + "/login"
                );

            } else {

                request.setAttribute(
                        "errorMessage",
                        "Unable to reset password. Please try again."
                );

                request.getRequestDispatcher(
                        "/WEB-INF/views/forgot-password.jsp"
                ).forward(request, response);
            }

        } catch (SQLException e) {

            throw new ServletException(
                    "Error while resetting password.",
                    e
            );
        }
    }

    // Check password strength
    private boolean isStrongPassword(String password) {

        String passwordRegex =
                "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)"
                + "(?=.*[^A-Za-z0-9\\s])[\\x20-\\x7E]{8,72}$";

        return password.matches(passwordRegex);
    }
}
