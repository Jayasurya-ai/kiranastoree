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

public class PickPastAllproducts extends AppCompatActivity {
    private RecyclerView recyclerView;
    private DatabaseReference reference,userref, orserRef, userOrderRef;
    ArrayList<GetSet> getSets;
    OrdersPlacedAdapter adapter;
    TextView bagprice, name, phone, paymode;
    String userid;
    String uphone;
    String orderid,state="past";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pick_past_allproducts);

        bagprice=findViewById(R.id.pbagrs3);
        paymode = findViewById(R.id.pickuppastPaymode);

        name = findViewById(R.id.pickupUsernamepast);
        phone = findViewById(R.id.pickupUserphonepast);
        Intent i = getIntent();
        orderid = i.getStringExtra("orderid");





        recyclerView = findViewById(R.id.pordersplacedRecycler3);
        recyclerView.setHasFixedSize(true);


        getSets = new ArrayList<GetSet>();


        userOrderRef = FirebaseDatabase.getInstance().getReference("User Orders");

        orserRef = FirebaseDatabase.getInstance().getReference("Orders").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child("Pickup");

        reference = FirebaseDatabase.getInstance().getReference("Seller Orders").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child("Pickup");
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(PickPastAllproducts.this);
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setAdapter(adapter);

        reference.child(orderid).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull final DataSnapshot dataSnapshot) {
                getSets.clear();
                for (final DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()){

                               // Toast.makeText(PickPastAllproducts.this, ""+statestr+state, Toast.LENGTH_SHORT).show();
                                GetSet h = dataSnapshot1.getValue(GetSet.class);
                                getSets.add(h);

                    orserRef.child(orderid).addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                            String Bagprice = dataSnapshot.child("bagprice").getValue().toString();
                            String namestr = dataSnapshot.child("uname").getValue().toString();
                            String phonestr = dataSnapshot.child("uphone").getValue().toString();
                            String paystr = dataSnapshot.child("pmode").getValue().toString();

                            bagprice.setText("₹"+Bagprice);
                            paymode.setText(paystr);
                            name.setText(namestr);
                            phone.setText(phonestr);


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

                }
                adapter = new OrdersPlacedAdapter(PickPastAllproducts.this, getSets);
                recyclerView.setAdapter(adapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });






    }
}