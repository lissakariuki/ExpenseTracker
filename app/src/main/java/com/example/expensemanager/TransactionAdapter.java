package com.example.expensemanager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class TransactionAdapter extends RecyclerView.Adapter<TransactionAdapter.ViewHolder> {

    private final List<TransactionModel> transactionList;

    // Constructor to initialize the list of transactions
    public TransactionAdapter(List<TransactionModel> transactionList) {
        this.transactionList = transactionList;
    }

    // Called when creating a new view holder
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // Inflating the item_transaction layout file
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_transaction, parent, false);
        return new ViewHolder(view);
    }

    // Bind data to the views in the ViewHolder
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        TransactionModel transaction = transactionList.get(position);

        // Set text for each TextView based on the transaction data
        holder.noteTextView.setText(transaction.getNote());

        // Format amount to include currency symbol
        holder.amountTextView.setText(String.format("$%s", transaction.getAmount()));

        // Set transaction type (e.g., "Income" or "Expense")
        holder.typeTextView.setText(transaction.getType());

        // Set category name
        holder.categoryTextView.setText(transaction.getCategory());
    }

    // Returns the total count of items in the list
    @Override
    public int getItemCount() {
        return transactionList.size();
    }

    // ViewHolder class to represent each item in the RecyclerView
    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView noteTextView, amountTextView, typeTextView, categoryTextView;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            // Initialize each TextView in the item layout
            noteTextView = itemView.findViewById(R.id.transactionNote);
            amountTextView = itemView.findViewById(R.id.transactionAmount);
            typeTextView = itemView.findViewById(R.id.transactionType);
            categoryTextView = itemView.findViewById(R.id.transactionCategory);
        }
    }
}
