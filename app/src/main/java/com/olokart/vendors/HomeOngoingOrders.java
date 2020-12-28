package com.olokart.vendors;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.widget.ProgressBar;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class HomeOngoingOrders extends AppCompatActivity {

    private RecyclerView recyclerView;
    ProgressBar progressBar;
    private DatabaseReference reference;
    ArrayList<GetSet> getSets;
    OngoingOrderAdapter adapter;
    String state="ongoing", optionmode;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_ongoing_orders);

        recyclerView = findViewById(R.id.ongoingOrderRecycler);
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

                for(DataSnapshot dataSnapshot1:dataSnapshot.getChildren()){
                            String statestr=dataSnapshot1.child("state").getValue().toString();
                            if(statestr.equals(state)) {

                                GetSet h = dataSnapshot1.getValue(GetSet.class);
                                getSets.add(h);

                    }
                    adapter = new OngoingOrderAdapter(HomeOngoingOrders.this, getSets);
                    recyclerView.setAdapter(adapter);
                }
                // getSets.clear();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });




    }
}