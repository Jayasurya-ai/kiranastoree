package com.olokart.vendors;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class HomeNewOrders extends AppCompatActivity {

    private RecyclerView recyclerView;
    private DatabaseReference reference;
    ArrayList<GetSet> getSets;
    HomedeliveryNewOrderAdapter adapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.newongoingpast);
//
//

        recyclerView = findViewById(R.id.newOrderRecycler);
        recyclerView.setHasFixedSize(true);
        getSets = new ArrayList<GetSet>();

        reference = FirebaseDatabase.getInstance().getReference("Orders").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child("Home Delivery");
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setAdapter(adapter);

        reference.addValueEventListener(new ValueEventListener() {
                                            @Override
                                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                                getSets.clear();
                                                for (DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()) {

                                                    String state = dataSnapshot1.child("state").getValue().toString();
                                                    if (state.equals("new")) {
                                                        GetSet h = dataSnapshot1.getValue(GetSet.class);
                                                        getSets.add(h);
                                                    }
                                                }
                                                adapter = new HomedeliveryNewOrderAdapter(HomeNewOrders.this, getSets);
                                                recyclerView.setAdapter(adapter);


                                            }

                                                        @Override
                                                        public void onCancelled(@NonNull DatabaseError databaseError) {

                                                        }
                                                    });





//                    if(getSets.isEmpty()){
//                        productHome.setVisibility(View.VISIBLE);
//                        recyclerView.setVisibility(View.GONE);
//                        progress.setVisibility(View.GONE);
//                    }
//                    else{
//                        recyclerView.setVisibility(View.VISIBLE);
//                        productHome.setVisibility(View.GONE);
//                    }






    }
}