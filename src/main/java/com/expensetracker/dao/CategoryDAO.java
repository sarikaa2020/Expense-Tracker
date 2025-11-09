// Minor update: Added comment for GitHub contribution - 09 Nov 2025

package com.expensetracker.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import com.expensetracker.model.Category;
import com.expensetracker.util.DatabaseConnection;



public class CategoryDAO {

    private static final String INSERT_CATEGORY = "INSERT INTO category(cname) VALUES (?)";
    private static final String SELECT_ALL_CATEGORIES = "SELECT * FROM category";        
    private static final String EDIT_CATEGORY = "UPDATE category SET cname=? WHERE id=?";
    private static final String DELETE_CATEGORY = "DELETE FROM category WHERE id=?";


    public void addCategory(String name) throws SQLException {
        try (Connection conn = DatabaseConnection.getDBConnection();
             PreparedStatement stmt = conn.prepareStatement(INSERT_CATEGORY)) {
            stmt.setString(1, name);
            stmt.executeUpdate();
        }
    }

    public List<Category> getAllCategories() throws SQLException {
        List<Category> categories = new ArrayList<>();
        try (Connection conn = DatabaseConnection.getDBConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(SELECT_ALL_CATEGORIES)) {
            
            while (rs.next()) {
                Category c = new Category(
                        rs.getInt("id"),
                        rs.getString("cname")
                );
                categories.add(c);
            }
        }
        return categories;
    }

    public void updateCategory(int id, String newName) throws SQLException {
        try (Connection conn = DatabaseConnection.getDBConnection();
             PreparedStatement stmt = conn.prepareStatement(EDIT_CATEGORY)) {
            stmt.setString(1, newName);
            stmt.setInt(2, id);
            stmt.executeUpdate();
        }
    }

    public void deleteCategory(int id) throws SQLException {
        try (Connection conn = DatabaseConnection.getDBConnection();
             PreparedStatement stmt = conn.prepareStatement(DELETE_CATEGORY)) {
            stmt.setInt(1, id);
            stmt.executeUpdate();
        }
    }
}