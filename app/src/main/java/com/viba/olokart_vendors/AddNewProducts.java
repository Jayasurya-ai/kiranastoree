package com.viba.olokart_vendors;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;

public class AddNewProducts extends AppCompatActivity {

    EditText product_name;
    TextView product_quant;
    EditText product_price, product_price2, product_price3;
    String setImageProduct, sphone, subcat, simage, saveCurrentDate, saveCurrentTime, productQuant, sLat, sLong, scity, sname, productCat;
    ImageView imageView;
    Button circularProgressButton, add, add2, button, button2, sub2, sub3;
    Spinner spinner, spinner2, spinner3, spinCat;
    ProgressDialog progressDialog;
    int itemQuant, itemQuant2 = 0, itemQuant3 = 0, productPrice, productPrice2 = 0, productPrice3 = 0;
    ArrayAdapter<CharSequence> arrayAdapter;
    Calendar calForDate;
    ArrayList<String> arrayList;
    private static final int GalleryPick = 1;
    private Uri ImageUri;
    private String downloadImageUrl;
    DatabaseReference databaseReference, sellerRef;
    private StorageReference shopImagesRef;

    FirebaseAuth firebaseAuth;
    Spinner spin, spin2, spin3;
    EditText proQuant, proQuant2, proQuant3;
    TextView textCat;
    String productfixed, productfixed2 = "0", productfixed3 = "0";
    LinearLayout linearLayout, lin, lin2, lin3;
    DatabaseReference userRef, categoryRef;
    StorageReference storageReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_new_products);
        storageReference= FirebaseStorage.getInstance().getReference("Product_image");


        Intent intent = getIntent();
        productCat = intent.getStringExtra("category");
        subcat = intent.getStringExtra("subcat");
        setImageProduct = intent.getStringExtra("pimage");
        product_name = findViewById(R.id.addnewName);
        imageView = findViewById(R.id.addnewImage);
        product_quant = findViewById(R.id.add_product_new_quant);
        product_price = findViewById(R.id.edit_addNewProductprice);
        product_price2 = findViewById(R.id.edit_addNewProductprice2);
        product_price3 = findViewById(R.id.edit_addNewProductprice3);
        circularProgressButton = findViewById(R.id.check_addNewProductname1);
        button = findViewById(R.id.check_addNewProductname2);
        button2 = findViewById(R.id.check_addNewProductname3);
        spinner = findViewById(R.id.spin_addNewProduct);
        spinner2 = findViewById(R.id.spin_addNewProduct2);
        spinner3 = findViewById(R.id.spin_addNewProduct3);
        spin = findViewById(R.id.NewFixedQuant);
        spin2 = findViewById(R.id.NewFixedQuant2);
        spin3 = findViewById(R.id.NewFixedQuant3);
        sub3 = findViewById(R.id.hide_new_quant3);
        sub2 = findViewById(R.id.hide_new_quant2);
        lin = findViewById(R.id.newlinProduct);
        lin2 = findViewById(R.id.newlinProducts2);
        lin3 = findViewById(R.id.newProductlin3);
        add = findViewById(R.id.add_new_quant);
        add2 = findViewById(R.id.add_new_quant2);
        proQuant = findViewById(R.id.ProductNewQuant);
        proQuant2 = findViewById(R.id.NewProductQuant2);
        proQuant3 = findViewById(R.id.NewProductQuant3);
        ProgressDialog progressDialog=new ProgressDialog(this);

        firebaseAuth = FirebaseAuth.getInstance();
//            textCat.setText(itemQuant);
//
        databaseReference = FirebaseDatabase.getInstance().getReference("Seller Products").child(
                firebaseAuth.getCurrentUser().getUid());

        //userRef = FirebaseDatabase.getInstance().getReference("Sellers").child(firebaseAuth.getCurrentUser().getUid());

        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                OpenGallery();
            }

        });

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

        arrayAdapter = ArrayAdapter.createFromResource(AddNewProducts.this, R.array.spin_dimen, android.R.layout.simple_spinner_item);
        arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spin.setAdapter(arrayAdapter);
        spin.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                switch (position) {
                    case 0:
                        productfixed = "gm";
                        break;
                    case 1:
                        productfixed = "kg";
                        break;
                    case 2:
                        productfixed = "lt";
                        break;
                    case 3:
                        productfixed = "ml";
                        break;
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        arrayAdapter = ArrayAdapter.createFromResource(AddNewProducts.this, R.array.spin_dimen, android.R.layout.simple_spinner_item);
        arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spin2.setAdapter(arrayAdapter);
        spin2.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                switch (position) {
                    case 0:
                        productfixed2 = "gm";
                        break;
                    case 1:
                        productfixed2 = "kg";
                        break;
                    case 2:
                        productfixed2 = "lt";
                        break;
                    case 3:
                        productfixed2 = "ml";
                        break;
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        arrayAdapter = ArrayAdapter.createFromResource(AddNewProducts.this, R.array.spin_dimen, android.R.layout.simple_spinner_item);
        arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spin3.setAdapter(arrayAdapter);
        spin3.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                switch (position) {
                    case 0:
                        productfixed3 = "gm";
                        break;
                    case 1:
                        productfixed3 = "kg";
                        break;
                    case 2:
                        productfixed3 = "lt";
                        break;
                    case 3:
                        productfixed3 = "ml";
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
                        itemQuant = 1;
                        break;
                    case 1:
                        itemQuant = 2;
                        break;
                    case 2:
                        itemQuant = 3;
                        break;
                    case 3:
                        itemQuant = 4;
                        break;
                    case 4:
                        itemQuant = 5;
                        break;
                    case 5:
                        itemQuant = 5;
                        break;
                    case 6:
                        itemQuant = 6;
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
                        itemQuant2 = 1;
                        break;
                    case 1:
                        itemQuant2 = 2;
                        break;
                    case 2:
                        itemQuant2 = 3;
                        break;
                    case 3:
                        itemQuant2 = 4;
                        break;
                    case 4:
                        itemQuant2 = 5;
                        break;
                    case 5:
                        itemQuant2 = 5;
                        break;
                    case 6:
                        itemQuant2 = 6;
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
                        itemQuant3 = 1;
                        break;
                    case 1:
                        itemQuant3 = 2;
                        break;
                    case 2:
                        itemQuant3 = 3;
                        break;
                    case 3:
                        itemQuant3 = 4;
                        break;
                    case 4:
                        itemQuant3 = 5;
                        break;
                    case 5:
                        itemQuant3 = 5;
                        break;
                    case 6:
                        itemQuant3 = 6;
                        break;

                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        circularProgressButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ValidateProductData();

                calForDate = Calendar.getInstance();
                SimpleDateFormat currentDate = new SimpleDateFormat("dd:mm:yyyy");
                saveCurrentDate = currentDate.format(calForDate.getTime());
                SimpleDateFormat currentTime = new SimpleDateFormat("hh:mm:ss:ms");
                saveCurrentTime = currentTime.format(calForDate.getTime());


                if (TextUtils.isEmpty(proQuant.getText().toString())) {
                    Snackbar.make(v, "Please enter Qty..", Snackbar.LENGTH_SHORT).show();
                }
                else if (TextUtils.isEmpty(product_price.getText().toString())) {
                    Snackbar.make(v, "Please enter  Price..", Snackbar.LENGTH_SHORT).show();
                } else {
                    productPrice = Integer.parseInt(product_price.getText().toString());

                    HashMap<String, Object> saveItem = new HashMap<>();
                    saveItem.put("product_name", product_name.getText().toString());
                    saveItem.put("actual_price", String.valueOf(productPrice));
                    saveItem.put("product_image", setImageProduct);
                    saveItem.put("product_quant", proQuant.getText().toString() + productfixed);
                    saveItem.put("product_brand", "");
                    saveItem.put("product_descp", "");
                    saveItem.put("total_quantity", String.valueOf(itemQuant));
                    saveItem.put("product_cat", productCat);
                    saveItem.put("product_subcat", subcat);
                    saveItem.put("suid", FirebaseAuth.getInstance().getCurrentUser().getUid());
                    saveItem.put("total_price", String.valueOf(Integer.parseInt(proQuant.getText().toString()) * productPrice));

                    databaseReference.child(product_name.getText().toString()).
                            updateChildren(saveItem);

                    circularProgressButton.setVisibility(View.VISIBLE);
                    circularProgressButton.setText("Added");
                }
                button.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        ValidateProductData();
                        if (TextUtils.isEmpty(proQuant2.getText().toString())) {
                            Snackbar.make(v, "Please enter Qty..", Snackbar.LENGTH_SHORT).show();
                        }
                        else if (TextUtils.isEmpty(product_price2.getText().toString())) {
                            Snackbar.make(v, "Please enter  Price..", Snackbar.LENGTH_SHORT).show();
                        }
                        else {

                            productPrice2 = Integer.parseInt(product_price2.getText().toString());

                            HashMap<String, Object> saveItem = new HashMap<>();
                            saveItem.put("actual_pricea", (product_price2.getText().toString()));
                            saveItem.put("product_quanta", proQuant2.getText().toString() + productfixed2);
                            saveItem.put("total_quantitya", String.valueOf(itemQuant2));
                            saveItem.put("total_pricea", String.valueOf(Integer.parseInt(proQuant2.getText().toString()) * Integer.parseInt(product_price2.getText().toString())));

                            databaseReference.child(product_name.getText().toString()).
                                    updateChildren(saveItem);

                            button.setVisibility(View.VISIBLE);
                            button.setText("Added");


                        }
                        button2.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                ValidateProductData();
                                if (TextUtils.isEmpty(proQuant3.getText().toString())) {
                                    Snackbar.make(v, "Please enter Qty..", Snackbar.LENGTH_SHORT).show();
                                }
                                else if (TextUtils.isEmpty(product_price3.getText().toString())) {
                                    Snackbar.make(v, "Please enter  Price..", Snackbar.LENGTH_SHORT).show();
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

                                    databaseReference.child(product_name.getText().toString()).
                                            updateChildren(saveItem);

                                    button2.setVisibility(View.VISIBLE);
                                    button2.setText("Added");


                                }
                            }
                        });
                    }
                });
            }
        });

    }

    private void OpenGallery() {
        Intent galleryIntent = new Intent();
        galleryIntent.setAction(Intent.ACTION_GET_CONTENT);
        galleryIntent.setType("image/*");
        startActivityForResult(galleryIntent, GalleryPick);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == GalleryPick && resultCode == RESULT_OK && data != null) {
            ImageUri = data.getData();
            imageView.setVisibility(View.VISIBLE);
            imageView.setImageURI(ImageUri);

        }
    }
    //
    private void ValidateProductData() {
        if (ImageUri != null) {
            StoreProductInformation();

        } else {
            Toast.makeText(this, "Store Image is Mandatory", Toast.LENGTH_SHORT).show();

        }
    }

    private void StoreProductInformation() {

        final StorageReference filePath = storageReference.child(ImageUri.getLastPathSegment() +".jpg");
        final UploadTask uploadTask = filePath.putFile(ImageUri);
        uploadTask.addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                String message = e.toString();
                Toast.makeText(AddNewProducts.this, "Error:" + message, Toast.LENGTH_SHORT).show();
            }
        }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                Task<Uri> urlTask = uploadTask.continueWithTask(new Continuation<UploadTask.TaskSnapshot, Task<Uri>>() {
                    @Override
                    public Task<Uri> then(@NonNull Task<UploadTask.TaskSnapshot> task) throws Exception {
                        if (!task.isSuccessful()) {
                            throw task.getException();
                        }
                        downloadImageUrl = filePath.getDownloadUrl().toString();
                        return filePath.getDownloadUrl();
                    }
                }).addOnCompleteListener(new OnCompleteListener<Uri>() {
                    @Override
                    public void onComplete(@NonNull Task<Uri> task) {
                        if (task.isSuccessful()) {

                            downloadImageUrl = task.getResult().toString();
                            saveProductInfoToDatabase();
                        }
                    }
                });
            }
        });

    }
    private void saveProductInfoToDatabase() {
        final HashMap<String, Object> productMap = new HashMap<>();
        if (downloadImageUrl.isEmpty()) {
            Toast.makeText(this, "upload image", Toast.LENGTH_SHORT).show();
        } else {
            productMap.put("simage", downloadImageUrl);
            databaseReference.child(product_name.getText().toString()).updateChildren(productMap).addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    if (task.isSuccessful()) {

                    } else {

                        String message = task.getException().toString();
                        Toast.makeText(AddNewProducts.this, "Error Occurred!" + message, Toast.LENGTH_SHORT).show();
                    }
                }
            });
        }
    }


    }
