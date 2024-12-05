package com.example.expensemanager;

import android.content.Intent;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class ChartActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //setContentView(R.layout.activity_chart); //Removed as per update instruction.  This line is likely to be replaced with a different layout or setup.
        //The rest of the code remains unchanged.  Consider adding a comment explaining why setContentView is removed.

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Chart");

        BottomNavigationView bottomNavigationView = findViewById(R.id.bottomNavigation);
        bottomNavigationView.setSelectedItemId(R.id.navigation_chart);
        bottomNavigationView.setOnNavigationItemSelectedListener(item -> {
            switch (item.getItemId()) {
                case R.id.navigation_dashboard:
                    startActivity(new Intent(this, Dashboard.class));
                    finish();
                    return true;
                case R.id.navigation_calendar:
                    startActivity(new Intent(this, CalendarActivity.class));
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

