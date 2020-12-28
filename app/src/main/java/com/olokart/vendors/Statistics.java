package com.olokart.vendors;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.text.format.DateFormat;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class Statistics extends AppCompatActivity {
    private Calendar calendar;
    private TextView dateView, collectMoney, MontlyMoney, YearlyMonely;
    String  totalPrice;
    String date, month, year;
    TextView nearByCustomer;
    Button button;
    Double nearbyDis = 6.00;
    DatabaseReference usercurrentRef, sellerRef, sellerOrders;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_statistics);
        collectMoney = findViewById(R.id.collectTotalMoney);
        dateView = (TextView) findViewById(R.id.textView3);
        button = findViewById(R.id.button1);
        MontlyMoney = findViewById(R.id.collectTotalMonth);
        YearlyMonely = findViewById(R.id.collectTotaYear);
        calendar = Calendar.getInstance();

        Date d = new Date();
        CharSequence s  = DateFormat.format("yyyy-MM-dd", d.getTime());
        dateView.setText(s);


        final DatePickerDialog.OnDateSetListener datepicker = new DatePickerDialog.OnDateSetListener() {

            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear,
                                  int dayOfMonth) {
                // TODO Auto-generated method stub
                calendar.set(Calendar.YEAR, year);
                calendar.set(Calendar.MONTH, monthOfYear);
                calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);

                updateLabel();
                sellerDate();
            }

        };

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new DatePickerDialog(Statistics.this, datepicker, calendar
                        .get(Calendar.YEAR), calendar.get(Calendar.MONTH),
                        calendar.get(Calendar.DAY_OF_MONTH)).show();
            }
        });
//
//
//
//
        nearByCustomer = findViewById(R.id.countNearby);
////////
        sellerOrders = FirebaseDatabase.getInstance().getReference("Orders").child(FirebaseAuth.getInstance().getCurrentUser().getUid());
        sellerRef = FirebaseDatabase.getInstance().getReference("Sellers").child(FirebaseAuth.getInstance().getCurrentUser().getUid());
        usercurrentRef = FirebaseDatabase.getInstance().getReference("Users");

        recycler();
        sellerDate();
//    }
    }

    private void updateLabel() {
        String myFormat = "yyyy-MM-dd"; //In which you need put here
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);

        dateView.setText(sdf.format(calendar.getTime()));
    }

    public void recycler() {
        try {

            sellerRef.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot userLoc) {
                    final String userLat = userLoc.child("slat").getValue().toString();
                    final String userLong = userLoc.child("slong").getValue().toString();

                    final Double lat1 = Double.parseDouble(userLat);
                    final Double lon1 = Double.parseDouble(userLong);

                    usercurrentRef.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot sellerLoc) {

                            long count = 0;
                            for (DataSnapshot dataSnapshot : sellerLoc.getChildren()) {

                                if (dataSnapshot.exists()) {

                                    String latstr1 = dataSnapshot.child("ulat").getValue().toString();
                                    String lonstr1 = dataSnapshot.child("ulong").getValue().toString();

                                    final Double lat2 = Double.parseDouble(latstr1);
                                    final Double lon2 = Double.parseDouble(lonstr1);

                                    Double rlon1, rlon2, rlat1, rlat2;
                                    rlon1 = Math.toRadians(lon1);
                                    rlon2 = Math.toRadians(lon2);
                                    rlat1 = Math.toRadians(lat1);
                                    rlat2 = Math.toRadians(lat2);
                                    Double dlon = rlon2 - rlon1;
                                    Double dlat = rlat2 - rlat1;
                                    Double a = Math.pow(Math.sin(dlat / 2), 2)
                                            + Math.cos(rlat1) * Math.cos(rlat2)
                                            * Math.pow(Math.sin(dlon / 2), 2);

                                    Double c = 2 * Math.asin(Math.sqrt(a));
                                    Integer r = 8000;

                                    // calculate the result
                                    double result = c * r;

                                    if (result <= nearbyDis) {
                                        count = count + 1;
                                        nearByCustomer.setText(String.valueOf(count));
                                    }
                                }
                            }

                        }


                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {

                        }
                    });
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public  void sellerDate() {

        sellerOrders.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                date = dateView.getText().toString();
                month = dateView.getText().toString().substring(5, 7);
                year = dateView.getText().toString().substring(0, 4);
                int addMoney = 0;
                int addMontly = 0;
                int addYearly = 0;
                for (DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()) {
                    for (DataSnapshot dataSnapshot2 : dataSnapshot1.getChildren()) {
                        String monthfire = dataSnapshot2.child("date").getValue().toString();
                        String state = dataSnapshot2.child("state").getValue().toString();
                        totalPrice = dataSnapshot2.child("bagprice").getValue().toString();
                        if (state.equals("past")) {
                            if (date.equals(monthfire)) {
                                addMoney = Integer.parseInt(totalPrice) + addMoney;
                            }
                            if (month.equals(monthfire.substring(5, 7))) {
                                addMontly = Integer.parseInt(totalPrice) + addMontly;
                            }
                            if (year.equals(monthfire.substring(0, 4))) {
                                addYearly = Integer.parseInt(totalPrice) + addYearly;
                            }
                        }
                    }
                }
                    collectMoney.setText(("₹"+addMoney));
                    MontlyMoney.setText(("₹"+addMontly));
                    YearlyMonely.setText(("₹"+addYearly));
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }
}