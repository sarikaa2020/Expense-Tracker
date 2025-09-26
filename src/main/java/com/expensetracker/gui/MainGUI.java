package com.expensetracker.gui;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;

import com.expensetracker.dao.CategoryDAO;
import com.expensetracker.dao.ExpenseDAO;

public class MainGUI extends JFrame {

    private JButton categoryButton;
    private JButton expensesButton;
    private JPanel panel;
    private GridBagConstraints gbc;

    private CategoryDAO categoryDAO;
    private ExpenseDAO expensesDAO;

    public MainGUI() {
        initializeComponents();
        setupLayout();
        setupEventListeners();
    }

    private void initializeComponents() {
        setTitle("Expense Tracker");
        setSize(800, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        panel = new JPanel(new GridBagLayout());
        gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 0, 10, 0);
        gbc.gridx = 0;

        categoryButton = new JButton("Categoriess");
        expensesButton = new JButton("Expensess");
    }

    private void setupLayout() {
        gbc.gridy = 0;
        panel.add(categoryButton, gbc);
        expensesButton.setPreferredSize(new Dimension(300,100));
        categoryButton.setPreferredSize(new Dimension(300,100));
        

        gbc.gridy = 1;
        panel.add(expensesButton, gbc);

        add(panel, BorderLayout.CENTER);
    }

    private void setupEventListeners() {
        categoryButton.addActionListener(e -> {
            CategoryGUI categoryGUI =new CategoryGUI(this);
            categoryGUI.setVisible(true);
        });

        expensesButton.addActionListener(e -> {
            ExpenseGUI expensesGUI =new ExpenseGUI(this);
            expensesGUI.setVisible(true);
        });
    }

    

    public static void main(String[] args) {
        try {
        SwingUtilities.invokeLater(() -> {
            MainGUI gui = new MainGUI();
            gui.setVisible(true);
        });
        } 
        catch (Exception e) {
            e.printStackTrace(); 
        }
    
}
}


    

    


    