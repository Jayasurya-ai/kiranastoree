package com.olokart.vendors;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class AllProducts extends AppCompatActivity {
    private RecyclerView recyclerView;
    private DatabaseReference reference,userref, orderRef, userOrderRef;
    ArrayList<GetSet> getSets;
    OrdersPlacedAdapter adapter;
    TextView bagprice;
    TextView delivery, name, phone, paymode;
    String userid;
    String uphone;
    String datetime,state="new";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_all_products);
        bagprice=findViewById(R.id.bagrs);

        name = findViewById(R.id.deliveryUsernamenew);
        paymode = findViewById(R.id.homenewPaymode);
        phone = findViewById(R.id.deliveryUserphone);
        delivery = findViewById(R.id.deliveryUserAddrnnew);



        Intent i = getIntent();
        datetime = i.getStringExtra("orderid");
        userOrderRef = FirebaseDatabase.getInstance().getReference("User Orders");

        orderRef = FirebaseDatabase.getInstance().getReference("Orders").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child("Home Delivery");
        reference = FirebaseDatabase.getInstance().getReference("Seller Orders").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child("Home Delivery");



                        recyclerView = findViewById(R.id.ordersplacedRecycler);
                        recyclerView.setHasFixedSize(true);


                    getSets = new ArrayList<GetSet>();


                        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(AllProducts.this);
                        recyclerView.setLayoutManager(linearLayoutManager);
                        recyclerView.setAdapter(adapter);

                        reference.child(datetime).addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull  DataSnapshot dataSnapshot) {
                                getSets.clear();
                                       for (DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()) {
                                           GetSet h = dataSnapshot1.getValue(GetSet.class);
                                           getSets.add(h);
                                       }
                                                // Toast.makeText(AllProducts.this, ""+datetime, Toast.LENGTH_SHORT).show();

                                                orderRef.child(datetime).addValueEventListener(new ValueEventListener() {
                                                    @Override
                                                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                                                        String Bagprice = dataSnapshot.child("bagprice").getValue().toString();
                                                        String address = dataSnapshot.child("address").getValue().toString();
                                                        String namestr = dataSnapshot.child("uname").getValue().toString();
                                                        String phonestr = dataSnapshot.child("uphone").getValue().toString();

                                                        bagprice.setText("₹"+Bagprice);
                                                        name.setText(namestr);
                                                        phone.setText(phonestr);
                                                        delivery.setText(address);


                                                        userid = dataSnapshot.child("uuid").getValue().toString();

                                                        userref = FirebaseDatabase.getInstance().getReference("Users").child(userid);

                                                        userref.addValueEventListener(new ValueEventListener() {
                                                            @Override
                                                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                                                uphone = dataSnapshot.child("uphone").getValue().toString();


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

                                  //      }
                                adapter = new OrdersPlacedAdapter(AllProducts.this, getSets);
                                recyclerView.setAdapter(adapter);



                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError databaseError) {

                            }
                        });


                    }

//    private void mymessage() {
//
//
//        SmsManager smsManager=SmsManager.getDefault();
//        smsManager.sendTextMessage(uphone,null,"Hy Jayasurya" +uphone,null,null);
//        Toast.makeText(this, "Message sent succesful..", Toast.LENGTH_SHORT).show();
//    }

//    @Override
//    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
//        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
//        switch (requestCode){
//            case 0:
//                if(grantResults.length>=0 &&grantResults[0]== PackageManager.PERMISSION_GRANTED){
//                    mymessage();
//                }
//                else
//                {
//                    Toast.makeText(this, "You dont have permission", Toast.LENGTH_SHORT).show();
//                }
//
//                break;
//        }

//    }



}
