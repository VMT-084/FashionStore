
package com.fashionstore.dao;

import com.fashionstore.model.User;
import java.sql.SQLException;

public interface UserDAO {

    // Register a new customer
    int registerUser(User user) throws SQLException;

    // Check whether an email is already registered
    boolean emailExists(String email) throws SQLException;

    // Retrieve a customer using their email
    // Used during login and forgot-password workflows
    User getUserByEmail(String email) throws SQLException;

    // Retrieve a customer using their ID
    User getUserById(int userId) throws SQLException;

    // Update customer profile details
    boolean updateProfile(User user) throws SQLException;

    // Update customer's saved delivery address
    boolean updateAddress(User user) throws SQLException;

    // Update customer's password
    boolean updatePassword(int userId, String passwordHash)
            throws SQLException;
}
