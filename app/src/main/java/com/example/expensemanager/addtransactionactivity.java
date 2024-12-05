package com.example.expensemanager;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.expensemanager.databinding.ActivityAddtransactionactivityBinding;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.UUID;

public class addtransactionactivity extends AppCompatActivity {
    private static final String TAG = "AddTransactionActivity";
    ActivityAddtransactionactivityBinding binding;
    FirebaseFirestore fStore;
    String type = "";
    FirebaseAuth firebaseAuth;
    FirebaseUser firebaseUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityAddtransactionactivityBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        fStore = FirebaseFirestore.getInstance();
        firebaseAuth = FirebaseAuth.getInstance();
        firebaseUser = firebaseAuth.getCurrentUser();

        if (firebaseUser == null) {
            Log.e(TAG, "No user is signed in");
            Toast.makeText(this, "No user signed in. Please login again.", Toast.LENGTH_LONG).show();
            finish();
            return;
        }

        Log.d(TAG, "Current user ID: " + firebaseUser.getUid());

        setupCheckBoxListeners();
        setupAddTransactionButton();
    }

    private void setupCheckBoxListeners() {
        binding.expenseCheckBox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                type = "Expense";
                binding.expenseCheckBox.setChecked(true);
                binding.incomeCheckBox.setChecked(false);
            }
        });

        binding.incomeCheckBox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                type = "Income";
                binding.expenseCheckBox.setChecked(false);
                binding.incomeCheckBox.setChecked(true);
            }
        });
    }

    private void setupAddTransactionButton() {
        binding.btnAddTransaction.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String amount = binding.userAmountAdd.getText().toString().trim();
                String note = binding.userNoteAdd.getText().toString().trim();

                if (amount.isEmpty()) {
                    Toast.makeText(addtransactionactivity.this, "Please enter an amount", Toast.LENGTH_SHORT).show();
                    return;
                }

                if (type.isEmpty()) {
                    Toast.makeText(addtransactionactivity.this, "Please select a transaction type", Toast.LENGTH_SHORT).show();
                    return;
                }

                addTransaction(amount, note, type);
            }
        });
    }

    private void addTransaction(String amount, String note, String type) {
        if (firebaseUser == null) {
            Log.e(TAG, "Attempt to add transaction without user authentication");
            Toast.makeText(this, "Error: User not authenticated", Toast.LENGTH_SHORT).show();
            return;
        }

        SimpleDateFormat sdf = new SimpleDateFormat("dd MM yyyy_HH:mm", Locale.getDefault());
        String currentDateandTime = sdf.format(new Date());
        String id = UUID.randomUUID().toString();

        Map<String, Object> transaction = new HashMap<>();
        transaction.put("id", id);
        transaction.put("amount", amount);
        transaction.put("note", note);
        transaction.put("type", type);
        transaction.put("date", currentDateandTime);

        String userId = firebaseUser.getUid();
        Log.d(TAG, "Attempting to add transaction: " + transaction.toString());
        Log.d(TAG, "User ID: " + userId);
        Log.d(TAG, "Collection path: Expenses/" + userId + "/Notes/" + id);

        fStore.collection("Expenses")
                .document(userId)
                .collection("Notes")
                .document(id)
                .set(transaction)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {
                        Log.d(TAG, "Transaction added successfully");
                        Toast.makeText(addtransactionactivity.this, "Transaction added", Toast.LENGTH_SHORT).show();
                        clearInputFields();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.e(TAG, "Error adding transaction", e);
                        if (e instanceof FirebaseFirestoreException) {
                            FirebaseFirestoreException ffe = (FirebaseFirestoreException) e;
                            Log.e(TAG, "Firestore Exception Code: " + ffe.getCode());
                        }
                        Log.e(TAG, "Error details: " + e.getMessage());
                        Toast.makeText(addtransactionactivity.this, "Error: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private void clearInputFields() {
        binding.userNoteAdd.setText("");
        binding.userAmountAdd.setText("");
        binding.expenseCheckBox.setChecked(false);
        binding.incomeCheckBox.setChecked(false);
        type = "";
    }
}

