package com.olokart.vendors;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class AllPastOrders extends AppCompatActivity {
    private RecyclerView recyclerView;
    private DatabaseReference reference,userref, orderRef, userOrderRef;
    ArrayList<GetSet> getSets;
    OrdersPlacedAdapter adapter;
    TextView bagprice, delivery, name, phone, paymode;
    String userid;
    String uphone;
    String orderid,state="past";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_all_past_orders);
        bagprice=findViewById(R.id.bagrs3);


        delivery = findViewById(R.id.deliveryUserAddrongoing);
        paymode = findViewById(R.id.homepastPaymode);
        name = findViewById(R.id.deliverUsernameonpast);
        phone = findViewById(R.id.deliveryUserphoneonpast);
        Intent intent = getIntent();
        orderid = intent.getStringExtra("orderid");

        recyclerView = findViewById(R.id.ordersplacedRecycler3);
        recyclerView.setHasFixedSize(true);


        getSets = new ArrayList<GetSet>();

        userOrderRef = FirebaseDatabase.getInstance().getReference("User Orders");

        orderRef = FirebaseDatabase.getInstance().getReference("Orders").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child("Home Delivery");

        reference = FirebaseDatabase.getInstance().getReference("Seller Orders").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child("Home Delivery");
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(AllPastOrders.this);
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setAdapter(adapter);

        reference.child(orderid).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                getSets.clear();
                for ( DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()) {

                        GetSet h = dataSnapshot1.getValue(GetSet.class);
                        getSets.add(h);


                    orderRef.child(orderid).addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                            String Bagprice=dataSnapshot.child("bagprice").getValue().toString();
                            String addressstr=dataSnapshot.child("address").getValue().toString();
                            String namestr=dataSnapshot.child("uname").getValue().toString();
                            String phonestr=dataSnapshot.child("uphone").getValue().toString();
                            String paystr=dataSnapshot.child("pmode").getValue().toString();

                            paymode.setText(paystr);
                            bagprice.setText("₹"+Bagprice);
                            delivery.setText(addressstr);
                            name.setText(namestr);
                            phone.setText(phonestr);

                            userid=dataSnapshot.child("uuid").getValue().toString();
                            userref=FirebaseDatabase.getInstance().getReference("Users").child(userid);
//                            userref.addValueEventListener(new ValueEventListener() {
//                                @Override
//                                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
//                                    uphone=dataSnapshot.child("uphone").getValue().toString();
//
//
//                                }
//
//                                @Override
//                                public void onCancelled(@NonNull DatabaseError databaseError) {
//
//                                }
//                            });

                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {

                        }
                    });


                }

                adapter = new OrdersPlacedAdapter(AllPastOrders.this, getSets);
                recyclerView.setAdapter(adapter);

            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });






    }


}