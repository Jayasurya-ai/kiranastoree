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

public class PickupNewAllproducts extends AppCompatActivity {
    private RecyclerView recyclerView;
    private DatabaseReference reference,orderRef, userref, userOrderRef;
    ArrayList<GetSet> getSets;
    OrdersPlacedAdapter adapter;
    TextView bagprice, name, phone, paymode;
    String userid;
    String uphone;
    String orderid,state="new";



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pickup_new_allproducts);

        Intent intent = getIntent();
        orderid = intent.getStringExtra("orderid");

        name = findViewById(R.id.pickupUsernamenew);
        phone = findViewById(R.id.pickupUserphone);
        bagprice=findViewById(R.id.pickbagrs);
        paymode = findViewById(R.id.pickupnewPaymode);






        recyclerView = findViewById(R.id.pickordersplacedRecycler);
        recyclerView.setHasFixedSize(true);


        getSets = new ArrayList<GetSet>();


        userOrderRef = FirebaseDatabase.getInstance().getReference("User Orders");

        orderRef = FirebaseDatabase.getInstance().getReference("Orders").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child("Pickup");

       // userref = FirebaseDatabase.getInstance().getReference("Users");
        reference = FirebaseDatabase.getInstance().getReference("Seller Orders").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child("Pickup");
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(PickupNewAllproducts.this);
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setAdapter(adapter);

        reference.child(orderid).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull final DataSnapshot dataSnapshot) {

                getSets.clear();
                try {

                    for (final DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()) {
                        GetSet h = dataSnapshot1.getValue(GetSet.class);
                        getSets.add(h);

                        orderRef.child(orderid).addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                                String Bagprice = dataSnapshot.child("bagprice").getValue().toString();
                                String namestr = dataSnapshot.child("uname").getValue().toString();
                                String phonestr = dataSnapshot.child("uphone").getValue().toString();
                                String paystr = dataSnapshot.child("pmode").getValue().toString();

                                bagprice.setText("₹" + Bagprice);
                                paymode.setText(paystr);

                                userid = dataSnapshot.child("uuid").getValue().toString();
                                name.setText(namestr);
                                phone.setText(phonestr);

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


                    }
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
                adapter = new OrdersPlacedAdapter(PickupNewAllproducts.this, getSets);
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

    private void sendNotification() {


    }
    private void sendNotificationCancel() {

           }
}