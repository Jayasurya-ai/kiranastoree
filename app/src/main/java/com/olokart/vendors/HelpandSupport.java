package com.olokart.vendors;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

public class HelpandSupport extends AppCompatActivity {

    TextView call, email;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_helpand_support);

        call = findViewById(R.id.callSupport);
        email = findViewById(R.id.emailSupprt);

        call.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Call();
            }
        });
        email.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendEMail();
            }
        });
    }

    // This function is called when button is clicked.
     public  void Call() {
    Uri u = Uri.parse("tel:" +"9346850529" );
    Intent i = new Intent(Intent.ACTION_DIAL, u);

    try
    {
        startActivity(i);
    }
    catch (SecurityException s)
    {
        Toast.makeText(this, s.getMessage(), Toast.LENGTH_LONG)
                .show();
    }
}

    public void sendEMail() {
        Intent intent = new Intent(Intent.ACTION_SENDTO);
        intent.setType("text/html");
        intent.putExtra(Intent.EXTRA_EMAIL, "Order@olokart.in");
        intent.putExtra(Intent.EXTRA_SUBJECT, "Name of the issue");
        intent.putExtra(Intent.EXTRA_TEXT, "Enter your queries here");

        startActivity(Intent.createChooser(intent, "Send Email"));
    }
}
