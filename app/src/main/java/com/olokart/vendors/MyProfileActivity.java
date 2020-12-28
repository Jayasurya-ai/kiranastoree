package com.olokart.vendors;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import java.util.HashMap;

import de.hdodenhof.circleimageview.CircleImageView;

public class MyProfileActivity extends AppCompatActivity {

    TextView userName, userNum, statustxt ;
    ImageView editProfile;
    DatabaseReference updateref;
    TextView about, feedback, desclaimer, help, terms, logout, share;
    CircleImageView circleImageView;
    private StorageReference shopImagesRef;
    Uri ImageUri;
    FloatingActionButton floatingActionButton;
    ProgressDialog progressDialog;
    private static final int GalleryPick = 1;
    TextView saveProfile;
    String downloadImageUrl;
    Switch aSwitch;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        updateref= FirebaseDatabase.getInstance().getReference("Sellers").child(FirebaseAuth.getInstance().getCurrentUser().getUid());;

        userName = findViewById(R.id.userName);
        userNum = findViewById(R.id.user_mobile);
        editProfile = findViewById(R.id.editProfile);
        floatingActionButton = findViewById(R.id.editFab);

        aSwitch = findViewById(R.id.switchStatus);
        statustxt = findViewById(R.id.shopStatus);
        saveProfile = findViewById(R.id.saveProfile);
        shopImagesRef = FirebaseStorage.getInstance().getReference("Sellers Data");

        circleImageView = findViewById(R.id.shopProfile);
        about = findViewById(R.id.cardAbout);
        feedback = findViewById(R.id.cardFeedback);
        desclaimer = findViewById(R.id.cardDesclaimer);
        help = findViewById(R.id.cardHelp);
        terms = findViewById(R.id.cardTerms);
        logout = findViewById(R.id.cardLogout);
        share = findViewById(R.id.cardShare);

        progressDialog = new ProgressDialog(this);
        progressDialog.setTitle("Updating Store profile");
        progressDialog.setMessage("Please wait..");
        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                OpenGallery();
            }

        });

        aSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    HashMap<String,Object>pushmap=new HashMap<>();
                    pushmap.put("sstatus","open");
                    updateref.updateChildren(pushmap);

                } else {
                    HashMap<String,Object>pushmap=new HashMap<>();
                    pushmap.put("sstatus","close");
                    updateref.updateChildren(pushmap);

                }
            }//
        });

        saveProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (ImageUri == null) {
                    Toast.makeText(MyProfileActivity.this, "Please provide store image...", Toast.LENGTH_SHORT).show();
                }
                else {

                    StoreProductInformation();
                }
            }
        });

        editProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ViewGroup viewGroup = findViewById(android.R.id.content);
                View dialogView = LayoutInflater.from(MyProfileActivity.this).inflate(R.layout.editprofilelayout, viewGroup, false);

                AlertDialog.Builder builder = new AlertDialog.Builder(MyProfileActivity.this);

                builder.setView(dialogView);

                final AlertDialog alertDialog = builder.create();
                Window window = alertDialog.getWindow();
                window.setGravity(Gravity.BOTTOM);

                window.setLayout(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                alertDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                alertDialog.getWindow().getAttributes().windowAnimations = R.style.DialogAnimation_2;
                alertDialog.show();
                Button update,cancel;
                update=dialogView.findViewById(R.id.update);
                cancel=dialogView.findViewById(R.id.cancel);
                final TextInputEditText editname,editemail, editMinorder;
                editname=dialogView.findViewById(R.id.editname);
                editemail=dialogView.findViewById(R.id.editemail);
                editMinorder = dialogView.findViewById(R.id.editminorder);
                updateref.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        String setName = dataSnapshot.child("sname").getValue().toString();
                        String setEmail = dataSnapshot.child("semail").getValue().toString();
                        String setMiniorder = dataSnapshot.child("minorder").getValue().toString();

                        editname.setText(setName);
                        editemail.setText(setEmail);
                        editMinorder.setText(setMiniorder);
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });

                update.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if(TextUtils.isEmpty(editemail.getText().toString())||TextUtils.isEmpty(editname.getText().toString())){
                            Toast.makeText(MyProfileActivity.this, "Please provide all details..", Toast.LENGTH_SHORT).show();
                        }
                        else{

                            HashMap<String,Object> updatemap=new HashMap<>();
                            updatemap.put("sname",editname.getText().toString());
                            updatemap.put("semail",editemail.getText().toString());
                            updatemap.put("minorder",editMinorder.getText().toString());
                            updateref.updateChildren(updatemap);
                            Toast.makeText(MyProfileActivity.this, "Data Updated Succesfully...", Toast.LENGTH_SHORT).show();
                            alertDialog.dismiss();
                        }

                    }
                });
                cancel.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        alertDialog.dismiss();
                    }
                });

            }
        });

        about.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), About_us.class));
            }
        });

        feedback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                showRateDialog(MyProfileActivity.this);
            }
        });

        desclaimer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), Desclaimer.class));
            }
        });

        help.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), HelpandSupport.class));
            }
        });

        terms.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), TermsandConditions.class));
            }
        });

        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FirebaseAuth.getInstance().signOut();
                startActivity(new Intent(getApplicationContext(), SellerOtpRegister.class));
                finishAffinity();
            }
        });
        share.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                try {
                    Intent shareIntent = new Intent(Intent.ACTION_SEND);
                    shareIntent.setType("text/plain");
                    shareIntent.putExtra(Intent.EXTRA_SUBJECT, "Olokart");
                    String shareMessage= "\nOLOKART is one of the emerging online market platform provided for Customers and Vendors to deal with Various Products.\nDownload the app Olokart from below link.. \n\n";
                    shareMessage = shareMessage + "https://play.google.com/store/apps/details?id=" + BuildConfig.APPLICATION_ID +"\n\n";
                    shareIntent.putExtra(Intent.EXTRA_TEXT, shareMessage);
                    startActivity(Intent.createChooser(shareIntent, "choose any one"));
                } catch(Exception e) {
                    //e.toString();
                }

            }
        });


        updateref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                String usernamestr= dataSnapshot.child("sname").getValue().toString();
                String userNumberstr = dataSnapshot.child("sphone").getValue().toString();
                String userImage = dataSnapshot.child("simage").getValue().toString();
                String status = dataSnapshot.child("sstatus").getValue().toString();

                userName.setText(usernamestr);
                userNum.setText(userNumberstr);
                Picasso.get().load(userImage).placeholder(R.drawable.shopimage).into(circleImageView);

                if (status.equals("open")) {
                    aSwitch.setChecked(true);
                    statustxt.setText("Open");
                    statustxt.setTextColor(getResources().getColor(R.color.colorText));
                }
               else if (status.equals("close")) {
                    aSwitch.setChecked(false);
                    statustxt.setText("Close");
                    statustxt.setTextColor(getResources().getColor(R.color.colorPrimaryDark));
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.nav_notify);
        navigation.setSelectedItemId(R.id.navigation_orders);
        navigation.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.navigation_home:

                        Intent intent = new Intent(MyProfileActivity.this, HomeActivity.class);
                        startActivity(intent);
                        overridePendingTransition(0, 0);
                        finish();
                        break;
                    case R.id.navigation_add:
                        Intent intentCat = new Intent(MyProfileActivity.this, SellerAddActivity.class);
                        startActivity(intentCat);
                        overridePendingTransition(0, 0);
                        finish();
                        break;
                    case R.id.navigation_orders:
                        break;
                }
                return false;
            }
        });
    }
    private void OpenGallery() {
        Intent galleryIntent = new Intent();
        galleryIntent.setAction(Intent.ACTION_GET_CONTENT);
        galleryIntent.setType("image/*");
        startActivityForResult(galleryIntent, GalleryPick);
    }
    private void StoreProductInformation() {

        progressDialog.show();
        final StorageReference filePath = shopImagesRef.child(ImageUri.getLastPathSegment() + ".jpg");
        final UploadTask uploadTask = filePath.putFile(ImageUri);
        uploadTask.addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                String message = e.toString();
                Toast.makeText(MyProfileActivity.this, "Error:" + message, Toast.LENGTH_SHORT).show();
            }
        }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                Task<Uri> urlTask = uploadTask.continueWithTask(new Continuation<UploadTask.TaskSnapshot, Task<Uri>>() {
                    @Override
                    public Task<Uri> then(@NonNull Task<UploadTask.TaskSnapshot> task) throws Exception {
                        if (!task.isSuccessful()) {
                            progressDialog.dismiss();
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
                            progressDialog.dismiss();
                        }
                    }
                });
            }
        });

    }

    private void saveProductInfoToDatabase() {
        final HashMap<String, Object> productMap = new HashMap<>();
        if (downloadImageUrl.isEmpty()) {

            Toast.makeText(this, "upload image...", Toast.LENGTH_SHORT).show();
        } else {
            productMap.put("simage", downloadImageUrl);
            updateref.updateChildren(productMap).addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    if (task.isSuccessful()) {

                        Toast.makeText(MyProfileActivity.this, "Store image has updated!...", Toast.LENGTH_SHORT).show();
                    } else {

                        String message = task.getException().toString();
                        Toast.makeText(MyProfileActivity.this, "Error Occurred!" + message, Toast.LENGTH_SHORT).show();
                    }
                }
            });
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == GalleryPick && resultCode == RESULT_OK && data != null) {
            ImageUri = data.getData();
            circleImageView.setVisibility(View.VISIBLE);
            circleImageView.setImageURI(ImageUri);

        }
    }

    public void showRateDialog(final Context context) {
        AlertDialog.Builder builder = new AlertDialog.Builder((Context) context)
                .setTitle("Rate application")
                .setMessage("Please, rate the app at Playstore")
                .setPositiveButton("RATE", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if (context != null) {
                            String link = "market://details?id=";
                            try {
                                // play market available
                                ((Context) context).getPackageManager()
                                        .getPackageInfo("com.olokart.vendors", 0);
                                // not available
                            } catch (PackageManager.NameNotFoundException e) {
                                e.printStackTrace();
                                // should use browser
                                link = "https://play.google.com/store/apps/details?id=com.olokart.vendors";
                            }
                            // starts external action
                            startActivity(new Intent(Intent.ACTION_VIEW,
                                    Uri.parse(link + ((Context) context).getPackageName())));
                        }
                    }
                })
                .setNegativeButton("CANCEL", null);
        builder.show();
    }
}