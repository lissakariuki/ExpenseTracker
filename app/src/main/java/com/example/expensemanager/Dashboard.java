package com.example.expensemanager;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.floatingactionbutton.ExtendedFloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;

public class Dashboard extends AppCompatActivity {

    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);

        mAuth = FirebaseAuth.getInstance();

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle("Dashboard");
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_menu); // You'll need to create this icon
        }

        BottomNavigationView bottomNavigationView = findViewById(R.id.bottomNavigation);
        bottomNavigationView.setSelectedItemId(R.id.navigation_dashboard);
        bottomNavigationView.setOnNavigationItemSelectedListener(item -> {
            switch (item.getItemId()) {
                case R.id.navigation_dashboard:
                    return true;
                case R.id.navigation_chart:
                    startActivity(new Intent(this, ChartActivity.class));
                    return true;
                case R.id.navigation_calendar:
                    startActivity(new Intent(this, CalendarActivity.class));
                    return true;
                case R.id.navigation_insights:
                    startActivity(new Intent(this, InsightsActivity.class));
                    return true;
            }
            return false;
        });

        ExtendedFloatingActionButton addFloatingBtn = findViewById(R.id.addFloatingBtn);
        addFloatingBtn.setOnClickListener(v -> {
            startActivity(new Intent(Dashboard.this, addtransactionactivity.class));
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.dashboard_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                // Handle the home/menu button
                return true;
            case R.id.action_settings:
                // Handle the settings action
                return true;
            case R.id.action_logout:
                mAuth.signOut();
                startActivity(new Intent(Dashboard.this, MainActivity.class));
                finish();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}

