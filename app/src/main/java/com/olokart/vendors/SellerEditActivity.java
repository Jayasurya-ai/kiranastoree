package com.olokart.vendors;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.HashMap;

public class SellerEditActivity extends AppCompatActivity {

    EditText product_brand, product_descp;
    String prodcutName;
    DatabaseReference databaseReference;
    FirebaseAuth firebaseAuth;
    Button saveProduct;
    ImageView imageView;
    TextView product_name;
    String pName, pImage, pQuant, tQuant, tPrice, aPrice;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_seller_edit);

        Intent intent = getIntent();
        product_name=findViewById(R.id.edit_product_name);
        prodcutName = intent.getStringExtra("product_name");

        firebaseAuth = FirebaseAuth.getInstance();
        product_brand = findViewById(R.id.product_brand);
        product_descp = findViewById(R.id.product_descp);
        saveProduct = findViewById(R.id.Edit_btn);
        imageView=findViewById(R.id.edit_product_image);
        databaseReference = FirebaseDatabase.getInstance().getReference("Seller Products").child(firebaseAuth.getCurrentUser().getUid()).child(prodcutName);

        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                try {

                    pName = dataSnapshot.child("product_name").getValue().toString();
                    pImage = dataSnapshot.child("product_image").getValue().toString();
                    pQuant = dataSnapshot.child("product_quant").getValue().toString();
                    aPrice = dataSnapshot.child("actual_price").getValue().toString();
                    tPrice = dataSnapshot.child("total_price").getValue().toString();
                    tQuant = dataSnapshot.child("total_quantity").getValue().toString();
                    product_name.setText(pName);
                    Picasso.get().load(pImage).placeholder(R.drawable.shopimage).into(imageView);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        saveProduct.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                HashMap<String,Object> saveMap = new HashMap<>();
                saveMap.put("product_descp", product_descp.getText().toString());
                saveMap.put("product_brand", product_brand.getText().toString());
                saveMap.put("product_quant", pQuant);
                saveMap.put("total_quantity", tQuant);
                saveMap.put("product_name", pName);
                saveMap.put("product_image", pImage);
                saveMap.put("actual_price", aPrice);
                saveMap.put("total_price", tPrice);

                databaseReference.updateChildren(saveMap);
                startActivity(new Intent(SellerEditActivity.this, HomeActivity.class));
                finish();
            }
        });
    }

}