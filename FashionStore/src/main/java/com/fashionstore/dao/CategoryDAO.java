
package com.fashionstore.dao;

import com.fashionstore.model.Category;
import java.sql.SQLException;
import java.util.List;

public interface CategoryDAO {

    // Retrieve all categories
    List<Category> getAllCategories() throws SQLException;

    // Retrieve a category using its ID
    Category getCategoryById(int categoryId) throws SQLException;

    // Retrieve a category using its name
    Category getCategoryByName(String categoryName) throws SQLException;

    // Add a new category
    boolean addCategory(Category category) throws SQLException;

    // Update an existing category
    boolean updateCategory(Category category) throws SQLException;

    // Delete a category using its ID
    boolean deleteCategory(int categoryId) throws SQLException;

    // Check whether a category name already exists
    boolean categoryExists(String categoryName) throws SQLException;
}
