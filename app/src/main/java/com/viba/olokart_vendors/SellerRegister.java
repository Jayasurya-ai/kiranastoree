package com.viba.olokart_vendors;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;

public class SellerRegister extends AppCompatActivity {
    private TextView sellerLoginBegin;
    private EditText nameInput, emailInput, contactInput;
    private Button registerButton;
    private ProgressDialog loadingBar;
    private ImageView InputshopImage;
    private static final int GalleryPick = 1;
    private Uri ImageUri;
    private String downloadImageUrl;
    DatabaseReference databaseReference;
    private StorageReference shopImagesRef;
    FloatingActionButton floatingActionButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_seller_register);
        overridePendingTransition(0,0);
        View relativeLayout=findViewById(R.id.login_container);
        Animation animation= AnimationUtils.loadAnimation(this,android.R.anim.fade_in);
        relativeLayout.startAnimation(animation);

        floatingActionButton = findViewById(R.id.upload_image);
        sellerLoginBegin =findViewById(R.id.seller_aleready_have_acnt_btn);
        nameInput = findViewById(R.id.seller_name);
        emailInput = findViewById(R.id.seller_email);
        contactInput = findViewById(R.id.seller_contact);
        InputshopImage = findViewById(R.id.select_shop_image);

        loadingBar = new ProgressDialog(this);
        registerButton = findViewById(R.id.seller_register_btn);
        shopImagesRef = FirebaseStorage.getInstance().getReference("Sellers Data");
        sellerLoginBegin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(SellerRegister.this,SellerLoginActivity.class));
            }
        });
        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                OpenGallery();
            }

        });
        registerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                if (ImageUri != null) {

                    final String name = nameInput.getText().toString();
                    final String email = emailInput.getText().toString();
                    final String contact = contactInput.getText().toString();
                    if (!name.equals("") && !email.equals("") && !contact.equals("")) {
                        loadingBar.setTitle("Sending confirmation");
                        loadingBar.setMessage("Please wait...");
                        loadingBar.setCanceledOnTouchOutside(false);
                        loadingBar.show();
                        ValidateProductData();

                        Calendar calendar = Calendar.getInstance();
                        SimpleDateFormat currentDate = new SimpleDateFormat("dd:mm:yyyy");
                        String saveCurrentDate = currentDate.format(calendar.getTime());
                        SimpleDateFormat currentTime = new SimpleDateFormat("hh:mm:ss:ms");
                        String saveCurrentTime = currentTime.format(calendar.getTime());
                        databaseReference = FirebaseDatabase.getInstance().getReference("Sellers Data").child(name);

                        String pass = name.substring(0, 4)+"@"+contact.substring(0, 3);
                        HashMap<String, Object> sellerMap = new HashMap<>();

                        sellerMap.put("semail", email);
                        sellerMap.put("sphone", contact);
                        sellerMap.put("sname", name);
                        sellerMap.put("date", saveCurrentDate);
                        sellerMap.put("time", saveCurrentTime);
                        sellerMap.put("spass", pass);
                        databaseReference.updateChildren(sellerMap).addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                loadingBar.dismiss();
                                showCustomDialog();

                            }                        });
                    }
                    else {
                        Snackbar
                                .make(v, "Please enter all the details", Snackbar.LENGTH_LONG).show();
                    }
                }
                else {
                    Snackbar
                            .make(v, "Please provide your store image", Snackbar.LENGTH_LONG).show();
                }
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
            InputshopImage.setVisibility(View.VISIBLE);
            InputshopImage.setImageURI(ImageUri);

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

        final StorageReference filePath = shopImagesRef.child(ImageUri.getLastPathSegment() +".jpg");
        final UploadTask uploadTask = filePath.putFile(ImageUri);
        uploadTask.addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                String message = e.toString();
                Toast.makeText(SellerRegister.this, "Error:" + message, Toast.LENGTH_SHORT).show();
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
            databaseReference.updateChildren(productMap).addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    if (task.isSuccessful()) {

                    } else {

                        String message = task.getException().toString();
                        Toast.makeText(SellerRegister.this, "Error Occurred!" + message, Toast.LENGTH_SHORT).show();
                    }
                }
            });
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        if (FirebaseAuth.getInstance().getCurrentUser() != null) {
            startActivity(new Intent(SellerRegister.this, HomeActivity.class));
            finish();
        }
    }

    private void showCustomDialog() {

        ViewGroup viewGroup = findViewById(android.R.id.content);

        View dialogView = LayoutInflater.from(this).inflate(R.layout.alert_confirm, viewGroup, false);


        AlertDialog.Builder builder = new AlertDialog.Builder(this);

        builder.setView(dialogView);

        AlertDialog alertDialog = builder.create();
        alertDialog.show();
        TextView ok = dialogView.findViewById(R.id.buttonOk);
        ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(SellerRegister.this, SellerRegister.class));
                SellerRegister.this.finish();
            }
        });
    }
}