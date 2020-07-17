package com.viba.olokart_vendors;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.StorageTask;
import com.squareup.picasso.Picasso;
import com.theartofdev.edmodo.cropper.CropImage;

import java.util.HashMap;

import de.hdodenhof.circleimageview.CircleImageView;

public class ChangeProfile extends AppCompatActivity {
    private CircleImageView profileImageView;
    private EditText fullNameEdittext,userPhoneEdittext,addressEditText;
    private TextView profileChangeTextBtn,closeTextBtn,saveTextBtn;
    private Button securityQuestionBtn;
    private Uri imageUri;
    private String myUrl="";
    private StorageTask uploadTask;
    private StorageReference storagePorfilePictureRef;
    private String checker="";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_profile);
        profileImageView = (CircleImageView) findViewById(R.id.settings_profile_image);
        fullNameEdittext = (EditText) findViewById(R.id.settings_full_name);
        storagePorfilePictureRef= FirebaseStorage.getInstance().getReference("Sellers Data").child(FirebaseAuth.getInstance().getCurrentUser().getUid());
        addressEditText = (EditText) findViewById(R.id.settings_address);
        userPhoneEdittext = (EditText) findViewById(R.id.settings_phone_number);
        profileChangeTextBtn = (TextView) findViewById(R.id.profile_image_change_btn);
        saveTextBtn = (TextView) findViewById(R.id.update_account_settings);
        userInfoDisplay(profileImageView, fullNameEdittext, userPhoneEdittext);
//
        saveTextBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (checker.equals("clicked")) {
                    userInfoSaved();
                } else {
                    upateOnlyUserInfoMethod();
                }
            }

            private void upateOnlyUserInfoMethod() {
                DatabaseReference ref= FirebaseDatabase.getInstance().getReference("Sellers");
                HashMap<String,Object> userMap=new HashMap<>();
                userMap.put("sname",fullNameEdittext.getText().toString());
                userMap.put("sowner",addressEditText.getText().toString());
                userMap.put("sphone",userPhoneEdittext.getText().toString());
                ref.child(FirebaseAuth.getInstance().getCurrentUser().getUid()).updateChildren(userMap);
             //   startActivity(new Intent(ChangeProfile.this, SellerSettingsActivity.class));
                Toast.makeText(ChangeProfile.this, "Profile info Updated Succesfully...", Toast.LENGTH_SHORT).show();
                finish();

            }
        });


        profileChangeTextBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checker="clicked";
                CropImage.activity(imageUri)
                        .setAspectRatio(1,1)
                        .start(ChangeProfile.this);


            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode==CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE && resultCode==RESULT_OK && data!=null){
            CropImage.ActivityResult result=CropImage.getActivityResult(data);
            imageUri=result.getUri();
            profileImageView.setImageURI(imageUri);
        }
        else{
            Toast.makeText(this, "Error:Try again", Toast.LENGTH_SHORT).show();
        }
    }
    private void userInfoSaved() {
        if(TextUtils.isEmpty(fullNameEdittext.getText().toString())){
            Toast.makeText(this, "Shop Name is Mandatory", Toast.LENGTH_SHORT).show();
        }
        else if(TextUtils.isEmpty(addressEditText.getText().toString())){
            Toast.makeText(this, "Shop Owner Name is Mandatory", Toast.LENGTH_SHORT).show();
        }
        else if(TextUtils.isEmpty(userPhoneEdittext.getText().toString())){
            Toast.makeText(this, "Phone Number  is Mandatory", Toast.LENGTH_SHORT).show();
        }
        else if (checker.equals("clicked")){
            uploadImage();

        }
    }

    private void uploadImage() {
        final ProgressDialog progressDialog= new ProgressDialog(this);
        progressDialog.setTitle("Store info updating");
        progressDialog.setMessage("Please wait...");
        progressDialog.setCanceledOnTouchOutside(false);
        progressDialog.show();
        if(imageUri!=null){
            final StorageReference fileRef=storagePorfilePictureRef.child(FirebaseAuth.getInstance().getCurrentUser().getUid());
            uploadTask=fileRef.putFile(imageUri);
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
                        myUrl = downloadUrl.toString();
                        DatabaseReference ref = FirebaseDatabase.getInstance().getReference("Sellers").child(FirebaseAuth.getInstance().getCurrentUser().getUid());
                        HashMap<String, Object> userMap = new HashMap<>();
                        userMap.put("sname", fullNameEdittext.getText().toString());
                        userMap.put("sowner", addressEditText.getText().toString());
                        userMap.put("sphone", userPhoneEdittext.getText().toString());
                        userMap.put("simage", myUrl);
                        ref.updateChildren(userMap);
                        progressDialog.dismiss();
                      //  startActivity(new Intent(ChangeProfile.this,SellerSettingsActivity.class));
                        Toast.makeText(ChangeProfile.this, "Store profile updated succesfully!", Toast.LENGTH_SHORT).show();
                        ChangeProfile.this.finish();

                    } else {
                        Toast.makeText(ChangeProfile.this, "Error:", Toast.LENGTH_SHORT).show();
                        progressDialog.dismiss();
                    }
                }
            });
        }
        else{
            Toast.makeText(this, "Image is not Selected..", Toast.LENGTH_SHORT).show();
        }
    }

    private void userInfoDisplay(final ImageView profileImageView, final EditText fullNameEdittext, final EditText userPhoneEdittext) {
        DatabaseReference UserRef= FirebaseDatabase.getInstance().getReference("Sellers").child(FirebaseAuth.getInstance().getCurrentUser().getUid());
        UserRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(dataSnapshot.exists()){
                    if(dataSnapshot.child("simage").exists()){
                        String image=dataSnapshot.child("simage").getValue().toString();
                        String name=dataSnapshot.child("sname").getValue().toString();
                        Picasso.get().load(image).into(profileImageView);
                        fullNameEdittext.setText(name);
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
    }