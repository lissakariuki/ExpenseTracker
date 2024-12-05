package com.example.expensemanager;

import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.google.android.material.button.MaterialButton;
import com.google.android.material.radiobutton.MaterialRadioButton;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class addtransactionactivity extends AppCompatActivity {

    private TextInputEditText amountInput, noteInput;
    private MaterialRadioButton radioIncome, radioExpense;
    private MaterialButton addButton;
    private FirebaseFirestore firestore;
    private FirebaseAuth mAuth;
    private AutoCompleteTextView categorySpinner;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_addtransactionactivity);

        // Initialize Firebase Firestore and Auth
        firestore = FirebaseFirestore.getInstance();
        mAuth = FirebaseAuth.getInstance();

        // Set up toolbar
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        // Initialize UI components
        amountInput = findViewById(R.id.editTextAmount);
        noteInput = findViewById(R.id.editTextNote);
        categorySpinner = findViewById(R.id.spinnerCategory);
        radioIncome = findViewById(R.id.radioIncome);
        radioExpense = findViewById(R.id.radioExpense);
        addButton = findViewById(R.id.btnAddTransaction);

        setupCategorySpinner();

        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addTransactionToFirestore();
            }
        });
    }

    private void setupCategorySpinner() {
        String[] categories = {"Food", "Transportation", "Housing", "Utilities", "Insurance", "Healthcare", "Savings", "Personal", "Entertainment", "Other"};
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_dropdown_item_1line, categories);
        categorySpinner.setAdapter(adapter);
    }

    private void addTransactionToFirestore() {
        String amountStr = amountInput.getText().toString().trim();
        String note = noteInput.getText().toString().trim();
        String category = categorySpinner.getText().toString().trim();
        String type = radioIncome.isChecked() ? "Income" : "Expense";

        if (amountStr.isEmpty() || category.isEmpty() || (!radioIncome.isChecked() && !radioExpense.isChecked())) {
            Toast.makeText(this, "Please fill all fields", Toast.LENGTH_SHORT).show();
            return;
        }

        double amount = Double.parseDouble(amountStr);
        String userId = mAuth.getCurrentUser().getUid();
        String id = UUID.randomUUID().toString();

        Map<String, Object> transaction = new HashMap<>();
        transaction.put("id", id);
        transaction.put("amount", amount);
        transaction.put("note", note);
        transaction.put("category", category);
        transaction.put("type", type);
        transaction.put("timestamp", new Date());

        // Save to Firestore
        firestore.collection("users").document(userId).collection("transactions").document(id)
                .set(transaction)
                .addOnSuccessListener(aVoid -> {
                    Toast.makeText(this, "Transaction added successfully", Toast.LENGTH_SHORT).show();
                    finish();
                })
                .addOnFailureListener(e -> Toast.makeText(this, "Failed to add transaction", Toast.LENGTH_SHORT).show());
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }
}
