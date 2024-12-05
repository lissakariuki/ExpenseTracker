package com.example.expensemanager;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.GoogleAuthProvider;

public class Signup extends AppCompatActivity {

    private static final String TAG = "SignupActivity";
    private static final int RC_SIGN_IN = 9001;
    private FirebaseAuth mAuth;
    private GoogleSignInClient mGoogleSignInClient;

    private TextInputEditText emailForSignup, passwordForSignup;
    private Button btnSignup;
    private MaterialButton btnGoogleSignIn;
    private TextView goToLoginScreen;
    private Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        // Initialize Firebase Auth
        mAuth = FirebaseAuth.getInstance();

        // Initialize views
        initializeViews();

        // Setup toolbar
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        // Setup click listeners
        setupClickListeners();

        // Configure Google Sign In
        setupGoogleSignIn();
    }

    private void initializeViews() {
        toolbar = findViewById(R.id.toolbar);
        emailForSignup = findViewById(R.id.emailForSignup);
        passwordForSignup = findViewById(R.id.passwordForSignup);
        btnSignup = findViewById(R.id.btnsignup);
        btnGoogleSignIn = findViewById(R.id.btnGoogleSignIn);
        goToLoginScreen = findViewById(R.id.goToLoginScreen);
    }

    private void setupClickListeners() {
        btnSignup.setOnClickListener(v -> signUpWithEmail());
        btnGoogleSignIn.setOnClickListener(v -> signInWithGoogle());
        goToLoginScreen.setOnClickListener(v -> {
            startActivity(new Intent(Signup.this, MainActivity.class));
            finish();
        });
    }

    private void setupGoogleSignIn() {
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();

        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);
    }

    private void signUpWithEmail() {
        String email = emailForSignup.getText().toString().trim();
        String password = passwordForSignup.getText().toString().trim();

        if (email.isEmpty() || password.isEmpty()) {
            Toast.makeText(this, "Please fill all fields", Toast.LENGTH_SHORT).show();
            return;
        }

        // Show loading state
        btnSignup.setEnabled(false);
        btnSignup.setText("Creating Account...");

        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, task -> {
                    btnSignup.setEnabled(true);
                    btnSignup.setText("Sign Up");

                    if (task.isSuccessful()) {
                        Toast.makeText(Signup.this, "Account created successfully", Toast.LENGTH_SHORT).show();
                        startActivity(new Intent(Signup.this, MainActivity.class));
                        finish();
                    } else {
                        Log.e(TAG, "Sign up failed: ", task.getException());
                        Toast.makeText(Signup.this, "Sign up failed: " + task.getException().getMessage(),
                                Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private void signInWithGoogle() {
        Intent signInIntent = mGoogleSignInClient.getSignInIntent();
        startActivityForResult(signInIntent, RC_SIGN_IN);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == RC_SIGN_IN) {
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            try {
                GoogleSignInAccount account = task.getResult(ApiException.class);
                if (account != null) {
                    firebaseAuthWithGoogle(account.getIdToken());
                } else {
                    Toast.makeText(this, "Google Sign-In failed.", Toast.LENGTH_SHORT).show();
                }
            } catch (ApiException e) {
                Log.e(TAG, "Google sign in failed", e);
                Toast.makeText(this, "Google sign in failed: " + e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void firebaseAuthWithGoogle(String idToken) {
        AuthCredential credential = GoogleAuthProvider.getCredential(idToken, null);
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, task -> {
                    if (task.isSuccessful()) {
                        Log.d(TAG, "Google sign-in successful.");
                        Toast.makeText(Signup.this, "Signed in with Google successfully", Toast.LENGTH_SHORT).show();
                        startActivity(new Intent(Signup.this, MainActivity.class));
                        finish();
                    } else {
                        Log.e(TAG, "Google sign-in failed", task.getException());
                        Toast.makeText(Signup.this, "Authentication failed: " + task.getException().getMessage(),
                                Toast.LENGTH_SHORT).show();
                    }
                });
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }
}
