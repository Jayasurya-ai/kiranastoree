package com.olokart.vendors;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.FirebaseAuth;

public class SellerOtpRegister extends AppCompatActivity {
    EditText editTextCountryCode, editTextPhone;
    Button buttonContinue;
    TextInputEditText username;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_seller_otp_register);
        //  requestLocationPermission();
        // checkLocationPermission();
        overridePendingTransition(0, 0);
        RelativeLayout relativeLayout = findViewById(R.id.login_container);
        Animation animation = AnimationUtils.loadAnimation(this, android.R.anim.fade_in);
        relativeLayout.startAnimation(animation);
        username = findViewById(R.id.usernameReg);


        editTextCountryCode = findViewById(R.id.editCode);
        editTextCountryCode.setText("+91");
        editTextPhone = findViewById(R.id.numer);
        buttonContinue = findViewById(R.id.continueReg);

        buttonContinue.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //  getLastLocation();
                String code = editTextCountryCode.getText().toString().trim();
                String number = editTextPhone.getText().toString().trim();

                if (number.isEmpty() || number.length() < 10 || TextUtils.isEmpty(username.getText().toString())) {
                    Toast.makeText(SellerOtpRegister.this, "Please Enter all details...", Toast.LENGTH_SHORT).show();
                    return;
                }

                String phoneNumber = code + number;

                Intent intent = new Intent(SellerOtpRegister.this, VerifyPhoneActivity.class);
                intent.putExtra("phoneNumber", phoneNumber);
                intent.putExtra("uname", username.getText().toString());
                startActivity(intent);
                finish();

            }
        });


    }

    @Override
    protected void onStart() {
        super.onStart();
        if (FirebaseAuth.getInstance().getCurrentUser() != null) {
            startActivity(new Intent(SellerOtpRegister.this, HomeActivity.class));
            finish();
        }
    }
}