package com.viba.olokart_vendors;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.os.Bundle;
import android.view.View;
import android.widget.DatePicker;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Calendar;

public class Statistics extends AppCompatActivity {
    private DatePicker datePicker;
    private Calendar calendar;
    private TextView dateView, collectMoney,MontlyMoney,YearlyMonely;
    String monthstr, totalPrice;
    private int year, month, day;
    String date, daystr;

    TextView nearByCustomer;
    Double nearbyDis=6.00;
    DatabaseReference usercurrentRef, sellerRef, sellerOrders;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_statistics);
        collectMoney = findViewById(R.id.collectTotalMoney);
        dateView = (TextView) findViewById(R.id.textView3);
        MontlyMoney=findViewById(R.id.collectTotalMonth);
        YearlyMonely=findViewById(R.id.collectTotaYear);
        calendar = Calendar.getInstance();
        year = calendar.get(Calendar.YEAR);

        month = calendar.get(Calendar.MONTH);

        day = calendar.get(Calendar.DAY_OF_MONTH);
        showDate(year, month+1, day);
        Calendar calForDate = Calendar.getInstance();





    nearByCustomer = findViewById(R.id.countNearby);
    sellerOrders = FirebaseDatabase.getInstance().getReference("Seller Orders").child(FirebaseAuth.getInstance().getCurrentUser().getUid());
        sellerRef = FirebaseDatabase.getInstance().getReference("Sellers").child(FirebaseAuth.getInstance().getCurrentUser().getUid());
        usercurrentRef = FirebaseDatabase.getInstance().getReference("Users Location");

        recycler();
        sellerDate();
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

                                long count=0;
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
                                            count = count+1;
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

    @SuppressWarnings("deprecation")
    public void setDate(View view) {
        showDialog(999);
    }

    @Override
    protected Dialog onCreateDialog(int id) {
        if (id == 999) {
            return new DatePickerDialog(this,
                    myDateListener, year, month, day);

        }
        return null;
    }

    private DatePickerDialog.OnDateSetListener myDateListener = new
            DatePickerDialog.OnDateSetListener() {
                @Override
                public void onDateSet(DatePicker arg0,
                                      int arg1, int arg2, int arg3) {

                    showDate(arg1, arg2+1, arg3);
                    sellerDate();
                }
            };

    private void showDate(int year, int month, int day) {
        if (month==01) {
            monthstr = "Jan";
        }
       else if (month==02) {
            monthstr = "Feb";
        }
        else if (month==03) {
            monthstr = "Mar";
        }
        else if (month==04) {
            monthstr = "Apr";
        }
        else if (month==05) {
            monthstr = "May";
        }
        else if (month==06) {
            monthstr = "Jun";
        }
        else if (month==07) {
            monthstr = "Jul";
        }
        else if (month==8) {
            monthstr = "Aug";
        }
        else if (month==9) {
            monthstr = "Sep";
        }
        else if (month==10) {
            monthstr = "Oct";
        }
        else if (month==11) {
            monthstr = "Nov";
        }
        else if (month==12) {
            monthstr = "Dec";
        }
        if (day <= 9) {
            daystr = "0"+String.valueOf(day);
        }
        dateView.setText(new StringBuilder().append(day).append("-")
                .append(monthstr).append("-").append(year));

        date = daystr+"-"+monthstr+"-"+year;


    }
    public  void sellerDate() {

        sellerOrders.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                int addMoney =0;
                int addMontly=0;
                int addYearly=0;
                for (DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()) {
                    String monthfire = dataSnapshot1.child("date").getValue().toString();
                     totalPrice = dataSnapshot1.child("total_price").getValue().toString();
                    if (date.equals(monthfire)) {
                        addMoney = Integer.parseInt(totalPrice) + addMoney;
                    }
                    if(date.substring(3,6).equals(monthfire.substring(3,6))){
                        addMontly = Integer.parseInt(totalPrice) + addMontly;
                    }
                    if(date.substring(7,11).equals(monthfire.substring(7,11))){
                        addYearly=Integer.parseInt(totalPrice)+addYearly;
                    }
                }
                collectMoney.setText(String.valueOf(addMoney+"Rs"));
                MontlyMoney.setText(String.valueOf(addMontly+"Rs"));
                YearlyMonely.setText(String.valueOf(addYearly+"Rs"));
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }
}