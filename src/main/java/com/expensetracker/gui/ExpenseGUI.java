package com.expensetracker.gui;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.GridBagConstraints;
import java.awt.Insets;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;
import javax.swing.table.DefaultTableModel;

import com.expensetracker.dao.CategoryDAO;
import com.expensetracker.dao.ExpenseDAO;
import com.expensetracker.model.Category;
import com.expensetracker.model.Expenses;
import javax.swing.SwingUtilities


public class ExpenseGUI extends JFrame {

    private ExpenseDAO expensesDAO;
    private CategoryDAO categoryDAO;

    private JTextField idField;
    private JTable expensesTable;
    private DefaultTableModel tableModel;
    private JTextField descriptionField;
    private JTextField amountField;
    private JComboBox<Category> categoryComboBox;
    private JTextField dateField;
    private JComboBox<String> typeComboBox;

    // No-argument constructor
    public ExpenseGUI() {
        setTitle("EXPENSE TRACKER");
        setSize(900, 700);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);

        expensesDAO = new ExpenseDAO();
        categoryDAO = new CategoryDAO();

        buildGUI();
    }

    // Constructor with parent JFrame
    public ExpenseGUI(JFrame parent) {
        this(); // call no-arg constructor
    }

    private void buildGUI() {
        // Top panel for inputs
        JPanel formPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 10));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(8, 8, 8, 8);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        idField = new JTextField();
        idField.setEditable(false);

        descriptionField = new JTextField(7);
        amountField = new JTextField(7);
        categoryComboBox = new JComboBox<>();
        dateField = new JTextField(LocalDate.now().toString(), 8);
        typeComboBox = new JComboBox<>(new String[]{"Expense", "Income"});
        typeComboBox.setSelectedIndex(0);

        // Row 1 - Description
        gbc.gridx = 0; gbc.gridy = 1; formPanel.add(new JLabel("Description:"), gbc);
        gbc.gridx = 1; formPanel.add(descriptionField, gbc);

        // Row 2 - Category
        gbc.gridx = 0; gbc.gridy = 2; formPanel.add(new JLabel("Category:"), gbc);
        gbc.gridx = 1; formPanel.add(categoryComboBox, gbc);

        // Row 3 - Date
        gbc.gridx = 0; gbc.gridy = 3; formPanel.add(new JLabel("Date (yyyy-mm-dd):"), gbc);
        gbc.gridx = 1; formPanel.add(dateField, gbc);

        // Row 4 - Amount
        gbc.gridx = 0; gbc.gridy = 4; formPanel.add(new JLabel("Amount:"), gbc);
        gbc.gridx = 1; formPanel.add(amountField, gbc);

        // Row 5 - Type
        gbc.gridx = 0; gbc.gridy = 5; formPanel.add(new JLabel("Type:"), gbc);
        gbc.gridx = 1; formPanel.add(typeComboBox, gbc);

        add(formPanel, BorderLayout.NORTH);

        // Table
        tableModel = new DefaultTableModel(new Object[]{"ID", "Description", "Date", "Category", "Amount", "Type"}, 0);
        expensesTable = new JTable(tableModel);
        expensesTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        add(new JScrollPane(expensesTable), BorderLayout.CENTER);

        loadCategories();
        loadExpenses();

        // Table selection listener
        expensesTable.getSelectionModel().addListSelectionListener(event -> {
            if (!event.getValueIsAdjusting() && expensesTable.getSelectedRow() != -1) {
                int selectedRow = expensesTable.getSelectedRow();
                int id = (int) tableModel.getValueAt(selectedRow, 0);
                String description = (String) tableModel.getValueAt(selectedRow, 1);
                String date = tableModel.getValueAt(selectedRow, 2).toString();
                String categoryName = (String) tableModel.getValueAt(selectedRow, 3);
                double amount = (double) tableModel.getValueAt(selectedRow, 4);
                String type = tableModel.getValueAt(selectedRow, 5).toString();

                idField.setText(String.valueOf(id));
                descriptionField.setText(description);
                dateField.setText(date);
                amountField.setText(String.valueOf(amount));
                typeComboBox.setSelectedItem(type.trim());

                for (int i = 0; i < categoryComboBox.getItemCount(); i++) {
                    Category c = categoryComboBox.getItemAt(i);
                    if (c.getName().equals(categoryName)) {
                        categoryComboBox.setSelectedIndex(i);
                        break;
                    }
                }
            }
        });

        // Buttons
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 10));
        JButton addButton = new JButton("Add");
        JButton editButton = new JButton("Edit");
        JButton deleteButton = new JButton("Delete");
        JButton filterExpenseBtn = new JButton("Show Expenses");
        JButton filterIncomeBtn = new JButton("Show Income");
        JButton showAllBtn = new JButton("Show All");

        buttonPanel.add(filterExpenseBtn);
        buttonPanel.add(filterIncomeBtn);
        buttonPanel.add(showAllBtn);
        buttonPanel.add(addButton);
        buttonPanel.add(editButton);
        buttonPanel.add(deleteButton);
        add(buttonPanel, BorderLayout.SOUTH);

        // Button actions
        addButton.addActionListener(e -> addExpense());
        editButton.addActionListener(e -> editExpense());
        deleteButton.addActionListener(e -> deleteExpense());
        filterExpenseBtn.addActionListener(e -> loadExpensesByType("Expense"));
        filterIncomeBtn.addActionListener(e -> loadExpensesByType("Income"));
        showAllBtn.addActionListener(e -> loadExpenses());
    }

    private void loadExpensesByType(String type) {
        try {
            tableModel.setRowCount(0);
            List<Expenses> expenses = expensesDAO.getExpensesByType(type);
            for (Expenses e : expenses) {
                tableModel.addRow(new Object[]{
                        e.getId(),
                        e.getDescription(),
                        e.getDate().toLocalDate(),
                        e.getCategory().getName(),
                        e.getAmount(),
                        e.getType()
                });
            }
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this, "Error loading expenses: " + ex.getMessage());
        }
    }

    private void loadCategories() {
        try {
            categoryComboBox.removeAllItems();
            List<Category> categories = categoryDAO.getAllCategories();
            for (Category c : categories) {
                categoryComboBox.addItem(c);
            }
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this, "Error loading categories: " + ex.getMessage());
        }
    }

    private void loadExpenses() {
        try {
            tableModel.setRowCount(0);
            List<Expenses> expenses = expensesDAO.getAllExpenses();
            for (Expenses e : expenses) {
                tableModel.addRow(new Object[]{
                        e.getId(),
                        e.getDescription(),
                        e.getDate().toLocalDate(),
                        e.getCategory().getName(),
                        e.getAmount(),
                        e.getType()
                });
            }
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this, "Error loading expenses: " + ex.getMessage());
        }
    }

    private void addExpense() {
        try {
            String description = descriptionField.getText().trim();
            double amount = Double.parseDouble(amountField.getText().trim());
            Category category = (Category) categoryComboBox.getSelectedItem();
            LocalDate date = LocalDate.parse(dateField.getText().trim());
            String type = (String) typeComboBox.getSelectedItem();

            if (category == null) {
                JOptionPane.showMessageDialog(this, "Please select a category.");
                return;
            }

            Expenses expense = new Expenses();
            expense.setDescription(description);
            expense.setAmount(amount);
            expense.setCategory(category);
            expense.setDate(date.atStartOfDay());
            expense.setType(type);
            expensesDAO.addExpense(expense);
            loadExpenses();
            clearFields();
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Error adding expense: " + ex.getMessage());
        }
    }

    private void editExpense() {
        int selectedRow = expensesTable.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Select an expense to edit.");
            return;
        }
        try {
            int id = (int) tableModel.getValueAt(selectedRow, 0);
            String description = descriptionField.getText().trim();
            double amount = Double.parseDouble(amountField.getText().trim());
            Category category = (Category) categoryComboBox.getSelectedItem();
            LocalDate date = LocalDate.parse(dateField.getText().trim());
            String type = (String) typeComboBox.getSelectedItem();

            Expenses expense = new Expenses();
            expense.setId(id);
            expense.setDescription(description);
            expense.setAmount(amount);
            expense.setCategory(category);
            expense.setDate(date.atStartOfDay());
            expense.setType(type);

            expensesDAO.updateExpense(expense);
            loadExpenses();
            clearFields();
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Error updating expense: " + ex.getMessage());
        }
    }

    private void deleteExpense() {
        int selectedRow = expensesTable.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Select an expense to delete.");
            return;
        }
        try {
            int id = (int) tableModel.getValueAt(selectedRow, 0);
            expensesDAO.deleteExpense(id);
            loadExpenses();
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this, "Error deleting expense: " + ex.getMessage());
        }
        clearFields();
    }
    private void clearFields() {
        idField.setText("");
        descriptionField.setText("");
        amountField.setText("");
        dateField.setText(LocalDate.now().toString());
        if (categoryComboBox.getItemCount() > 0) {
            categoryComboBox.setSelectedIndex(0);
        }
        typeComboBox.setSelectedIndex(0);
        expensesTable.clearSelection();
    }
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            ExpenseGUI gui = new ExpenseGUI();
            gui.setVisible(true);
        });
    }
}
