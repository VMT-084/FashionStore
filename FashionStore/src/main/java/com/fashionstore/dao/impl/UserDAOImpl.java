
package com.fashionstore.dao.impl;

import com.fashionstore.dao.UserDAO;
import com.fashionstore.model.User;
import com.fashionstore.util.DBConnection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class UserDAOImpl implements UserDAO {

    // Register a new customer
    @Override
    public int registerUser(User user) throws SQLException {

        String sql = "INSERT INTO users "
                + "(full_name, email, phone, password, address, "
                + "city, state, pincode) "
                + "VALUES (?, ?, ?, ?, ?, ?, ?, ?)";

        try (Connection connection = DBConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(
                     sql, Statement.RETURN_GENERATED_KEYS)) {

            statement.setString(1, user.getFullName());
            statement.setString(2, user.getEmail());
            statement.setString(3, user.getPhone());
            statement.setString(4, user.getPassword());
            statement.setString(5, user.getAddress());
            statement.setString(6, user.getCity());
            statement.setString(7, user.getState());
            statement.setString(8, user.getPincode());

            int rowsAffected = statement.executeUpdate();

            if (rowsAffected > 0) {
                try (ResultSet resultSet = statement.getGeneratedKeys()) {
                    if (resultSet.next()) {
                        return resultSet.getInt(1);
                    }
                }
            }
        }

        return -1;
    }

    // Check whether an email is already registered
    @Override
    public boolean emailExists(String email) throws SQLException {

        String sql = "SELECT 1 FROM users WHERE email = ?";

        try (Connection connection = DBConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setString(1, email);

            try (ResultSet resultSet = statement.executeQuery()) {
                return resultSet.next();
            }
        }
    }

    // Retrieve a customer using their email
    @Override
    public User getUserByEmail(String email) throws SQLException {

        String sql = "SELECT * FROM users WHERE email = ?";

        try (Connection connection = DBConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setString(1, email);

            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    return mapUser(resultSet);
                }
            }
        }

        return null;
    }

    // Retrieve a customer using their ID
    @Override
    public User getUserById(int userId) throws SQLException {

        String sql = "SELECT * FROM users WHERE user_id = ?";

        try (Connection connection = DBConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setInt(1, userId);

            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    return mapUser(resultSet);
                }
            }
        }

        return null;
    }

    // Update customer profile details
    @Override
    public boolean updateProfile(User user) throws SQLException {

        String sql = "UPDATE users SET full_name = ?, phone = ? "
                + "WHERE user_id = ?";

        try (Connection connection = DBConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setString(1, user.getFullName());
            statement.setString(2, user.getPhone());
            statement.setInt(3, user.getUserId());

            return statement.executeUpdate() > 0;
        }
    }

    // Update customer's saved delivery address
    @Override
    public boolean updateAddress(User user) throws SQLException {

        String sql = "UPDATE users SET address = ?, city = ?, "
                + "state = ?, pincode = ? WHERE user_id = ?";

        try (Connection connection = DBConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setString(1, user.getAddress());
            statement.setString(2, user.getCity());
            statement.setString(3, user.getState());
            statement.setString(4, user.getPincode());
            statement.setInt(5, user.getUserId());

            return statement.executeUpdate() > 0;
        }
    }

    // Update customer's password
    @Override
    public boolean updatePassword(int userId, String passwordHash)
            throws SQLException {

        String sql = "UPDATE users SET password = ? WHERE user_id = ?";

        try (Connection connection = DBConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setString(1, passwordHash);
            statement.setInt(2, userId);

            return statement.executeUpdate() > 0;
        }
    }

    // Convert a database record into a User object
    private User mapUser(ResultSet resultSet) throws SQLException {

        User user = new User();

        user.setUserId(resultSet.getInt("user_id"));
        user.setFullName(resultSet.getString("full_name"));
        user.setEmail(resultSet.getString("email"));
        user.setPhone(resultSet.getString("phone"));
        user.setPassword(resultSet.getString("password"));
        user.setAddress(resultSet.getString("address"));
        user.setCity(resultSet.getString("city"));
        user.setState(resultSet.getString("state"));
        user.setPincode(resultSet.getString("pincode"));

        if (resultSet.getTimestamp("created_at") != null) {
            user.setCreatedAt(
                resultSet.getTimestamp("created_at").toLocalDateTime()
            );
        }

        return user;
    }
}
