package com.example.expensemanager;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "MainActivity";

    private TextInputEditText emailLogin, passwordLogin;
    private MaterialButton btnLogin;
    private TextView goToSignupScreen;
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d(TAG, "onCreate: Initializing MainActivity");
        setContentView(R.layout.activity_main);

        mAuth = FirebaseAuth.getInstance();
        Log.d(TAG, "onCreate: FirebaseAuth instance initialized");

        emailLogin = findViewById(R.id.emailLogin);
        passwordLogin = findViewById(R.id.passwordLogin);
        btnLogin = findViewById(R.id.btnLogin);
        goToSignupScreen = findViewById(R.id.goToSignupScreen);
        Log.d(TAG, "onCreate: Views initialized");

        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(TAG, "onClick: Login button clicked");
                loginUser();
            }
        });

        goToSignupScreen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(TAG, "onClick: Go to signup screen clicked");
                startActivity(new Intent(MainActivity.this, Signup.class));
            }
        });

        Log.i(TAG, "onCreate: MainActivity setup completed");
    }

    @Override
    protected void onStart() {
        super.onStart();
        Log.d(TAG, "onStart: Checking for current user");
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if(currentUser != null){
            Log.i(TAG, "onStart: User already logged in, redirecting to Dashboard");
            startActivity(new Intent(MainActivity.this, Dashboard.class));
            finish();
        } else {
            Log.i(TAG, "onStart: No user logged in");
        }
    }

    private void loginUser() {
        Log.d(TAG, "loginUser: Attempting to log in user");
        String email = emailLogin.getText().toString().trim();
        String password = passwordLogin.getText().toString().trim();

        if (email.isEmpty() || password.isEmpty()) {
            Log.w(TAG, "loginUser: Empty email or password");
            Toast.makeText(this, "Please fill all fields", Toast.LENGTH_SHORT).show();
            return;
        }

        Log.d(TAG, "loginUser: Attempting Firebase authentication");
        mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, task -> {
                    if (task.isSuccessful()) {
                        Log.i(TAG, "loginUser: Login successful");
                        Toast.makeText(MainActivity.this, "Login successful", Toast.LENGTH_SHORT).show();
                        startActivity(new Intent(MainActivity.this, Dashboard.class));
                        finish();
                    } else {
                        Log.e(TAG, "loginUser: Authentication failed", task.getException());
                        Toast.makeText(MainActivity.this, "Authentication failed: " + task.getException().getMessage(),
                                Toast.LENGTH_SHORT).show();
                    }
                });
    }
}

