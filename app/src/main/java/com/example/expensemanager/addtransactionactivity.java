package com.example.expensemanager;

import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.RadioGroup;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.Date;
import java.util.UUID;

public class addtransactionactivity extends AppCompatActivity {

    private TextInputEditText amountInput, noteInput;
    private AutoCompleteTextView categorySpinner;
    private RadioGroup typeGroup;
    private MaterialButton addButton;

    private FirebaseAuth mAuth;
    private DatabaseReference mDatabase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_addtransactionactivity);

        mAuth = FirebaseAuth.getInstance();
        mDatabase = FirebaseDatabase.getInstance().getReference();

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        amountInput = findViewById(R.id.user_amount_add);
        noteInput = findViewById(R.id.user_note_add);
        categorySpinner = findViewById(R.id.category_spinner);
        typeGroup = findViewById(R.id.transaction_type_group);
        addButton = findViewById(R.id.btn_add_transaction);

        setupCategorySpinner();

        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addTransaction();
            }
        });
    }

    private void setupCategorySpinner() {
        String[] categories = {"Food", "Transportation", "Housing", "Utilities", "Insurance", "Healthcare", "Savings", "Personal", "Entertainment", "Other"};
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_dropdown_item_1line, categories);
        categorySpinner.setAdapter(adapter);
    }

    private void addTransaction() {
        String amountStr = amountInput.getText().toString();
        String note = noteInput.getText().toString();
        String category = categorySpinner.getText().toString();
        String type = typeGroup.getCheckedRadioButtonId() == R.id.expense_radio_button ? "Expense" : "Income";

        if (amountStr.isEmpty() || category.isEmpty()) {
            Toast.makeText(this, "Please fill all fields", Toast.LENGTH_SHORT).show();
            return;
        }

        double amount = Double.parseDouble(amountStr);
        String id = UUID.randomUUID().toString();
        //Date date = new Date(); //Removed Date from TransactionModel

        TransactionModel transaction = new TransactionModel(id, note, String.valueOf(amount), type, category);

        String userId = mAuth.getCurrentUser().getUid();
        mDatabase.child("users").child(userId).child("transactions").child(id).setValue(transaction)
                .addOnSuccessListener(aVoid -> {
                    Toast.makeText(addtransactionactivity.this, "Transaction added successfully", Toast.LENGTH_SHORT).show();
                    finish();
                })
                .addOnFailureListener(e -> Toast.makeText(addtransactionactivity.this, "Failed to add transaction", Toast.LENGTH_SHORT).show());
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }
}

