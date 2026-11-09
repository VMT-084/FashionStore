
package com.fashionstore.controller;

import java.io.IOException;
import java.sql.SQLException;

import com.fashionstore.dao.UserDAO;
import com.fashionstore.dao.impl.UserDAOImpl;
import com.fashionstore.model.User;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

public class ProfileServlet extends HttpServlet {

    private static final long serialVersionUID = 1L;

    private UserDAO userDAO;

    @Override
    public void init() throws ServletException {
        userDAO = new UserDAOImpl();
    }

    // Display customer profile
    @Override
    protected void doGet(HttpServletRequest request,
                         HttpServletResponse response)
            throws ServletException, IOException {

        HttpSession session = request.getSession(false);

        if (session == null
                || session.getAttribute("userId") == null) {

            response.sendRedirect(
                request.getContextPath() + "/login"
            );
            return;
        }

        try {

            int userId = Integer.parseInt(
                session.getAttribute("userId").toString()
            );

            User user = userDAO.getUserById(userId);

            if (user == null) {
                session.invalidate();

                response.sendRedirect(
                    request.getContextPath() + "/login"
                );
                return;
            }

            request.setAttribute("user", user);

            request.getRequestDispatcher(
                "/WEB-INF/views/profile.jsp"
            ).forward(request, response);

        } catch (NumberFormatException e) {

            session.invalidate();

            response.sendRedirect(
                request.getContextPath() + "/login"
            );

        } catch (SQLException e) {

            throw new ServletException(
                "Unable to load customer profile.", e
            );
        }
    }

    // Update personal information or saved address
    @Override
    protected void doPost(HttpServletRequest request,
                          HttpServletResponse response)
            throws ServletException, IOException {

        HttpSession session = request.getSession(false);

        if (session == null
                || session.getAttribute("userId") == null) {

            response.sendRedirect(
                request.getContextPath() + "/login"
            );
            return;
        }

        request.setCharacterEncoding("UTF-8");

        try {

            int userId = Integer.parseInt(
                session.getAttribute("userId").toString()
            );

            User user = userDAO.getUserById(userId);

            if (user == null) {
                session.invalidate();

                response.sendRedirect(
                    request.getContextPath() + "/login"
                );
                return;
            }

            String action = request.getParameter("action");

            boolean updated = false;

            if ("personal".equals(action)) {

                String fullName =
                    request.getParameter("fullName");

                String phone =
                    request.getParameter("phone");

                if (isBlank(fullName)
                        || fullName.trim().length() > 100
                        || isBlank(phone)
                        || !phone.trim().matches("[0-9]{10}")) {

                    setMessage(session,
                        "Please enter a valid name and 10-digit phone number.");

                    redirectToProfile(request, response);
                    return;
                }

                user.setFullName(fullName.trim());
                user.setPhone(phone.trim());

                updated = userDAO.updateProfile(user);

            } else if ("address".equals(action)) {

                String address =
                    request.getParameter("address");

                String city =
                    request.getParameter("city");

                String state =
                    request.getParameter("state");

                String pincode =
                    request.getParameter("pincode");

                if (isBlank(address)
                        || isBlank(city)
                        || isBlank(state)
                        || isBlank(pincode)
                        || address.trim().length() > 500
                        || city.trim().length() > 100
                        || state.trim().length() > 100
                        || !pincode.trim().matches("[0-9]{6}")) {

                    setMessage(session,
                        "Please enter valid delivery address details.");

                    redirectToProfile(request, response);
                    return;
                }

                user.setAddress(address.trim());
                user.setCity(city.trim());
                user.setState(state.trim());
                user.setPincode(pincode.trim());

                updated = userDAO.updateAddress(user);

            } else {

                setMessage(session, "Invalid profile action.");

                redirectToProfile(request, response);
                return;
            }

            if (updated) {

                setMessage(session,
                    "Your profile has been updated successfully.");

            } else {

                setMessage(session,
                    "Unable to update your profile. Please try again.");
            }

            redirectToProfile(request, response);

        } catch (NumberFormatException e) {

            session.invalidate();

            response.sendRedirect(
                request.getContextPath() + "/login"
            );

        } catch (SQLException e) {

            throw new ServletException(
                "Unable to update customer profile.", e
            );
        }
    }

    private boolean isBlank(String value) {
        return value == null || value.trim().isEmpty();
    }

    private void setMessage(HttpSession session, String message) {
        session.setAttribute("profileMessage", message);
    }

    private void redirectToProfile(
            HttpServletRequest request,
            HttpServletResponse response) throws IOException {

        response.sendRedirect(
            request.getContextPath() + "/profile"
        );
    }
}
