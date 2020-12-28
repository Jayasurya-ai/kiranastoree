package com.olokart.vendors;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;

public class EditPredefinedProducts extends AppCompatActivity {

    TextView product_name, product_quant;
    EditText product_price, product_price2, product_price3;
    String productName, setImageProduct,  subcat, saveCurrentDate, saveCurrentTime, productCat;
    ImageView imageView,edita, editb, editc;
    Button circularProgressButton, add, add2, button, button2, sub2, sub3;
    Spinner spinner, spinner2, spinner3;
    int itemQuant, itemQuant2 = 0, itemQuant3 = 0, productPrice, productPrice2 = 0, productPrice3 = 0;
    ArrayAdapter<CharSequence> arrayAdapter;
    Calendar calForDate;
    FirebaseAuth firebaseAuth;
    Spinner spin, spin2, spin3;
    EditText proQuant, proQuant2, proQuant3;
    String productfixed, productfixed2 = "0", productfixed3 = "0";
    LinearLayout  lin, lin2, lin3;
    DatabaseReference databaseReference;
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_editpredefined_products);

        Intent intent = getIntent();
        productCat = intent.getStringExtra("pcat");
        subcat = intent.getStringExtra("psubcat");
        productName = intent.getStringExtra("pname");
        setImageProduct = intent.getStringExtra("pimage");

       // goBack = findViewById(R.id.goBackproducts);
        product_name = findViewById(R.id.setpredefinedName);
        imageView = findViewById(R.id.setpredefinedImage);
        product_quant = findViewById(R.id.setproduct_quant);
        product_price = findViewById(R.id.setProductprice);
        product_price2 = findViewById(R.id.setProductprice2);
        product_price3 = findViewById(R.id.setProductprice3);
        circularProgressButton = findViewById(R.id.setProductname1);
        edita = findViewById(R.id.setpreProductsa);
        editb = findViewById(R.id.setpreProductsb);
        editc = findViewById(R.id.setpreProductsc);
        button = findViewById(R.id.setProductname2);
        button2 = findViewById(R.id.setProductname3);
        spinner = findViewById(R.id.setspin_addProduct);
        spinner2 = findViewById(R.id.setspin_addProduct2);
        spinner3 = findViewById(R.id.setspin_addProduct3);
        spin = findViewById(R.id.setFixedQuant);
        spin2 = findViewById(R.id.setFixedQuant2);
        spin3 = findViewById(R.id.setFixedQuant3);
        sub3 = findViewById(R.id.sethide_quant3);
        sub2 = findViewById(R.id.sethide_quant2);
        lin = findViewById(R.id.setnewlin1);
        lin2 = findViewById(R.id.setnewlin2);
        lin3 = findViewById(R.id.setnewlin3);
        add = findViewById(R.id.setquant);
        add2 = findViewById(R.id.setquant2);
        proQuant = findViewById(R.id.setProductQuant);
        proQuant2 = findViewById(R.id.setProductQuant2);
        proQuant3 = findViewById(R.id.setProductQuant3);

        firebaseAuth = FirebaseAuth.getInstance();
//            textCat.setText(itemQuant);
//
        databaseReference = FirebaseDatabase.getInstance().getReference("Seller Products")
                .child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child(productCat);

        databaseReference.child(subcat).child(productName).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                try {
                    String weight = snapshot.child("product_quant").getValue().toString();
                    String weighta = snapshot.child("product_quanta").getValue().toString();
                    String weightb = snapshot.child("product_quantb").getValue().toString();

                    String price = snapshot.child("actual_price").getValue().toString();
                    String pricea = snapshot.child("actual_pricea").getValue().toString();
                    String priceb = snapshot.child("actual_priceb").getValue().toString();

                    proQuant.setText(weight.substring(0, weight.length() - 2));
                    proQuant2.setText(weighta.substring(0, weighta.length() - 2));
                    proQuant3.setText(weightb.substring(0, weightb.length() - 2));

                    product_price.setText(price);
                    product_price2.setText(pricea);
                    product_price3.setText(priceb);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        //userRef = FirebaseDatabase.getInstance().getReference("Sellers").child(firebaseAuth.getCurrentUser().getUid());

        edita.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                proQuant.setText("");
                product_price.setText("");
                circularProgressButton.setText("SAVE");
            }
        });
        editb.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                proQuant2.setText("");
                product_price2.setText("");
                button.setText("SAVE");
            }
        });
        editc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                proQuant3.setText("");
                product_price3.setText("");
                button2.setText("SAVE");
            }
        });
//        goBack.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                if (!TextUtils.isEmpty(proQuant.getText().toString())) {
//                    Toast.makeText(PredefinedProducts.this, "Product added!", Toast.LENGTH_SHORT).show();
//                }
//                startActivity(new Intent(PredefinedProducts.this, AddProductsActivity.class)
//                .putExtra("category", productCat).putExtra("subcat", subcat));
//                PredefinedProducts.this.finish();
//            }
//        });
        product_name.setText(productName);
        Picasso.get().load(setImageProduct).into(imageView);
        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                lin2.setVisibility(View.VISIBLE);
            }
        });
        add2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                lin3.setVisibility(View.VISIBLE);
            }
        });
        sub2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                lin2.setVisibility(View.GONE);
            }
        });
        sub3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                lin3.setVisibility(View.GONE);
            }
        });

        arrayAdapter = ArrayAdapter.createFromResource(EditPredefinedProducts.this, R.array.spin_dimen, android.R.layout.simple_spinner_item);
        arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spin.setAdapter(arrayAdapter);
        spin.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                switch (position) {
                    case 0:
                        productfixed = "kg";
                        break;
                    case 1:
                        productfixed = "gm";
                        break;
                    case 2:
                        productfixed = "lt";
                        break;
                    case 3:
                        productfixed = "ml";
                        break;
                    case 4:
                        productfixed = "pc";
                        break;
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        arrayAdapter = ArrayAdapter.createFromResource(EditPredefinedProducts.this, R.array.spin_dimen, android.R.layout.simple_spinner_item);
        arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spin2.setAdapter(arrayAdapter);
        spin2.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                switch (position) {
                    case 0:
                        productfixed2 = "kg";
                        break;
                    case 1:
                        productfixed2 = "gm";
                        break;
                    case 2:
                        productfixed2 = "lt";
                        break;
                    case 3:
                        productfixed2 = "ml";
                        break;
                    case 4:
                        productfixed2 = "pc";
                        break;
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        arrayAdapter = ArrayAdapter.createFromResource(EditPredefinedProducts.this, R.array.spin_dimen, android.R.layout.simple_spinner_item);
        arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spin3.setAdapter(arrayAdapter);
        spin3.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                switch (position) {
                    case 0:
                        productfixed3 = "kg";
                        break;
                    case 1:
                        productfixed3 = "gm";
                        break;
                    case 2:
                        productfixed3 = "lt";
                        break;
                    case 3:
                        productfixed3 = "ml";
                        break;
                    case 4:
                        productfixed3 = "pc";
                        break;
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                switch (position) {
                    case 0:
                        itemQuant = 5;
                        break;
                    case 1:
                        itemQuant = 10;
                        break;
                    case 2:
                        itemQuant = 20;
                        break;
                    case 3:
                        itemQuant = 50;
                        break;
                    case 4:
                        itemQuant = 100;
                        break;
                    case 5:
                        itemQuant = 200;
                        break;
                    case 6:
                        itemQuant = 500;
                        break;

                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        spinner2.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                switch (position) {
                    case 0:
                        itemQuant2 = 5;
                        break;
                    case 1:
                        itemQuant2 = 10;
                        break;
                    case 2:
                        itemQuant2 = 20;
                        break;
                    case 3:
                        itemQuant2 = 50;
                        break;
                    case 4:
                        itemQuant2 = 100;
                        break;
                    case 5:
                        itemQuant2 = 200;
                        break;
                    case 6:
                        itemQuant2 = 500;
                        break;

                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        spinner3.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                switch (position) {
                    case 0:
                        itemQuant3 = 5;
                        break;
                    case 1:
                        itemQuant3 = 10;
                        break;
                    case 2:
                        itemQuant3 = 20;
                        break;
                    case 3:
                        itemQuant3 = 50;
                        break;
                    case 4:
                        itemQuant3 = 100;
                        break;
                    case 5:
                        itemQuant3 = 200;
                        break;
                    case 6:
                        itemQuant3 = 500;
                        break;

                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
//
//
//
//


        circularProgressButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                calForDate = Calendar.getInstance();
                SimpleDateFormat currentDate = new SimpleDateFormat("dd:mm:yyyy");
                saveCurrentDate = currentDate.format(calForDate.getTime());
                SimpleDateFormat currentTime = new SimpleDateFormat("hh:mm:ss:ms");
                saveCurrentTime = currentTime.format(calForDate.getTime());


                if (TextUtils.isEmpty(proQuant.getText().toString())) {
                    Snackbar.make(v, "Please enter Weight..", Snackbar.LENGTH_SHORT).show();
                }
                else if (TextUtils.isEmpty(product_price.getText().toString())) {
                    Snackbar.make(v, "Please enter  Price..", Snackbar.LENGTH_SHORT).show();
                }

                else {

                    productPrice = Integer.parseInt(product_price.getText().toString());

                    HashMap<String, Object> saveItem = new HashMap<>();
                    saveItem.put("product_name", productName);
                    saveItem.put("actual_price", String.valueOf(productPrice));
                    saveItem.put("product_image", setImageProduct);
                    saveItem.put("product_quant", proQuant.getText().toString() + productfixed);
                    saveItem.put("product_brand", "");
                    saveItem.put("product_descp", "");
                    saveItem.put("total_quantity", String.valueOf(itemQuant));
                    saveItem.put("actual_pricea", " ");
                    saveItem.put("product_quanta", " ");
                    saveItem.put("total_quantitya", "0");
                    saveItem.put("total_pricea", " ");
                    saveItem.put("actual_priceb", " ");
                    saveItem.put("product_quantb", " ");
                    saveItem.put("total_quantityb", "0");
                    saveItem.put("total_priceb", " ");
                    saveItem.put("product_cat", productCat);
                    saveItem.put("product_subcat", subcat);
                    saveItem.put("suid",FirebaseAuth.getInstance().getCurrentUser().getUid());
                    saveItem.put("product_state","activated");
                    saveItem.put("total_price", String.valueOf(Integer.parseInt(proQuant.getText().toString()) * productPrice));

                    databaseReference.child(subcat).child(productName).
                            updateChildren(saveItem);

                    circularProgressButton.setVisibility(View.VISIBLE);
                    circularProgressButton.setText("Saved");

                    proQuant.setCursorVisible(false);
                    product_price.setCursorVisible(false);
                }
                button.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (TextUtils.isEmpty(proQuant2.getText().toString())) {
                            Snackbar.make(v, "Please enter Weight..", Snackbar.LENGTH_SHORT).show();
                        }
                        else if (TextUtils.isEmpty(product_price2.getText().toString())) {
                            Snackbar.make(v, "Please enter price...", Snackbar.LENGTH_SHORT).show();
                        }
                        else if ((proQuant2.getText().toString()+productfixed2).equals(proQuant.getText().toString()+productfixed)) {
                            Snackbar.make(v, "Error You cant enter same Weight", Snackbar.LENGTH_SHORT).show();
                        }
                        else {

                            productPrice2 = Integer.parseInt(product_price2.getText().toString());

                            HashMap<String, Object> saveItem = new HashMap<>();
                            saveItem.put("actual_pricea", (product_price2.getText().toString()));
                            saveItem.put("product_quanta", proQuant2.getText().toString() + productfixed2);
                            saveItem.put("total_quantitya", String.valueOf(itemQuant2));
                            saveItem.put("total_pricea", String.valueOf(Integer.parseInt(proQuant2.getText().toString()) * Integer.parseInt(product_price2.getText().toString())));

                            databaseReference.child(subcat).child(productName).
                                    updateChildren(saveItem);

                            button.setVisibility(View.VISIBLE);
                            button.setText("Saved");

                            product_price2.setCursorVisible(false);

                            proQuant2.setCursorVisible(false);
                        }
                        button2.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                if (TextUtils.isEmpty(proQuant3.getText().toString())) {
                                    Snackbar.make(v, "Please enter weight..", Snackbar.LENGTH_SHORT).show();
                                }
                                else if (TextUtils.isEmpty(product_price3.getText().toString())) {
                                    Snackbar.make(v, "Please enter  Price..", Snackbar.LENGTH_SHORT).show();
                                }
                                else if ((proQuant3.getText().toString()+productfixed3).equals(proQuant2.getText().toString()+productfixed2)) {
                                    Snackbar.make(v, "Error You cant enter same Weight", Snackbar.LENGTH_SHORT).show();
                                }
                                else if ((proQuant3.getText().toString()+productfixed3).equals(proQuant.getText().toString()+productfixed)) {
                                    Snackbar.make(v, "Error You cant enter same Weight", Snackbar.LENGTH_SHORT).show();
                                }

                                else {
                                    productPrice3 = Integer.parseInt(product_price3.getText().toString());

                                    HashMap<String, Object> saveItem = new HashMap<>();
                                    saveItem.put("actual_priceb", (product_price3.getText().toString()));

                                    saveItem.put("product_quantb", proQuant3.getText().toString() + productfixed3);
//
                                    saveItem.put("total_quantityb", String.valueOf(itemQuant3));
//
                                    saveItem.put("total_priceb", String.valueOf(Integer.parseInt(proQuant3.getText().toString()) * Integer.parseInt(product_price3.getText().toString())));

                                    databaseReference.child(subcat).child(productName).
                                            updateChildren(saveItem);

                                    button2.setVisibility(View.VISIBLE);
                                    button2.setText("Saved");

                                    product_price3.setCursorVisible(false);
                                    proQuant3.setCursorVisible(false);

                                }
                            }
                        });
                    }
                });
            }
        });

    }
}