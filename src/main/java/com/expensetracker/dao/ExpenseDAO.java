package com.expensetracker.dao;

import com.expensetracker.model.Category;
import com.expensetracker.model.Expenses;
import com.expensetracker.util.DatabaseConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ExpenseDAO {

    private static final String INSERT_EXPENSE = "INSERT INTO expenses (description, edate, cid, amt, type) VALUES (?, ?, ?, ?, ?)";
    private static final String SELECT_ALL_EXPENSES = 
        "SELECT e.eid, e.description, e.edate, e.amt, e.type, c.id AS cid, c.cname " +
        "FROM expenses e JOIN category c ON e.cid = c.id";
    private static final String UPDATE_EXPENSE = "UPDATE expenses SET description=?, edate=?, cid=?, amt=?, type=? WHERE eid=?";
    private static final String DELETE_EXPENSE = "DELETE FROM expenses WHERE eid=?";

    // Add expense
    public void addExpense(Expenses expense) throws SQLException {
        try (Connection conn = DatabaseConnection.getDBConnection();
             PreparedStatement stmt = conn.prepareStatement(INSERT_EXPENSE)) {

            stmt.setString(1, expense.getDescription());
            stmt.setTimestamp(2, Timestamp.valueOf(expense.getDate()));
            stmt.setInt(3, expense.getCategory().getId());
            stmt.setDouble(4, expense.getAmount());
            stmt.setString(5, expense.getType());
            stmt.executeUpdate();
        }
    }

    // Get all expenses
    public List<Expenses> getAllExpenses() throws SQLException {
        List<Expenses> list = new ArrayList<>();
        try (Connection conn = DatabaseConnection.getDBConnection();
             PreparedStatement pst = conn.prepareStatement(SELECT_ALL_EXPENSES);
             ResultSet rs = pst.executeQuery()) {

            while (rs.next()) {
                Expenses e = new Expenses();
                e.setId(rs.getInt("eid"));
                e.setDescription(rs.getString("description"));
                e.setAmount(rs.getDouble("amt"));
                e.setDate(rs.getDate("edate").toLocalDate().atStartOfDay());
                e.setType(rs.getString("type"));

                // Create Category object
                int cid = rs.getInt("cid");
                String cname = rs.getString("cname");
                Category c = new Category(cid, cname); 
                e.setCategory(c);

                list.add(e);
            }
        }
        return list;
    }

    // Update expense
    public void updateExpense(Expenses expense) throws SQLException {
        try (Connection conn = DatabaseConnection.getDBConnection();
             PreparedStatement stmt = conn.prepareStatement(UPDATE_EXPENSE)) {

            stmt.setString(1, expense.getDescription());
            stmt.setTimestamp(2, Timestamp.valueOf(expense.getDate()));
            stmt.setInt(3, expense.getCategory().getId());
            stmt.setDouble(4, expense.getAmount());
            stmt.setString(5, expense.getType());
            stmt.setInt(6, expense.getId());
            stmt.executeUpdate();


        }
    }
    public List<Expenses> getExpensesByType(String type) throws SQLException {
    List<Expenses> list = new ArrayList<>();
    String sql = "SELECT e.eid, e.description, e.edate, e.amt, e.type, c.id, c.cname " +
                 "FROM expenses e JOIN category c ON e.cid = c.id WHERE e.type = ?";
    try (Connection conn = DatabaseConnection.getDBConnection();
         PreparedStatement pst = conn.prepareStatement(sql)) {
        pst.setString(1, type);
        try (ResultSet rs = pst.executeQuery()) {
            while (rs.next()) {
                Expenses e = new Expenses();
                e.setId(rs.getInt("eid"));
                e.setDescription(rs.getString("description"));
                e.setAmount(rs.getDouble("amt"));
                e.setDate(rs.getDate("edate").toLocalDate().atStartOfDay());
                e.setType(rs.getString("type"));
                Category c = new Category(rs.getInt("id"), rs.getString("cname"));
                e.setCategory(c);
                list.add(e);
            }
        }
    }
    return list;
}


    // Delete expense
    public void deleteExpense(int id) throws SQLException {
        try (Connection conn = DatabaseConnection.getDBConnection();
             PreparedStatement stmt = conn.prepareStatement(DELETE_EXPENSE)) {

            stmt.setInt(1, id);
            stmt.executeUpdate();
        }
    }
}