package com.example.expensemanager;

import android.content.Intent;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class CalendarActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //setContentView(R.layout.activity_calendar); //Removed this line as per update instruction.  The layout should be set elsewhere or handled differently.

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Calendar");

        BottomNavigationView bottomNavigationView = findViewById(R.id.bottomNavigation);
        bottomNavigationView.setSelectedItemId(R.id.navigation_calendar);
        bottomNavigationView.setOnNavigationItemSelectedListener(item -> {
            switch (item.getItemId()) {
                case R.id.navigation_dashboard:
                    startActivity(new Intent(this, Dashboard.class));
                    finish();
                    return true;
                case R.id.navigation_chart:
                    startActivity(new Intent(this, ChartActivity.class));
                    finish();
                    return true;
                case R.id.navigation_insights:
                    startActivity(new Intent(this, InsightsActivity.class));
                    finish();
                    return true;
            }
            return false;
        });
    }
}

