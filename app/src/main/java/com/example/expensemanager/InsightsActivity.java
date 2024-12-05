package com.example.expensemanager;

import android.content.Intent;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class InsightsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_insights);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle("Insights");
        }

        BottomNavigationView bottomNavigationView = findViewById(R.id.bottomNavigation);
        bottomNavigationView.setSelectedItemId(R.id.navigation_insights);
        bottomNavigationView.setOnItemSelectedListener(item -> {
            int itemId = item.getItemId();
            if (itemId == R.id.navigation_dashboard) {
                startActivity(new Intent(this, Dashboard.class));
                finish();
                return true;
            } else if (itemId == R.id.navigation_chart) {
                startActivity(new Intent(this, ChartActivity.class));
                finish();
                return true;
            } else if (itemId == R.id.navigation_calendar) {
                startActivity(new Intent(this, CalendarActivity.class));
                finish();
                return true;
            } else if (itemId == R.id.navigation_insights) {
                return true;
            }
            return false;
        });
    }
}

