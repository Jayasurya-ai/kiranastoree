package com.olokart.vendors;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;

import android.app.TabActivity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TabHost;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.messaging.FirebaseMessaging;
import com.squareup.picasso.Picasso;

import de.hdodenhof.circleimageview.CircleImageView;

public class HomeActivity extends TabActivity {
    TextView  sname, scity;
    DatabaseReference databaseReference;
    DatabaseReference databaseRef, updateRef;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        updateRef = FirebaseDatabase.getInstance().getReference("Update");
        databaseRef = FirebaseDatabase.getInstance().getReference("Sellers").child(FirebaseAuth.getInstance().getCurrentUser().getUid());
        updateRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                String update=dataSnapshot.child("business").getValue().toString();
                String version;
                try {
                    PackageInfo pInfo = getPackageManager().getPackageInfo(getPackageName(), 0);
                     version = pInfo.versionName;
                  //  Toast.makeText(HomeActivity.this, ""+version, Toast.LENGTH_SHORT).show();

                if(!version.equals(update)){
                    updateCustomDialog();
                }
                } catch (PackageManager.NameNotFoundException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        databaseRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                try {

                    String lat = dataSnapshot.child("slat").getValue().toString();
                    String lon = dataSnapshot.child("slong").getValue().toString();
                    if (lat.equals("0") && lon.equals("0")) {
                        showCustomDialog();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
        databaseRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                try {

                    TextView userNameTextView = findViewById(R.id.storeName);
                    CircleImageView profileImageView = findViewById(R.id.store_profile);
                    String userName = dataSnapshot.child("sname").getValue().toString();
                    String userImage = dataSnapshot.child("simage").getValue().toString();

                    userNameTextView.setText(userName);
                    Picasso.get().load(userImage).placeholder(R.drawable.shopimage).into(profileImageView);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        FirebaseMessaging.getInstance().subscribeToTopic(FirebaseAuth.getInstance().getCurrentUser().getUid());

        sname = findViewById(R.id.snameHome);
        scity = findViewById(R.id.scityHome);

        checkConnection();

        TabHost tabHost = findViewById(android.R.id.tabhost); // initiate TabHost
        TabHost.TabSpec spec; // Reusable TabSpec for each tab
        Intent intent; // Reusable Intent for each tab

        spec = tabHost.newTabSpec("recent/norders"); // Create a new TabSpec using tab host
        spec.setIndicator("Recent\nOrders"); // set the “CONTACT” as an indicator
        // Create an Intent to launch an Activity for the tab (to be reused)
        intent = new Intent(this, RecentOrders.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        spec.setContent(intent);
        tabHost.addTab(spec);

        spec = tabHost.newTabSpec("statistics"); // Create a new TabSpec using tab host
        spec.setIndicator("Statistics"); // set the “HOME” as an indicator
        // Create an Intent to launch an Activity for the tab (to be reused)
        intent = new Intent(this, Statistics.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        spec.setContent(intent);
        tabHost.addTab(spec);



        // Do the same for the other tabs

        spec = tabHost.newTabSpec("productsadded"); // Create a new TabSpec using tab host
        spec.setIndicator("Products\nAdded"); // set the “CONTACT” as an indicator
        // Create an Intent to launch an Activity for the tab (to be reused)
        intent = new Intent(this, ProductsAdded.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        spec.setContent(intent);
        tabHost.addTab(spec);


        //set tab which one you want to open first time 0 or 1 or 2
        tabHost.setCurrentTab(0);


        BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.nav_view);
        navigation.setSelectedItemId(R.id.navigation_home);
        navigation.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.navigation_home:

                        break;
                    case R.id.navigation_add:
                        Intent intentCat = new Intent(HomeActivity.this, SellerAddActivity.class);
                        startActivity(intentCat);
                        overridePendingTransition(0, 0);
                        finish();
                        break;
                    case R.id.navigation_orders:
                        Intent intent = new Intent(HomeActivity.this, MyProfileActivity.class);
                        startActivity(intent);
                        overridePendingTransition(0, 0);
                        finish();
                        break;
                }
                return false;
            }
        });


        databaseReference= FirebaseDatabase.getInstance().getReference("Sellers").child(FirebaseAuth.getInstance().getCurrentUser().getUid());
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                String snamestr=dataSnapshot.child("sname").getValue().toString();
                String scitystr=dataSnapshot.child("scity").getValue().toString();
                sname.setText(snamestr);
                scity.setText(scitystr);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

     }

    public void checkConnection() {
        ConnectivityManager connectivityManager = (ConnectivityManager) getApplicationContext().getSystemService(
                Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetwork = connectivityManager.getActiveNetworkInfo();

        if (null == activeNetwork) {
            Toast.makeText(HomeActivity.this, "No Internet Connection!...",
                    Toast.LENGTH_LONG).show();
        }
    }
    private void showCustomDialog() {

        ViewGroup viewGroup = findViewById(android.R.id.content);

        View dialogView = LayoutInflater.from(this).inflate(R.layout.errorlocation, viewGroup, false);


        AlertDialog.Builder builder = new AlertDialog.Builder(this);

        builder.setView(dialogView);

        AlertDialog alertDialog = builder.create();
        alertDialog.show();
        TextView ok = dialogView.findViewById(R.id.buttonloc);
        alertDialog.setCanceledOnTouchOutside(false);
        ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(HomeActivity.this, movemap.class));
                HomeActivity.this.finish();
            }
        });
    }

    private void updateCustomDialog() {

        ViewGroup viewGroup = findViewById(android.R.id.content);

        View dialogView = LayoutInflater.from(this).inflate(R.layout.update_layout, viewGroup, false);

        AlertDialog.Builder builder = new AlertDialog.Builder(this);

        builder.setView(dialogView);

        AlertDialog alertDialog = builder.create();
        alertDialog.show();
        TextView ok = dialogView.findViewById(R.id.updatebtn);
        alertDialog.setCanceledOnTouchOutside(false);
        ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                    String link = "market://details?id=";
                    try {
                        // play market available
                        getPackageManager()
                                .getPackageInfo("com.olokart.vendors", 0);
                        // not available
                    } catch (PackageManager.NameNotFoundException e) {
                        e.printStackTrace();
                        // should use browser
                        link = "https://play.google.com/store/apps/details?id=com.olokart.vendors";
                    }
                    // starts external action
                    startActivity(new Intent(Intent.ACTION_VIEW,
                            Uri.parse(link + getPackageName())));
            }
        });
    }
}