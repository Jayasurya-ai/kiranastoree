package com.viba.olokart_vendors;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;

import com.google.android.material.bottomnavigation.BottomNavigationView;

public class Notification extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notification);

        BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.nav_notify);
        navigation.setSelectedItemId(R.id.navigation_orders);
        navigation.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.navigation_home:

                        Intent intent = new Intent(Notification.this, HomeActivity.class);
                        startActivity(intent);
                        overridePendingTransition(0, 0);
                        break;
                    case R.id.navigation_add:
                        Intent intentCat = new Intent(Notification.this, SellerAddActivity.class);
                        startActivity(intentCat);
                        overridePendingTransition(0, 0);
                        break;
                    case R.id.navigation_orders:
                        break;
                }
                return false;
            }
        });
    }
}