package com.viba.olokart_vendors;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.navigation.ui.AppBarConfiguration;

import android.app.Activity;
import android.app.TabActivity;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TabHost;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import de.hdodenhof.circleimageview.CircleImageView;

public class HomeActivity extends TabActivity implements NavigationView.OnNavigationItemSelectedListener{
    TextView activeUsers, sname, scity, szip;
    DatabaseReference activeReference, databaseReference, userRef;
    String userid;
    RelativeLayout storeName;
    CircleImageView storeImage;
    DatabaseReference databaseRef;
    private AppBarConfiguration mAppBarConfiguration;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);

        databaseRef = FirebaseDatabase.getInstance().getReference("Sellers").child(FirebaseAuth.getInstance().getCurrentUser().getUid());
        databaseRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                String lat=dataSnapshot.child("slat").getValue().toString();
                String lon=dataSnapshot.child("slong").getValue().toString();
                if(lat.equals("0")&&lon.equals("0")){
                    showCustomDialog();

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
                    Picasso.get().load(userImage).placeholder(R.drawable.holder_shop).into(profileImageView);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
        sname = findViewById(R.id.snameHome);
        scity = findViewById(R.id.scityHome);
        szip = findViewById(R.id.szipHome);
        Toolbar toolbar = findViewById(R.id.toolbarHome);
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        NavigationView navigationView = findViewById(R.id.nav_drawer);

        checkConnection();

        navigationView.setNavigationItemSelectedListener(this);



        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();


        mAppBarConfiguration = new AppBarConfiguration.Builder(
                R.id.nav_home, R.id.aboutus, R.id.helpandsupport, R.id.logout, R.id.updatepro, R.id.desclaimer, R.id.feedback, R.id.logout)
                .setDrawerLayout(drawer).build();


        TabHost tabHost = getTabHost();

        Intent intentAndroid = new Intent().setClass(this, Statistics.class);
        TabHost.TabSpec tabSpecAndroid = tabHost
                .newTabSpec("Statistics")
                .setIndicator("Statistics")
                .setContent(intentAndroid);

        Intent intentApple = new Intent().setClass(this, ProductsAdded.class);
        TabHost.TabSpec tabSpecApple = tabHost
                .newTabSpec("Products_Added")
                .setIndicator("  Products\n      Added")
                .setContent(intentApple);



        Intent intentWindows = new Intent().setClass(this, RecentOrders.class);
        TabHost.TabSpec tabSpecWindows = tabHost
                .newTabSpec("Recent_Orders")
                .setIndicator("  Recent\n  Orders")
                .setContent(intentWindows);


        tabHost.addTab(tabSpecAndroid);
        tabHost.addTab(tabSpecApple);
        tabHost.addTab(tabSpecWindows);

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
                        break;
                    case R.id.navigation_orders:
                        Intent intent = new Intent(HomeActivity.this, Notification.class);
                        startActivity(intent);
                        overridePendingTransition(0, 0);
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
                String szipstr=dataSnapshot.child("szip").getValue().toString();
                String scitystr=dataSnapshot.child("scity").getValue().toString();
                sname.setText(snamestr);
                scity.setText(scitystr+" - ");
                szip.setText(szipstr);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

     }
    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();

        switch (id) {
            case R.id.nav_home:
                break;

            case R.id.aboutus:
                startActivity(new Intent(HomeActivity.this, About_us.class));
                break;
            case R.id.tearmsandconditions:
                startActivity(new Intent(HomeActivity.this, TermsandConditions.class));
                break;

            case R.id.helpandsupport:
                startActivity(new Intent(HomeActivity.this, HelpandSupport.class));
                break;
            case R.id.desclaimer:
                startActivity(new Intent(HomeActivity.this, Desclaimer.class));
                break;
            case R.id.feedback:
                startActivity(new Intent(HomeActivity.this, Rateus.class));
                break;
            case R.id.updatepro:
                startActivity(new Intent(HomeActivity.this, ChangeProfile.class));
                break;

            case R.id.logout:
                FirebaseAuth.getInstance().signOut();
                startActivity(new Intent(HomeActivity.this, SellerLoginActivity.class));
                ((Activity) HomeActivity.this).finish();
                break;

        }
        DrawerLayout drawerLayout = findViewById(R.id.drawer_layout);
        drawerLayout.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    public void onPointerCaptureChanged(boolean hasCapture) {

    }
    public void checkConnection() {
        ConnectivityManager connectivityManager = (ConnectivityManager) getApplicationContext().getSystemService(
                Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetwork = connectivityManager.getActiveNetworkInfo();

        if (null == activeNetwork) {
            Toast.makeText(HomeActivity.this, "No Internet Connection!",
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
                startActivity(new Intent(HomeActivity.this, SellerLocation.class));
                HomeActivity.this.finish();
            }
        });
    }
}