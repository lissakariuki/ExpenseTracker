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

    public TransactionAdapter(List<TransactionModel> transactionList) {
        this.transactionList = transactionList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_transaction, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        TransactionModel transaction = transactionList.get(position);
        holder.noteTextView.setText(transaction.getNote());
        holder.amountTextView.setText(String.format("$%s", transaction.getAmount()));
        holder.typeTextView.setText(transaction.getType());
        holder.categoryTextView.setText(transaction.getCategory());
    }

    @Override
    public int getItemCount() {
        return transactionList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView noteTextView, amountTextView, typeTextView, categoryTextView;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            noteTextView = itemView.findViewById(R.id.transactionNote);
            amountTextView = itemView.findViewById(R.id.transactionAmount);
            typeTextView = itemView.findViewById(R.id.transactionType);
            categoryTextView = itemView.findViewById(R.id.transactionCategory);
        }
    }
}
