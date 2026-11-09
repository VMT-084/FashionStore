
package com.fashionstore.controller;

import java.io.IOException;
import java.sql.SQLException;

import com.fashionstore.dao.UserDAO;
import com.fashionstore.dao.impl.UserDAOImpl;
import com.fashionstore.model.User;
import com.fashionstore.util.PasswordUtil;

import com.fashionstore.dao.CartDAO;
import com.fashionstore.dao.CartItemDAO;
import com.fashionstore.dao.impl.CartDAOImpl;
import com.fashionstore.dao.impl.CartItemDAOImpl;
import com.fashionstore.model.Cart;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

public class LoginServlet extends HttpServlet {

    private static final long serialVersionUID = 1L;

    private UserDAO userDAO;
    
    private CartDAO cartDAO;
    
    private CartItemDAO cartItemDAO;

    @Override
    public void init() {
        userDAO = new UserDAOImpl();
        cartDAO = new CartDAOImpl();
        cartItemDAO = new CartItemDAOImpl();
    }

    // Display login page
    @Override
    protected void doGet(
            HttpServletRequest request,
            HttpServletResponse response)
            throws ServletException, IOException {

        request.getRequestDispatcher(
                "/WEB-INF/views/login.jsp"
        ).forward(request, response);
    }

    // Process login
    @Override
    protected void doPost(
            HttpServletRequest request,
            HttpServletResponse response)
            throws ServletException, IOException {

        request.setCharacterEncoding("UTF-8");

        String email = request.getParameter("email");
        String password = request.getParameter("password");

        if (email == null || password == null ||
                email.isBlank() || password.isBlank()) {

            request.setAttribute(
                    "errorMessage",
                    "Please enter your email and password."
            );

            request.getRequestDispatcher(
                    "/WEB-INF/views/login.jsp"
            ).forward(request, response);

            return;
        }

        email = email.trim().toLowerCase();

        try {

            // Find customer using email
            User user = userDAO.getUserByEmail(email);

            // Verify password
            if (user == null ||
                    !PasswordUtil.verifyPassword(
                            password,
                            user.getPassword())) {

                request.setAttribute(
                        "errorMessage",
                        "Invalid email or password."
                );

                request.getRequestDispatcher(
                        "/WEB-INF/views/login.jsp"
                ).forward(request, response);

                return;
            }

            // Create a new session after successful login
            HttpSession oldSession = request.getSession(false);

            if (oldSession != null) {
                oldSession.invalidate();
            }

            HttpSession session = request.getSession(true);

            session.setAttribute("userId", user.getUserId());
            session.setAttribute("userName", user.getFullName());
            session.setAttribute("userEmail", user.getEmail());
            
            Cart cart = cartDAO.getCartByUserId(user.getUserId());

            int cartItemCount = 0;

            if (cart != null) {
                cartItemCount =
                        cartItemDAO.getTotalItemCount(cart.getCartId());
            }

            session.setAttribute("cartItemCount", cartItemCount);

            // Redirect to homepage
            response.sendRedirect(
                    request.getContextPath() + "/home"
            );

        } catch (SQLException e) {

            throw new ServletException(
                    "Error while logging in customer.",
                    e
            );
        }
    }
}
