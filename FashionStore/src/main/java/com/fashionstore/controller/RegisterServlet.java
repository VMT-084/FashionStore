
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

public class RegisterServlet extends HttpServlet {

    private static final long serialVersionUID = 1L;

    private UserDAO userDAO;

    @Override
    public void init() {
        userDAO = new UserDAOImpl();
    }

    // Display registration page
    @Override
    protected void doGet(
            HttpServletRequest request,
            HttpServletResponse response)
            throws ServletException, IOException {

        request.getRequestDispatcher(
                "/WEB-INF/views/register.jsp"
        ).forward(request, response);
    }

    // Process registration form
    @Override
    protected void doPost(
            HttpServletRequest request,
            HttpServletResponse response)
            throws ServletException, IOException {

        request.setCharacterEncoding("UTF-8");

        String fullName = request.getParameter("fullName");
        String email = request.getParameter("email");
        String phone = request.getParameter("phone");
        String password = request.getParameter("password");
        String confirmPassword =
                request.getParameter("confirmPassword");

        // Validate required fields
        if (fullName == null || email == null ||
                phone == null || password == null ||
                confirmPassword == null ||
                fullName.isBlank() || email.isBlank() ||
                phone.isBlank() || password.isBlank() ||
                confirmPassword.isBlank()) {

            request.setAttribute(
                    "errorMessage",
                    "Please fill in all required fields."
            );

            request.getRequestDispatcher(
                    "/WEB-INF/views/register.jsp"
            ).forward(request, response);

            return;
        }

        fullName = fullName.trim();
        email = email.trim().toLowerCase();
        phone = phone.trim();

        // Validate name
        if (fullName.length() > 100) {
            request.setAttribute(
                    "errorMessage",
                    "Name must not exceed 100 characters."
            );

            request.getRequestDispatcher(
                    "/WEB-INF/views/register.jsp"
            ).forward(request, response);

            return;
        }

        // Validate email
        if (!email.matches(
                "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$")) {

            request.setAttribute(
                    "errorMessage",
                    "Please enter a valid email address."
            );

            request.getRequestDispatcher(
                    "/WEB-INF/views/register.jsp"
            ).forward(request, response);

            return;
        }

        // Validate phone number
        if (!phone.matches("^[0-9]{10}$")) {
            request.setAttribute(
                    "errorMessage",
                    "Please enter a valid 10-digit phone number."
            );

            request.getRequestDispatcher(
                    "/WEB-INF/views/register.jsp"
            ).forward(request, response);

            return;
        }

        // Validate password strength
        if (!isStrongPassword(password)) {
            request.setAttribute(
                    "errorMessage",
                    "Password must contain 8 to 72 characters, "
                    + "including at least one uppercase letter, "
                    + "one lowercase letter, one digit, "
                    + "and one special character."
            );

            request.getRequestDispatcher(
                    "/WEB-INF/views/register.jsp"
            ).forward(request, response);

            return;
        }

        if (!password.equals(confirmPassword)) {
            request.setAttribute(
                    "errorMessage",
                    "Passwords do not match."
            );

            request.getRequestDispatcher(
                    "/WEB-INF/views/register.jsp"
            ).forward(request, response);

            return;
        }

        try {

            // Check whether email is already registered
            if (userDAO.emailExists(email)) {
                request.setAttribute(
                        "errorMessage",
                        "This email is already registered."
                );

                request.getRequestDispatcher(
                        "/WEB-INF/views/register.jsp"
                ).forward(request, response);

                return;
            }

            // Create customer
            User user = new User();

            user.setFullName(fullName);
            user.setEmail(email);
            user.setPhone(phone);

            // Hash password before storing it
            user.setPassword(
                    PasswordUtil.hashPassword(password)
            );

            int userId = userDAO.registerUser(user);

            if (userId > 0) {

                request.getSession().setAttribute(
                        "successMessage",
                        "Registration successful! Please log in."
                );

                response.sendRedirect(
                        request.getContextPath() + "/login"
                );

            } else {

                request.setAttribute(
                        "errorMessage",
                        "Registration failed. Please try again."
                );

                request.getRequestDispatcher(
                        "/WEB-INF/views/register.jsp"
                ).forward(request, response);
            }

        } catch (SQLException e) {

            throw new ServletException(
                    "Error while registering customer.",
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
