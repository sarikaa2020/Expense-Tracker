package com.expensetracker.gui;

import com.expensetracker.dao.CategoryDAO;
import com.expensetracker.model.Category;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.sql.SQLException;
import java.util.List;

public class CategoryGUI extends JFrame {

    private CategoryDAO categoryDAO;
    private JTable table;
    private DefaultTableModel tableModel;
    private JTextField nameField;

    // No-argument constructor
    public CategoryGUI() {
        this(null); // calls the constructor with JFrame parameter
    }

    // Constructor with JFrame parent
    public CategoryGUI(JFrame parent) {
        categoryDAO = new CategoryDAO();

        setTitle("Categoriess");
        setSize(800, 600);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(parent);

        // --- Top Panel (input + buttons) ---
        JPanel panel = new JPanel(new FlowLayout());
        panel.add(new JLabel("Category Name:"));
        nameField = new JTextField(20);
        panel.add(nameField);

        JButton addButton = new JButton("Add Category");
        JButton editButton = new JButton("Edit Category");
        JButton deleteButton = new JButton("Delete Category");

        panel.add(addButton);
        panel.add(editButton);
        panel.add(deleteButton);

        // --- Table ---
        tableModel = new DefaultTableModel(new Object[]{"ID", "Name"}, 0);
        table = new JTable(tableModel);
        table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        table.getSelectionModel().addListSelectionListener(event -> {
            if (!event.getValueIsAdjusting() && table.getSelectedRow() != -1) {
                int selectedRow = table.getSelectedRow();
                int id = (int) tableModel.getValueAt(selectedRow, 0);
                String name = (String) tableModel.getValueAt(selectedRow, 1);
                nameField.setText(name);
            }
        });

        JScrollPane scrollPane = new JScrollPane(table);

        // --- Add components to frame ---
        add(panel, BorderLayout.NORTH);
        add(scrollPane, BorderLayout.CENTER);

        // --- Button Actions ---
        addButton.addActionListener(e -> addCategory());
        editButton.addActionListener(e -> editCategory());
        deleteButton.addActionListener(e -> deleteCategory());

        // Load data initially
        loadCategories();
    }

    // --- Methods ---
    private void loadCategories() {
        tableModel.setRowCount(0);
        try {
            List<Category> categories = categoryDAO.getAllCategories();
            for (Category c : categories) {
                tableModel.addRow(new Object[]{c.getId(), c.getName()});
            }
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this, "Error loading categories: " + ex.getMessage(),
                    "Database Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void addCategory() {
        String name = nameField.getText().trim();
        if (name.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Category name cannot be empty.", "Input Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
        try {
            categoryDAO.addCategory(name);
            nameField.setText("");
            loadCategories();
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this, "Error adding category: " + ex.getMessage(),
                    "Database Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void editCategory() {
        int selectedRow = table.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Please select a category to edit.", "Selection Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
        String newName = nameField.getText().trim();
        if (newName.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Category name cannot be empty.", "Input Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
        int categoryId = (int) tableModel.getValueAt(selectedRow, 0);
        try {
            categoryDAO.updateCategory(categoryId, newName);
            nameField.setText("");
            loadCategories();
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this, "Error updating category: " + ex.getMessage(),
                    "Database Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void deleteCategory() {
        int selectedRow = table.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Please select a category to delete.", "Selection Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
        int categoryId = (int) tableModel.getValueAt(selectedRow, 0);
        int confirm = JOptionPane.showConfirmDialog(this,
                "Are you sure you want to delete this category?",
                "Confirm Delete",
                JOptionPane.YES_NO_OPTION);

        if (confirm == JOptionPane.YES_OPTION) {
            try {
                categoryDAO.deleteCategory(categoryId);
                loadCategories();
            } catch (SQLException ex) {
                JOptionPane.showMessageDialog(this, "Error deleting category: " + ex.getMessage(),
                        "Database Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }
}
