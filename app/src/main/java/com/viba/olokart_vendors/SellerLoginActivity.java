package com.viba.olokart_vendors;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.InputType;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.rey.material.widget.LinearLayout;

public class SellerLoginActivity extends AppCompatActivity {
    private Button loginSellerBtn;
    private EditText emailInput, passwordInput;
    private ProgressDialog loadingBar;
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_seller_login);
        emailInput = findViewById(R.id.seller_login_email);
        passwordInput = findViewById(R.id.seller_login_password);
        loginSellerBtn = findViewById(R.id.seller_login_btn);
        mAuth = FirebaseAuth.getInstance();
        loadingBar = new ProgressDialog(this);
        loginSellerBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loginSeller();
            }
        });
    }

    private void loginSeller() {
        final String email = emailInput.getText().toString();
        final String password = passwordInput.getText().toString();
        if (!email.equals("") && !password.equals("")) {
            loadingBar.setTitle("Validating Account");
            loadingBar.setMessage("Please wait while we are checking the Credentials");
            loadingBar.setCanceledOnTouchOutside(false);
            loadingBar.show();
            mAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if (task.isSuccessful()) {
                        Intent intent = new Intent(SellerLoginActivity.this, SellerLocation.class);
                        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        startActivity(intent);
                        finish();
                    }
                    else {
                        loadingBar.dismiss();
                        Toast.makeText(SellerLoginActivity.this, "Wrong credentials!", Toast.LENGTH_SHORT).show();
                    }
                }
            });
        } else {
            loadingBar.dismiss();
            Toast.makeText(this, "Please provide Email and Password..", Toast.LENGTH_SHORT).show();
        }

    }

    public void logtoreg(View view) {
        startActivity(new Intent(this, SellerRegister.class));
    }

    public void recover(View view) {
        forgotshow();
    }

    private void forgotshow() {

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Recover Password");

        LinearLayout linearLayout = new LinearLayout(this);
        final EditText emailText = new EditText(this);
        emailText.setHint("Enter email");
        emailText.setInputType(InputType.TYPE_TEXT_VARIATION_EMAIL_ADDRESS);
        emailText.setMinEms(15);

        linearLayout.addView(emailText);
        linearLayout.setPadding(10, 10, 10, 10);

        builder.setView(linearLayout);

        builder.setPositiveButton("Recover", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                String recoveremail = emailText.getText().toString();

                if (recoveremail.isEmpty()) {
                    loadingBar.dismiss();
                    Toast.makeText(SellerLoginActivity.this, "Please enter ur email",
                            Toast.LENGTH_SHORT).show();
                } else {
                    beginRecovery(recoveremail);
                }
            }
        });

        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                loadingBar.dismiss();
            }
        });
        builder.create().show();
    }

    private void beginRecovery(String foremail) {

        loadingBar.show();

        FirebaseAuth.getInstance().sendPasswordResetEmail(foremail)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {

                        if (task.isSuccessful()) {
                            loadingBar.dismiss();
                            Toast.makeText(SellerLoginActivity.this, "Recover link is sent to your registered email, Please check!",
                                    Toast.LENGTH_LONG).show();
                        } else {
                            loadingBar.dismiss();
                            Toast.makeText(SellerLoginActivity.this, "Failed...! try again",
                                    Toast.LENGTH_LONG).show();
                        }
                    }
                }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                loadingBar.dismiss();
                Toast.makeText(SellerLoginActivity.this, "" + e.getMessage(),
                        Toast.LENGTH_LONG).show();
            }
        });

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }
}
