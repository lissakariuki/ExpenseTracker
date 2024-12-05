package com.example.expensemanager;

public class Transaction {

        private String title;
        private String date;
        private double amount;

        public Transaction() {
            // Default constructor required for Firestore
        }

        public Transaction(String title, String date, double amount) {
            this.title = title;
            this.date = date;
            this.amount = amount;
        }

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public String getDate() {
            return date;
        }

        public void setDate(String date) {
            this.date = date;
        }

        public double getAmount() {
            return amount;
        }

        public void setAmount(double amount) {
            this.amount = amount;
        }
    }

