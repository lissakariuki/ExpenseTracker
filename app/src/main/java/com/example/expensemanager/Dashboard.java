package com.example.expensemanager;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.example.expensemanager.databinding.ActivityDashboardBinding;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.ListenerRegistration;
import com.google.firebase.firestore.FirebaseFirestoreException;

import java.util.ArrayList;

public class Dashboard extends AppCompatActivity {
    private static final String TAG = "Dashboard";
    ActivityDashboardBinding binding;
    FirebaseFirestore firebaseFirestore;
    FirebaseAuth firebaseAuth;
    ListenerRegistration transactionListener;

    ArrayList<TransactionModel> transactionModelArrayList;
    TransactionAdapter transactionAdapter;
    private double totalIncome = 0.0;
    private double totalExpense = 0.0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d(TAG, "Dashboard onCreate started");
        binding = ActivityDashboardBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        Log.d(TAG, "Dashboard setContentView completed");

        firebaseFirestore = FirebaseFirestore.getInstance();
        firebaseAuth = FirebaseAuth.getInstance();

        // Initialize RecyclerView
        transactionModelArrayList = new ArrayList<>();
        binding.historyRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        binding.historyRecyclerView.setHasFixedSize(true);
        transactionAdapter = new TransactionAdapter(Dashboard.this, transactionModelArrayList);
        binding.historyRecyclerView.setAdapter(transactionAdapter);

        // Style the dashboard header
        binding.toolbarLayout.setBackgroundColor(getResources().getColor(R.color.red_500));
        binding.dashboardTitle.setTextColor(getResources().getColor(android.R.color.white));

        // Style the summary cards
        binding.summaryCard.setCardBackgroundColor(getResources().getColor(android.R.color.white));
        binding.summaryCard.setCardElevation(4f);
        binding.summaryCard.setRadius(8f);

        binding.addFloatingBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    Intent intent = new Intent(Dashboard.this, addtransactionactivity.class);
                    startActivity(intent);
                } catch (Exception e) {
                    Log.e(TAG, "Error starting addtransactionactivity", e);
                    Toast.makeText(Dashboard.this, "Error: Unable to add transaction", Toast.LENGTH_SHORT).show();
                }
            }
        });

        setupRealtimeUpdates();
        Log.d(TAG, "Dashboard onCreate completed");
    }

    private void setupRealtimeUpdates() {
        if (firebaseAuth.getCurrentUser() == null) {
            Log.e(TAG, "setupRealtimeUpdates: No user is signed in");
            Toast.makeText(this, "No user signed in. Please login again.", Toast.LENGTH_LONG).show();
            startActivity(new Intent(Dashboard.this, MainActivity.class));
            finish();
            return;
        }

        transactionListener = firebaseFirestore.collection("Expenses")
                .document(firebaseAuth.getUid())
                .collection("Notes")
                .addSnapshotListener(new EventListener<QuerySnapshot>() {
                    @Override
                    public void onEvent(QuerySnapshot snapshots, FirebaseFirestoreException e) {
                        if (e != null) {
                            Log.e(TAG, "Listen failed.", e);
                            return;
                        }

                        transactionModelArrayList.clear();
                        totalIncome = 0;
                        totalExpense = 0;

                        for (DocumentSnapshot document : snapshots) {
                            TransactionModel model = new TransactionModel(
                                    document.getString("id"),
                                    document.getString("note"),
                                    document.getString("amount"),
                                    document.getString("type"),
                                    document.getString("date")
                            );
                            transactionModelArrayList.add(model);

                            // Calculate totals
                            try {
                                double amount = Double.parseDouble(model.getAmount());
                                if ("Income".equals(model.getType())) {
                                    totalIncome += amount;
                                } else if ("Expense".equals(model.getType())) {
                                    totalExpense += amount;
                                }
                            } catch (NumberFormatException ex) {
                                Log.e(TAG, "Error parsing amount: " + model.getAmount(), ex);
                            }
                        }

                        // Update UI
                        updateDashboardSummary();
                        transactionAdapter.notifyDataSetChanged();
                    }
                });
    }

    private void updateDashboardSummary() {
        binding.incomeTextView.setText(String.format("%.2f", totalIncome));
        binding.incomeTextView.setTextColor(getResources().getColor(R.color.green_500));

        binding.expenseTextView.setText(String.format("%.2f", totalExpense));
        binding.expenseTextView.setTextColor(getResources().getColor(R.color.red_500));

        double balance = totalIncome - totalExpense;
        binding.balanceTextView.setText(String.format("%.2f", balance));
        binding.balanceTextView.setTextColor(balance >= 0 ?
                getResources().getColor(R.color.green_500) :
                getResources().getColor(R.color.red_500));
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.d(TAG, "Dashboard onResume called");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (transactionListener != null) {
            transactionListener.remove();
        }
    }
}

