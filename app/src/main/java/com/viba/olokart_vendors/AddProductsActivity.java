package com.viba.olokart_vendors;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class AddProductsActivity extends AppCompatActivity {
    DatabaseReference reference, databaseReference;
    FirebaseAuth firebaseAuth;
    String category, subcat;
    TextView sname, szip, scity, pcat, psubCat;
    RecyclerView recyclerView;
    EditText Categoryser;

    ArrayList<GetSet> getSets;
    AddProductsrAdapter adapter;
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_products);

        Intent intent = getIntent();
        category = intent.getStringExtra("category");
        subcat = intent.getStringExtra("subcat");

        sname=findViewById(R.id.snameProducts);
        szip=findViewById(R.id.szipProducts);
        scity=findViewById(R.id.scityProducts);
        pcat = findViewById(R.id.ProductCat);
        psubCat = findViewById(R.id.ProductSubcat);
        Categoryser=findViewById(R.id.categorysear);


        firebaseAuth = FirebaseAuth.getInstance();
        reference=FirebaseDatabase.getInstance().getReference("Predefined Products").child(category).child(subcat);
        Categoryser.addTextChangedListener(new TextWatcher() {
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

        pcat.setText(category+" > ");
        psubCat.setText(subcat);

        recyclerView = findViewById(R.id.addProducts_recycler);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        getSets = new ArrayList<GetSet>();


        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                for (DataSnapshot dataSnapshot1: dataSnapshot.getChildren()) {
                    GetSet h = dataSnapshot1.getValue(GetSet.class);
                    getSets.add(h);

                }
                adapter = new AddProductsrAdapter(AddProductsActivity.this,getSets);
                recyclerView.setAdapter(adapter);
            }


            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }
        });
    }
    private void filter(String text) {
        try {
            ArrayList<GetSet> filteredList = new ArrayList<>();
            for (GetSet item : getSets) {
                if (item.getProduct_name().toLowerCase().contains(text.toLowerCase())) {
                    filteredList.add(item);
                }
            }
            adapter.filterList(filteredList);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    public void addNewProduct(View view) {
        startActivity(new Intent(AddProductsActivity.this, AddNewProducts.class).putExtra("category", category).putExtra("subcat",subcat));
    }
}