
package com.fashionstore.controller;

import java.io.IOException;
import java.sql.SQLException;

import com.fashionstore.dao.UserDAO;
import com.fashionstore.dao.impl.UserDAOImpl;
import com.fashionstore.model.User;
import com.fashionstore.util.PasswordUtil;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

public class ChangePasswordServlet extends HttpServlet {

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

        HttpSession session = request.getSession(false);

        if (session == null ||
                session.getAttribute("userId") == null) {

            response.sendRedirect(
                    request.getContextPath() + "/login"
            );

            return;
        }

        request.getRequestDispatcher(
                "/WEB-INF/views/change-password.jsp"
        ).forward(request, response);
    }

    @Override
    protected void doPost(
            HttpServletRequest request,
            HttpServletResponse response)
            throws ServletException, IOException {

        request.setCharacterEncoding("UTF-8");

        HttpSession session = request.getSession(false);

        if (session == null ||
                session.getAttribute("userId") == null) {

            response.sendRedirect(
                    request.getContextPath() + "/login"
            );

            return;
        }

        int userId = (Integer) session.getAttribute("userId");

        String currentPassword =
                request.getParameter("currentPassword");

        String newPassword =
                request.getParameter("newPassword");

        String confirmPassword =
                request.getParameter("confirmPassword");

        if (currentPassword == null ||
                newPassword == null ||
                confirmPassword == null ||
                currentPassword.isBlank() ||
                newPassword.isBlank() ||
                confirmPassword.isBlank()) {

            request.setAttribute(
                    "errorMessage",
                    "Please fill in all password fields."
            );

            request.getRequestDispatcher(
                    "/WEB-INF/views/change-password.jsp"
            ).forward(request, response);

            return;
        }

        // Validate new password strength
        if (!isStrongPassword(newPassword)) {

            request.setAttribute(
                    "errorMessage",
                    "New password must contain 8 to 72 characters, "
                    + "including at least one uppercase letter, "
                    + "one lowercase letter, one digit, "
                    + "and one special character."
            );

            request.getRequestDispatcher(
                    "/WEB-INF/views/change-password.jsp"
            ).forward(request, response);

            return;
        }

        if (!newPassword.equals(confirmPassword)) {

            request.setAttribute(
                    "errorMessage",
                    "New passwords do not match."
            );

            request.getRequestDispatcher(
                    "/WEB-INF/views/change-password.jsp"
            ).forward(request, response);

            return;
        }

        if (currentPassword.equals(newPassword)) {

            request.setAttribute(
                    "errorMessage",
                    "New password must be different from current password."
            );

            request.getRequestDispatcher(
                    "/WEB-INF/views/change-password.jsp"
            ).forward(request, response);

            return;
        }

        try {

            User user = userDAO.getUserById(userId);

            if (user == null ||
                    !PasswordUtil.verifyPassword(
                            currentPassword,
                            user.getPassword())) {

                request.setAttribute(
                        "errorMessage",
                        "Current password is incorrect."
                );

                request.getRequestDispatcher(
                        "/WEB-INF/views/change-password.jsp"
                ).forward(request, response);

                return;
            }

            String newPasswordHash =
                    PasswordUtil.hashPassword(newPassword);

            boolean updated = userDAO.updatePassword(
                    userId,
                    newPasswordHash
            );

            if (updated) {

                session.invalidate();

                HttpSession newSession =
                        request.getSession(true);

                newSession.setAttribute(
                        "successMessage",
                        "Password changed successfully. Please log in again."
                );

                response.sendRedirect(
                        request.getContextPath() + "/login"
                );

            } else {

                request.setAttribute(
                        "errorMessage",
                        "Unable to update password. Please try again."
                );

                request.getRequestDispatcher(
                        "/WEB-INF/views/change-password.jsp"
                ).forward(request, response);
            }

        } catch (SQLException e) {

            throw new ServletException(
                    "Error while changing customer password.",
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
