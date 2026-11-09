
package com.fashionstore.dao.impl;

import com.fashionstore.dao.CategoryDAO;
import com.fashionstore.model.Category;
import com.fashionstore.util.DBConnection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class CategoryDAOImpl implements CategoryDAO {

    // Retrieve all categories
    @Override
    public List<Category> getAllCategories() throws SQLException {

        List<Category> categories = new ArrayList<>();

        String sql = "SELECT * FROM categories ORDER BY category_name";

        try (Connection connection = DBConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql);
             ResultSet resultSet = statement.executeQuery()) {

            while (resultSet.next()) {
                categories.add(mapCategory(resultSet));
            }
        }

        return categories;
    }

    // Retrieve a category using its ID
    @Override
    public Category getCategoryById(int categoryId) throws SQLException {

        String sql = "SELECT * FROM categories WHERE category_id = ?";

        try (Connection connection = DBConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setInt(1, categoryId);

            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    return mapCategory(resultSet);
                }
            }
        }

        return null;
    }

    // Retrieve a category using its name
    @Override
    public Category getCategoryByName(String categoryName)
            throws SQLException {

        String sql = "SELECT * FROM categories WHERE category_name = ?";

        try (Connection connection = DBConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setString(1, categoryName);

            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    return mapCategory(resultSet);
                }
            }
        }

        return null;
    }

    // Add a new category
    @Override
    public boolean addCategory(Category category) throws SQLException {

        String sql = "INSERT INTO categories (category_name, description) "
                   + "VALUES (?, ?)";

        try (Connection connection = DBConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setString(1, category.getCategoryName());
            statement.setString(2, category.getDescription());

            return statement.executeUpdate() > 0;
        }
    }

    // Update an existing category
    @Override
    public boolean updateCategory(Category category) throws SQLException {

        String sql = "UPDATE categories SET category_name = ?, "
                   + "description = ? WHERE category_id = ?";

        try (Connection connection = DBConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setString(1, category.getCategoryName());
            statement.setString(2, category.getDescription());
            statement.setInt(3, category.getCategoryId());

            return statement.executeUpdate() > 0;
        }
    }

    // Delete a category using its ID
    @Override
    public boolean deleteCategory(int categoryId) throws SQLException {

        String sql = "DELETE FROM categories WHERE category_id = ?";

        try (Connection connection = DBConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setInt(1, categoryId);

            return statement.executeUpdate() > 0;
        }
    }

    // Check whether a category name already exists
    @Override
    public boolean categoryExists(String categoryName)
            throws SQLException {

        String sql = "SELECT 1 FROM categories WHERE category_name = ?";

        try (Connection connection = DBConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setString(1, categoryName);

            try (ResultSet resultSet = statement.executeQuery()) {
                return resultSet.next();
            }
        }
    }

    // Convert a database record into a Category object
    private Category mapCategory(ResultSet resultSet) throws SQLException {

        Category category = new Category();

        category.setCategoryId(resultSet.getInt("category_id"));
        category.setCategoryName(resultSet.getString("category_name"));
        category.setDescription(resultSet.getString("description"));

        return category;
    }
}
