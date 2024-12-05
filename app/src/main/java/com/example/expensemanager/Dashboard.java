package com.example.expensemanager;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

public class Dashboard extends AppCompatActivity {

    private RecyclerView recyclerView;
    private TransactionAdapter transactionAdapter;
    private FirebaseAuth mAuth;
    private FirebaseFirestore db;
    private List<TransactionModel> transactionList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);

        // Initialize Firebase Auth and Firestore
        mAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();

        // Initialize RecyclerView
        recyclerView = findViewById(R.id.recyclerViewTransactions);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        transactionList = new ArrayList<>();
        transactionAdapter = new TransactionAdapter(transactionList);
        recyclerView.setAdapter(transactionAdapter);

        // Load data from Firestore
        loadTransactions();

        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setHomeButtonEnabled(true);
        }

        // Set up the "Add Transaction" button click listener
        Button btnAddTransaction = findViewById(R.id.btnAddTransaction);
        btnAddTransaction.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Create an Intent to open AddTransactionActivity
                Intent intent = new Intent(Dashboard.this, addtransactionactivity.class);
                startActivity(intent); // This will open AddTransactionActivity
            }
        });
    }

    // Handle ActionBar back button press
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed(); // Goes back to the previous activity
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void loadTransactions() {
        String userId = mAuth.getCurrentUser().getUid(); // Get current user ID

        // Reference to the user's transactions in Firestore
        CollectionReference transactionsRef = db.collection("users").document(userId).collection("transactions");

        // Query to fetch the transactions in ascending order of the timestamp
        Query query = transactionsRef.orderBy("timestamp", Query.Direction.DESCENDING);

        query.get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                transactionList.clear(); // Clear existing data
                QuerySnapshot querySnapshot = task.getResult();

                if (querySnapshot != null) {
                    for (DocumentSnapshot documentSnapshot : querySnapshot.getDocuments()) {
                        TransactionModel transaction = documentSnapshot.toObject(TransactionModel.class);
                        if (transaction != null) {
                            transactionList.add(transaction);
                        }
                    }
                    transactionAdapter.notifyDataSetChanged(); // Notify adapter about data changes
                }
            } else {
                Log.e("FirestoreError", "Error fetching transactions", task.getException());
                Toast.makeText(Dashboard.this, "Failed to load transactions", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
