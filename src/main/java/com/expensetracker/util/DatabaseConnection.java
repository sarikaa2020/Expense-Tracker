package com.expensetracker.util;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DatabaseConnection{
    public static final String URL = "jdbc:mysql://localhost:3306/expensetracker";
    public static final String USERNAME = "root";
    public static final String PASSWORD = "Sari@2327";

    static
    {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
        } 
        catch (ClassNotFoundException e) 
        {
            System.out.println("JDBC DRIVER IS MISSING");
        }
    }
    public static Connection getDBConnection() throws SQLException
    {
        return DriverManager.getConnection(URL, USERNAME, PASSWORD);
    }
}