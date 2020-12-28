package com.olokart.vendors;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.github.ybq.android.spinkit.sprite.Sprite;
import com.github.ybq.android.spinkit.style.Circle;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class ProductsAdded extends AppCompatActivity {

    private RecyclerView recyclerView;
    EditText editText;
    TextView productHome;
    FirebaseAuth firebaseAuth;
    private DatabaseReference reference;
    ArrayList<GetSet> getSets;
    FrameLayout frameLayout;
    HomeAdapter adapter;
    ProgressBar progress;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_products_added);

        editText = findViewById(R.id.EditText);
        frameLayout = findViewById(R.id.frameProducts);
        progress=findViewById(R.id.spin_kit);
        Sprite foldingCube = new Circle();
        progress.setIndeterminateDrawable(foldingCube);

        firebaseAuth = FirebaseAuth.getInstance();
        reference = FirebaseDatabase.getInstance().getReference().child("Seller Products").child(firebaseAuth.getCurrentUser().getUid());
        recyclerView = findViewById(R.id.seller_recycler);
        productHome = findViewById(R.id.NoProducts);
        recyclerView.setHasFixedSize(true);


        editText.addTextChangedListener(new TextWatcher() {
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

        getSets = new ArrayList<GetSet>();

            reference.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    getSets.clear();
                    try {

                        for (DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()) {
                            for (DataSnapshot dataSnapshot2 : dataSnapshot1.getChildren()) {
                                for (DataSnapshot dataSnapshot3 : dataSnapshot2.getChildren()) {
                                    GetSet h = dataSnapshot3.getValue(GetSet.class);
                                    getSets.add(h);
                                    progress.setVisibility(View.GONE);

                                }
                            }
                        }

                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    adapter = new HomeAdapter(ProductsAdded.this, getSets);
                    recyclerView.setAdapter(adapter);
                    if(getSets.isEmpty()){
                        productHome.setVisibility(View.VISIBLE);
                        recyclerView.setVisibility(View.GONE);
                        progress.setVisibility(View.GONE);
                    }
                    else{
                        recyclerView.setVisibility(View.VISIBLE);
                        productHome.setVisibility(View.GONE);
                    }


                }



                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {
                    progress.setVisibility(View.GONE);
                }
            });


        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setAdapter(adapter);

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
}