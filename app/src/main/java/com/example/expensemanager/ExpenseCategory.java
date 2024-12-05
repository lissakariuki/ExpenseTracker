package com.example.expensemanager;

public class ExpenseCategory {
    private String name;
    private float amount;
    private float percentage;
    private int color;

    public ExpenseCategory(String name, float amount, float percentage, int color) {
        this.name = name;
        this.amount = amount;
        this.percentage = percentage;
        this.color = color;
    }

    // Required getters that were missing
    public String getName() {
        return name;
    }

    public float getAmount() {
        return amount;
    }

    public float getPercentage() {
        return percentage;
    }

    public int getColor() {
        return color;
    }

    // Formatted string methods for display
    public String getFormattedAmount() {
        return String.format("£%.2f", amount);
    }

    public String getFormattedPercentage() {
        return String.format("%.0f%%", percentage);
    }

    // Setters in case we need to update values
    public void setName(String name) {
        this.name = name;
    }

    public void setAmount(float amount) {
        this.amount = amount;
    }

    public void setPercentage(float percentage) {
        this.percentage = percentage;
    }

    public void setColor(int color) {
        this.color = color;
    }
}


