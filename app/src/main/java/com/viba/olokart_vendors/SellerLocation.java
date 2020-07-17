package com.viba.olokart_vendors;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.Looper;
import android.provider.Settings;
import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

public class SellerLocation extends AppCompatActivity {

    DatabaseReference databaseReference;
    FusedLocationProviderClient mFusedLocationClient;
    TextView editAddr, editCity, editState, editCtry, editZip;
    Geocoder geocoder;
    Double lat, longi;
    List<Address> addresses;
    int PERMISSION_ID = 44;
    String address, city, country, zip, state;
    ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_seller_location);
        editAddr = findViewById(R.id.address);

        progressDialog = new ProgressDialog(this);
        progressDialog.setTitle("Fetching your current location");
        progressDialog.setMessage("Please wait...");
        editCity = findViewById(R.id.city);
        editCtry = findViewById(R.id.ctry);
        editZip = findViewById(R.id.zip);
        editState = findViewById(R.id.state);

        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(this);
        geocoder = new Geocoder(SellerLocation.this, Locale.getDefault());
        addresses = null;
        databaseReference = FirebaseDatabase.getInstance().getReference("Sellers").child(FirebaseAuth.getInstance().getCurrentUser().getUid());

    }

    @SuppressLint("MissingPermission")
    private void getLastLocation() {
        if (checkPermissions()) {
            if (isLocationEnabled()) {
                mFusedLocationClient.getLastLocation().addOnCompleteListener(
                        new OnCompleteListener<Location>() {
                            @Override
                            public void onComplete(@NonNull Task<Location> task) {


                                Location location = task.getResult();
                                if (location == null) {
                                    requestNewLocationData();
                                } else {
                                    requestPermissions();

                                }

                            }
                        }
                );
            } else {
                Toast.makeText(this, "Turn on location", Toast.LENGTH_LONG).show();
                Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                startActivity(intent);
            }
        } else {
            requestPermissions();
        }
    }


    @SuppressLint("MissingPermission")
    private void requestNewLocationData() {

        LocationRequest mLocationRequest = new LocationRequest();
        mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        mLocationRequest.setInterval(0);
        mLocationRequest.setFastestInterval(0);
        mLocationRequest.setNumUpdates(1);

        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(this);
        mFusedLocationClient.requestLocationUpdates(
                mLocationRequest, mLocationCallback,
                Looper.myLooper()
        );
    }

    private LocationCallback mLocationCallback = new LocationCallback() {
        @Override
        public void onLocationResult(LocationResult locationResult) {
            Location mLastLocation = locationResult.getLastLocation();

            progressDialog.show();
            lat = mLastLocation.getLatitude();
            longi = mLastLocation.getLongitude();
            try {
                addresses = geocoder.getFromLocation(lat, longi, 1);
            } catch (IOException e) {
                e.printStackTrace();
            }
            address = addresses.get(0).getAddressLine(0);
            Toast.makeText(SellerLocation.this, address, Toast.LENGTH_SHORT).show();
            city = addresses.get(0).getLocality();
            state = addresses.get(0).getAdminArea();
            zip = addresses.get(0).getPostalCode();
            country = addresses.get(0).getCountryName();

            editAddr.setText(address);
            editCity.setText(city);
            editCtry.setText(country);
            editZip.setText(zip);
            editState.setText(state);
            progressDialog.dismiss();
        }
    };



    private boolean checkPermissions() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED &&
                ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            requestNewLocationData();
            return true;
        }
        return false;
    }

    private void requestPermissions() {
        ActivityCompat.requestPermissions(
                this,
                new String[]{Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.ACCESS_FINE_LOCATION},
                PERMISSION_ID
        );
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions,
                                           int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == PERMISSION_ID) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            }
        }
    }

    private boolean isLocationEnabled() {
        LocationManager locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        return locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER) || locationManager.isProviderEnabled(
                LocationManager.NETWORK_PROVIDER
        );
    }


    public void saveData() {
        progressDialog.show();
        HashMap<String, Object> SellerMap = new HashMap<>();
        SellerMap.put("scity", editCity.getText().toString());
        SellerMap.put("sstate", editState.getText().toString());
        SellerMap.put("szip", editZip.getText().toString());
        SellerMap.put("sctry", editCtry.getText().toString());
        SellerMap.put("saddress", editAddr.getText().toString());
        SellerMap.put("slat", String.valueOf(lat));
        SellerMap.put("slong", String.valueOf(longi));
        databaseReference.updateChildren(SellerMap);
        Toast.makeText(this, "Current location saved!", Toast.LENGTH_SHORT).show();
        progressDialog.dismiss();
        startActivity(new Intent(SellerLocation.this, HomeActivity.class));
        finish();
    }

    public void saveSellerloc(View view) {
        getLastLocation();
    }

    public void saveSellerData(View view) {
        if (TextUtils.isEmpty(editAddr.getText().toString()) && TextUtils.isEmpty(editCity.getText().toString()) && TextUtils.isEmpty(editZip.getText().toString()) && TextUtils.isEmpty(editState.getText().toString()) && TextUtils.isEmpty(editCtry.getText().toString())) {
            Snackbar.make(view, "Please Set Location and Save!", Snackbar.LENGTH_SHORT).show();
        } else {

            saveData();
        }
    }
}