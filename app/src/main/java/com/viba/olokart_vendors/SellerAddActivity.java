package com.viba.olokart_vendors;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.TextView;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class SellerAddActivity extends AppCompatActivity {

    TextView sname, szip, scity;
    DatabaseReference databaseReference, reference;
    RecyclerView recyclerView;
    ArrayList<GetSet> getSets;
    CategoryAdapter adapter;
    EditText Categorysearch;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add);
        sname=findViewById(R.id.snameCat);
        szip=findViewById(R.id.szipCat);
        scity=findViewById(R.id.scityCat);
        Categorysearch=findViewById(R.id.category_search);
        reference = FirebaseDatabase.getInstance().getReference("All Categories");

        databaseReference= FirebaseDatabase.getInstance().getReference("Sellers").child(FirebaseAuth.getInstance().getCurrentUser().getUid());
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                String snamestr=dataSnapshot.child("sname").getValue().toString();
                String szipstr=dataSnapshot.child("szip").getValue().toString();
                String scitystr=dataSnapshot.child("scity").getValue().toString();
                sname.setText(snamestr);
                scity.setText(scitystr+" - ");
                szip.setText(szipstr);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
        Categorysearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                filter(s.toString());

            }
        });
        recyclerView = findViewById(R.id.add_cat_recycler);
        recyclerView.setHasFixedSize(true);

        getSets = new ArrayList<GetSet>();


        reference.addValueEventListener(new ValueEventListener() {

            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                for (DataSnapshot dataSnapshot1: dataSnapshot.getChildren()) {
                        GetSet h = dataSnapshot1.getValue(GetSet.class);
                        getSets.add(h);
                }
                adapter = new CategoryAdapter(SellerAddActivity.this,getSets);
                recyclerView.setAdapter(adapter);
            }


            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }
        });

        GridLayoutManager linearLayoutManager = new GridLayoutManager(this, 2, RecyclerView.VERTICAL, false);
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setAdapter(adapter);

        BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.nav_add);
        navigation.setSelectedItemId(R.id.navigation_add);
        navigation.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.navigation_home:
                        Intent intentCat=new Intent(SellerAddActivity.this,HomeActivity.class);
                        startActivity(intentCat);
                        overridePendingTransition(0, 0);
                        break;
                    case R.id.navigation_add:
                        break;
                    case R.id.navigation_orders:
                        Intent intent=new Intent(SellerAddActivity.this,Notification.class);
                        startActivity(intent);
                        overridePendingTransition(0, 0);
                        break;
                }
                return false;
            }
        });

    }
    private void filter(String text) {
        try {
            ArrayList<GetSet> filteredList = new ArrayList<>();
            for (GetSet item : getSets) {
                if (item.getPcatname().toLowerCase().contains(text.toLowerCase())) {
                    filteredList.add(item);
                }
            }
            adapter.filterList(filteredList);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}