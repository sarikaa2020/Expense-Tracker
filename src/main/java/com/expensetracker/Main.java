package com.expensetracker;
import java.sql.Connection;
import java.awt.FlowLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.SwingUtilities;
import java.awt.FlowLayout;
import java.awt.Dimension;



import com.expensetracker.gui.CategoryGUI;
import com.expensetracker.gui.ExpenseGUI;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;



import com.expensetracker.util.DatabaseConnection;

public class Main{

    public static void main(String[] args) {
        DatabaseConnection dbConnection = new DatabaseConnection();
        try {
            Connection cn = dbConnection.getDBConnection();
            System.out.println("CONNECTED SUCCESSFULLY");
        }
        catch (Exception e) {
            System.out.println("CONNECTION FAILED");
            System.exit(1);
        }
        try{
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        }
        catch(ClassNotFoundException | InstantiationException | IllegalAccessException | UnsupportedLookAndFeelException e){
            System.err.println("Could not set look and feel"+e.getMessage());
        }

       SwingUtilities.invokeLater(() -> {
            JFrame frame = new JFrame("Expense Tracker Homie");
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.setSize(400, 200);
            frame.setLocationRelativeTo(null);
            frame.setLayout(new FlowLayout(FlowLayout.CENTER, 50, 50)); // ✅ Correct

            // Expense Button
            JButton expenseBtn = new JButton("Expense");
            expenseBtn.setPreferredSize(new Dimension(120, 50));
            expenseBtn.addActionListener(e -> new ExpenseGUI().setVisible(true));

            // Category Button
            JButton categoryBtn = new JButton("Category");
            categoryBtn.setPreferredSize(new Dimension(120, 50));
            categoryBtn.addActionListener(e -> new CategoryGUI().setVisible(true));

            // Add buttons to frame
            frame.add(expenseBtn);
            frame.add(categoryBtn);

            frame.setVisible(true);
        });

    }
}