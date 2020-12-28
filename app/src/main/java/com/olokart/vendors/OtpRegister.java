package com.olokart.vendors;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.gms.tasks.TaskExecutors;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.StorageTask;
import com.google.firebase.storage.UploadTask;
import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;

import java.util.HashMap;
import java.util.concurrent.TimeUnit;

public class OtpRegister extends AppCompatActivity {
    private Button registerButton;
    private ImageView InputshopImage;
    private static final int GalleryPick = 1;
    private Uri ImageUri;
    private String downloadImageUrl;
    DatabaseReference databaseReference;
    private StorageReference shopImagesRef;
    ImageView floatingActionButton;
    FirebaseAuth firebaseAuth;
    private StorageTask uploadTask;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_otp_register);
        firebaseAuth = FirebaseAuth.getInstance();

            databaseReference = FirebaseDatabase.getInstance().getReference("Sellers").child(FirebaseAuth.getInstance().getCurrentUser().getUid());
        floatingActionButton = findViewById(R.id.upload_image);
        InputshopImage = findViewById(R.id.select_shop_image);

        registerButton = findViewById(R.id.seller_register_btn);
        shopImagesRef = FirebaseStorage.getInstance().getReference("Sellers Data");


        registerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (ImageUri == null) {
                    Toast.makeText(OtpRegister.this, "Upload Store image", Toast.LENGTH_SHORT).show();
                }
                else {
                    uploadImage();
                }
            }
        });
        // save phone number

//        floatingActionButton.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                OpenGallery();
//            }
//
//        });
//        registerButton.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                if(ImageUri==null){
//                    Toast.makeText(OtpRegister.this, "Please Provide Store Image...", Toast.LENGTH_SHORT).show();
//
//                } else{
//                    ValidateProductData();
//                }
//
//            }
//
//        });


        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                CropImage.activity(ImageUri)
                        .setAspectRatio(1,1)
                        .start(OtpRegister.this);


            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode==CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE && resultCode==RESULT_OK && data!=null){
            CropImage.ActivityResult result=CropImage.getActivityResult(data);
            ImageUri=result.getUri();
            InputshopImage.setVisibility(View.VISIBLE);
            InputshopImage.setImageURI(ImageUri);
        }
        else{
            Toast.makeText(this, "Error:Try again", Toast.LENGTH_SHORT).show();
        }
    }

    private void uploadImage() {
        final ProgressDialog progressDialog= new ProgressDialog(this);
        progressDialog.setTitle("Please wait while we are updating your profile information...");
        progressDialog.setCanceledOnTouchOutside(false);
        progressDialog.show();
        if(ImageUri!=null){
            final StorageReference fileRef=shopImagesRef.child(ImageUri.getLastPathSegment() + ".jpg");
            uploadTask=fileRef.putFile(ImageUri);
            Task task;
            task = uploadTask.continueWithTask(new Continuation() {
                @Override
                public Object then(@NonNull Task task) throws Exception {
                    if (!task.isSuccessful()) {
                        throw task.getException();
                    }
                    return fileRef.getDownloadUrl();
                }
            }).addOnCompleteListener(new OnCompleteListener<Uri>() {
                @Override
                public void onComplete(@NonNull Task<Uri> task) {
                    if (task.isSuccessful()) {
                        Uri downloadUrl = task.getResult();
                        downloadImageUrl = downloadUrl.toString();
                        DatabaseReference ref = FirebaseDatabase.getInstance().getReference().child("Sellers").child(
                                FirebaseAuth.getInstance().getCurrentUser().getUid()
                        );
                        HashMap<String, Object> userMap = new HashMap<>();
                        userMap.put("simage", downloadImageUrl);
                        databaseReference.updateChildren(userMap);
                        progressDialog.dismiss();
                        startActivity(new Intent(OtpRegister.this,movemap.class));
                        Toast.makeText(OtpRegister.this, "Profile Updated Succesfully!", Toast.LENGTH_SHORT).show();

                        finish();

                    } else {
                        Toast.makeText(OtpRegister.this, "Error:", Toast.LENGTH_SHORT).show();
                        progressDialog.dismiss();
                    }
                }
            });
        }
        else{
            Toast.makeText(this, "Image is not Selected..", Toast.LENGTH_SHORT).show();
        }
    }

//    @Override
//    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
//        super.onActivityResult(requestCode, resultCode, data);
//        if (requestCode == GalleryPick && resultCode == RESULT_OK && data != null) {
//            ImageUri = data.getData();
//            InputshopImage.setVisibility(View.VISIBLE);
//            InputshopImage.setImageURI(ImageUri);
//
//        }
//    }
//@Override
//protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
//    super.onActivityResult(requestCode, resultCode, data);
//
//    if (requestCode == GalleryPick && resultCode == RESULT_OK && data != null && data.getData() != null) {
//        Uri imageUri = data.getData();
//        CropImage.activity()
//                .setGuidelines(CropImageView.Guidelines.ON)
//                .setAspectRatio(1,1)
//                .start(this);
//    }
//    if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
//
//        CropImage.ActivityResult result = CropImage.getActivityResult(data);
//
//        if (resultCode == RESULT_OK) {
//
//            final Uri resultUri = result.getUri();
//
//            final StorageReference filePath = shopImagesRef;
//
//            filePath.putFile(resultUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
//                @Override
//                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
//
//                    filePath.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
//                        @Override
//                        public void onSuccess(Uri uri) {
//                            databaseReference.child("simage").setValue(String.valueOf(uri))
//                                    .addOnCompleteListener(new OnCompleteListener<Void>() {
//                                        @Override
//                                        public void onComplete(@NonNull Task<Void> task) {
//                                            if (task.isSuccessful()) {
//                                                Toast.makeText(OtpRegister.this, "Store image updated", Toast.LENGTH_SHORT).show();
//                                            }
//                                            else {
//                                                String message = task.getException().getMessage();
//                                                Toast.makeText(OtpRegister.this, "Error Occurred! Try again "+message, Toast.LENGTH_SHORT).show();
//                                            }
//                                        }
//                                    });
//                        }
//                    });
//                }
//            });
//        }
//    }
//}

//    protected void onActivityResult(int requestCode, int resultCode, Intent imageReturnedIntent) {
//        super.onActivityResult(requestCode, resultCode, imageReturnedIntent);
//        switch(requestCode) {
//            case 0:
//                if(resultCode == RESULT_OK){
//                     Uri uri = imageReturnedIntent.getData();
//                     InputshopImage.setVisibility(View.VISIBLE);
//                    InputshopImage.setImageURI(uri);
//                }
//
//                break;
//            case 1:
//                if(resultCode == RESULT_OK){
//                     Uri imageUri = imageReturnedIntent.getData();
//                    InputshopImage.setVisibility(View.VISIBLE);
//                    InputshopImage.setImageURI(imageUri);
//                }
//                break;
//        }
//    }

    //
//    private void ValidateProductData() {
//        if (ImageUri != null) {
//            StoreProductInformation();
//
//        } else {
//            Toast.makeText(this, "Store Image is Mandatory", Toast.LENGTH_SHORT).show();
//
//        }
//    }
//
//    private void StoreProductInformation() {
//
//        final StorageReference filePath = shopImagesRef.child(ImageUri.getLastPathSegment() + ".jpg");
//        final UploadTask uploadTask = filePath.putFile(ImageUri);
//        uploadTask.addOnFailureListener(new OnFailureListener() {
//            @Override
//            public void onFailure(@NonNull Exception e) {
//                String message = e.toString();
//                Toast.makeText(OtpRegister.this, "Error:" + message, Toast.LENGTH_SHORT).show();
//            }
//        }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
//            @Override
//            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
//                Task<Uri> urlTask = uploadTask.continueWithTask(new Continuation<UploadTask.TaskSnapshot, Task<Uri>>() {
//                    @Override
//                    public Task<Uri> then(@NonNull Task<UploadTask.TaskSnapshot> task) throws Exception {
//                        if (!task.isSuccessful()) {
//                            throw task.getException();
//                        }
//                        downloadImageUrl = filePath.getDownloadUrl().toString();
//                        return filePath.getDownloadUrl();
//                    }
//                }).addOnCompleteListener(new OnCompleteListener<Uri>() {
//                    @Override
//                    public void onComplete(@NonNull Task<Uri> task) {
//                        if (task.isSuccessful()) {
//
//                            downloadImageUrl = task.getResult().toString();
//                            saveProductInfoToDatabase();
//                        }
//                    }
//                });
//            }
//        });
//
//    }
//
//    private void saveProductInfoToDatabase() {
//        final HashMap<String, Object> productMap = new HashMap<>();
//        if (downloadImageUrl.isEmpty()) {
//            Toast.makeText(this, "upload image", Toast.LENGTH_SHORT).show();
//        } else {
//            productMap.put("simage", downloadImageUrl);
//            databaseReference.child(FirebaseAuth.getInstance().getCurrentUser().getUid()).updateChildren(productMap).addOnCompleteListener(new OnCompleteListener<Void>() {
//                @Override
//                public void onComplete(@NonNull Task<Void> task) {
//                    if (task.isSuccessful()) {
//
//                        startActivity(new Intent(OtpRegister.this, movemap.class));
//                        finish();
//                    } else {
//
//                        String message = task.getException().toString();
//                        Toast.makeText(OtpRegister.this, "Error Occurred!" + message, Toast.LENGTH_SHORT).show();
//                    }
//                }
//            });
//        }
//    }

//                        String pass = name.substring(0, 4) + "@" + contact.substring(0, 3
}