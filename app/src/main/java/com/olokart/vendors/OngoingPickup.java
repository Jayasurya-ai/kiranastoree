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

public class OngoingPickup extends AppCompatActivity {
    private RecyclerView recyclerView;
    private DatabaseReference reference;
    ArrayList<GetSet> getSets;
    PickOngoingOrderAdapter adapter;
    String state="ongoing";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ongoing_pickup);


        recyclerView = findViewById(R.id.pickongoingOrderRecycler);
        recyclerView.setHasFixedSize(true);
        getSets = new ArrayList<GetSet>();

        reference = FirebaseDatabase.getInstance().getReference("Orders").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child("Pickup");
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setAdapter(adapter);

        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                getSets.clear();
                for(DataSnapshot dataSnapshot1:dataSnapshot.getChildren()) {
                    String statestr = dataSnapshot1.child("state").getValue().toString();
                    if (statestr.equals(state)) {

                        GetSet h = dataSnapshot1.getValue(GetSet.class);
                        getSets.add(h);
                    }
                }
                    adapter = new PickOngoingOrderAdapter(OngoingPickup.this, getSets);
                    recyclerView.setAdapter(adapter);
                // getSets.clear();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });




    }


}