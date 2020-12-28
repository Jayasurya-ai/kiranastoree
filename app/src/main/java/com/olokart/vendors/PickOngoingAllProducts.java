package com.olokart.vendors;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class PickOngoingAllProducts extends AppCompatActivity {
    private RecyclerView recyclerView;
    private DatabaseReference reference,userref, orderRef, userOrderRef,sellerRef;
    ArrayList<GetSet> getSets;
    OrdersPlacedAdapter adapter;
    TextView bagprice, name, phone, paymode;
    Button accept;
    String userid;
    String uphone, uname, sname;
    String orderid,state="ongoing";
    private RequestQueue mRequestQue;
    private String URL = "https://fcm.googleapis.com/fcm/send";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pick_ongoing_all_products);
        bagprice=findViewById(R.id.pbagrs2);
        name = findViewById(R.id.pickupUsernameongoing);
        phone = findViewById(R.id.pickupUserphoneongoing);
        //accept=findViewById(R.id.paccept2);
        accept=findViewById(R.id.pickedup);
        paymode = findViewById(R.id.pickupongoingPaymode);

        sellerRef = FirebaseDatabase.getInstance().getReference("Sellers").child(FirebaseAuth.getInstance().getCurrentUser().getUid());

        Intent intent = getIntent();
        orderid = intent.getStringExtra("orderid");
        mRequestQue = Volley.newRequestQueue(this);

        recyclerView = findViewById(R.id.pordersplacedRecycler2);
        recyclerView.setHasFixedSize(true);


        getSets = new ArrayList<GetSet>();


        userOrderRef = FirebaseDatabase.getInstance().getReference("User Orders");

        orderRef = FirebaseDatabase.getInstance().getReference("Orders").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child("Pickup");

        reference = FirebaseDatabase.getInstance().getReference("Seller Orders").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child("Pickup");
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(PickOngoingAllProducts.this);
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setAdapter(adapter);

        sellerRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                sname = dataSnapshot.child("sname").getValue().toString();}

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        reference.child(orderid).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull final DataSnapshot dataSnapshot) {
                getSets.clear();
                for (final DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()) {

                                GetSet h = dataSnapshot1.getValue(GetSet.class);
                                getSets.add(h);

                    orderRef.child(orderid).addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                            String Bagprice=dataSnapshot.child("bagprice").getValue().toString();
                            String namestr = dataSnapshot.child("uname").getValue().toString();
                            String phonestr = dataSnapshot.child("uphone").getValue().toString();
                            String paystr = dataSnapshot.child("pmode").getValue().toString();

                            bagprice.setText("₹"+Bagprice);
                            name.setText(namestr);
                            phone.setText(phonestr);
                            paymode.setText(paystr);

                            userid=dataSnapshot.child("uuid").getValue().toString();
                            userref=FirebaseDatabase.getInstance().getReference("Users").child(userid);
                            userref.addValueEventListener(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                    uphone=dataSnapshot.child("uphone").getValue().toString();
                                    uname = dataSnapshot.child("uname").getValue().toString();

                                }

                                @Override
                                public void onCancelled(@NonNull DatabaseError databaseError) {

                                }
                            });

                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {

                        }
                    });

                }
                adapter = new OrdersPlacedAdapter(PickOngoingAllProducts.this, getSets);
                recyclerView.setAdapter(adapter);
                //                                        date=dataSnapshot2.child("User details").child("date").getValue().toString();
//                                        time=dataSnapshot2.child("User details").child("time").getValue().toString();



                accept.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {





                        HashMap<String,Object>promap=new HashMap<>();
                        promap.put("state","past");
                        orderRef.child(orderid).updateChildren(promap);
                        userOrderRef.child(userid).child(orderid).updateChildren(promap);
                        Toast.makeText(PickOngoingAllProducts.this, "Products added to past...", Toast.LENGTH_SHORT).show();
                        sendNotification();

                    }
                });

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

//    }
//            private void mymessage() {
//
//
//        SmsManager smsManager=SmsManager.getDefault();
//        smsManager.sendTextMessage(uphone,null,"Your Product Delivery started" +uphone,null,null);
//        Toast.makeText(this, "Message sent succesful..", Toast.LENGTH_SHORT).show();
    }

//    @Override
//    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
//        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
//        switch (requestCode){
//            case 0:
//                if(grantResults.length>=0 &&grantResults[0]== PackageManager.PERMISSION_GRANTED){
//                    mymessage();
//                }
//                else
//                {
//                    Toast.makeText(this, "You dont have permission", Toast.LENGTH_SHORT).show();
//                }
//
//                break;
//        }
//
//    }
    private void sendNotification() {

        JSONObject json = new JSONObject();
        try {
            json.put("to","/topics/"+userid);
            JSONObject notificationObj = new JSONObject();
            notificationObj.put("title","Order Picked Up!");
            notificationObj.put("body","Hi "+uname+"\n Your order with Order number "+orderid+" has been picked up from "+sname+".\nThanks\nTeam Olokart"
                    );

            JSONObject extraData = new JSONObject();
            extraData.put("brandId","puma");
            extraData.put("category","Shoes");



            json.put("notification",notificationObj);
            json.put("data",extraData);


            JsonObjectRequest request = new JsonObjectRequest(Request.Method.POST, URL,
                    json,
                    new Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(JSONObject response) {

                            Log.d("MUR", "onResponse: ");
                        }
                    }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    Log.d("MUR", "onError: "+error.networkResponse);
                }
            }
            ){
                @Override
                public Map<String, String> getHeaders() throws AuthFailureError {
                    Map<String,String> header = new HashMap<>();
                    header.put("content-type","application/json");
                    header.put("authorization","key=AAAA2vX6iik:APA91bFj3SPKHBN8dmzXpuBOcCv3flSGLWpV17OlDdsix56fiu4UWwbhX4fnvef9HkU3-A90Ts1wsDtFG1QweW_kVvDLPxb_P5jwORDyX5zLeGHZYibabo8Qsdzfcht3N4GjEELsvX9b");
                    return header;
                }
            };
            mRequestQue.add(request);
        }
        catch (JSONException e)

        {
            e.printStackTrace();
        }
    }
}