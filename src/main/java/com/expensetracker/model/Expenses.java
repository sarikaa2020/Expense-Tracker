package com.expensetracker.model;

import java.time.LocalDateTime;

public class Expenses {
    private int id;
    private double amount;
    private LocalDateTime date;
    private Category category;
    private String description;
    public String type;

    public Expenses() {
        this.date = LocalDateTime.now();
    }

    public Expenses(int id, String description, double amount, LocalDateTime date ,String type, Category category) {
        this.id = id;
        this.description = description;
        this.amount = amount;
        this.date = date;
        this.type = type;
        this.category = category;
        
    }
    
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    public LocalDateTime getDate() {
        return date;
    }

    public void setDate(LocalDateTime date) {
        this.date = date;
    }

    public Category getCategory() {
        return category;
    }

    public void setCategory(Category category) {
        this.category = category;
    }

    public String getDescription() {
        return description;
    }   
    public void setDescription(String description) {
        this.description = description;
    }

    public String getType() {
        return type;
        }

    public void setType(String type) {
        this.type = type;
        }
    

}
