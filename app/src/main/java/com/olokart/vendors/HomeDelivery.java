package com.olokart.vendors;

import android.app.TabActivity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.TabHost;

public class HomeDelivery extends TabActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_delivery);

        TabHost tabHost = (TabHost)findViewById(android.R.id.tabhost); // initiate TabHost
        TabHost.TabSpec spec; // Reusable TabSpec for each tab
        Intent intent; // Reusable Intent for each tab

        spec = tabHost.newTabSpec("new"); // Create a new TabSpec using tab host
        spec.setIndicator("New");
       // set the “HOME” as an indicatora.
        // Create an Intent to launch an Activity tfor the tab (to be reused)
        intent = new Intent(this, HomeNewOrders.class).putExtra("optionmode", "Home Delivery");
        spec.setContent(intent);
        tabHost.addTab(spec);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_WHEN_TASK_RESET);

        // Do the same for the other tabs

        spec = tabHost.newTabSpec("ongoing"); // Create a new TabSpec using tab host
        spec.setIndicator("Ongoing");
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);// set the “CONTACT” as an indicator
        // Create an Intent to launch an Activity for the tab (to be reused)
        intent = new Intent(this, HomeOngoingOrders.class).putExtra("optionmode", "Home Delivery");
        spec.setContent(intent);
        tabHost.addTab(spec);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_WHEN_TASK_RESET);

        spec = tabHost.newTabSpec("past"); // Create a new TabSpec using tab host
        spec.setIndicator("Past");

        // Create an Intent to launch an Activity for the tab (to be reused)
        intent = new Intent(this, HomePastOrders.class).putExtra("optionmode", "Home Delivery");
        spec.setContent(intent);
        tabHost.addTab(spec);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_WHEN_TASK_RESET);

        //set tab which one you want to open first time 0 or 1 or 2
        tabHost.setCurrentTab(0);

    }
}