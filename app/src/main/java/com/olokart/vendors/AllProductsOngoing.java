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

public class AllProductsOngoing extends AppCompatActivity {
    private RecyclerView recyclerView;
    private DatabaseReference reference,userref, orderRef, userOrderRef, sellerRef;
    ArrayList<GetSet> getSets;
    OrdersPlacedAdapter adapter;
    TextView bagprice, delivery, name, phone;
    Button accept,decline;
    String userid;
    String uphone, uname;
    String orderid,state="ongoing", sname;

    private RequestQueue mRequestQue;
    private String URL = "https://fcm.googleapis.com/fcm/send";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_all_products_ongoing);

        delivery = findViewById(R.id.deliveryUserAddrongoing);
        bagprice = findViewById(R.id.bagrs2);
        accept = findViewById(R.id.accept2);
        decline = findViewById(R.id.decline2);
        name = findViewById(R.id.deliverUsernameongoing);
        phone = findViewById(R.id.deliveryUserphoneongoing);
        mRequestQue = Volley.newRequestQueue(this);


        Intent i = getIntent();
        orderid = i.getStringExtra("orderid");

        sellerRef = FirebaseDatabase.getInstance().getReference("Sellers").child(FirebaseAuth.getInstance().getCurrentUser().getUid());

        recyclerView = findViewById(R.id.ordersplacedRecycler2);
        recyclerView.setHasFixedSize(true);


        getSets = new ArrayList<GetSet>();


        userOrderRef = FirebaseDatabase.getInstance().getReference("User Orders");

        orderRef = FirebaseDatabase.getInstance().getReference("Orders").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child("Home Delivery");

        reference = FirebaseDatabase.getInstance().getReference("Seller Orders").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child("Home Delivery");
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(AllProductsOngoing.this);
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setAdapter(adapter);

        sellerRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                sname = dataSnapshot.child("sname").getValue().toString();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        reference.child(orderid).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                getSets.clear();
                for (DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()) {

                    GetSet h = dataSnapshot1.getValue(GetSet.class);
                    getSets.add(h);

                    orderRef.child(orderid).addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                            String Bagprice = dataSnapshot.child("bagprice").getValue().toString();
                            String address = dataSnapshot.child("address").getValue().toString();
                            String unamestr = dataSnapshot.child("uname").getValue().toString();
                            String uphonestr = dataSnapshot.child("uphone").getValue().toString();
                            bagprice.setText("₹"+Bagprice);
                            delivery.setText(address);
                            name.setText(unamestr);
                            phone.setText(uphonestr);

                            userid = dataSnapshot.child("uuid").getValue().toString();
                            userref = FirebaseDatabase.getInstance().getReference("Users").child(userid);
                            userref.addValueEventListener(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                    uphone = dataSnapshot.child("uphone").getValue().toString();
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
                adapter = new OrdersPlacedAdapter(AllProductsOngoing.this, getSets);
                recyclerView.setAdapter(adapter);


                accept.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                            //mymessage();
                            Toast.makeText(AllProductsOngoing.this, "Started Delivery...", Toast.LENGTH_SHORT).show();

                            sendNotification();

                        }

                });

                decline.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {


                            //mymessage();

                        HashMap<String, Object> stateMap = new HashMap<>();
                        stateMap.put("state", "past");
                        orderRef.child(orderid).updateChildren(stateMap);
                        userOrderRef.child(userid).child(orderid).updateChildren(stateMap);
                        Toast.makeText(AllProductsOngoing.this, "Products Delivered...", Toast.LENGTH_SHORT).show();
                        sendNotificationDelivered();

                    }
                });

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

//    private void mymessage() {
//
//
//        SmsManager smsManager=SmsManager.getDefault();
//        smsManager.sendTextMessage(uphone,null,"Your Product Delivery started" +uphone,null,null);
//    }

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
            notificationObj.put("title",sname+" has Started delivery");
            notificationObj.put("body","Hi "+uname+ "\nYour order with order number "+orderid+" has been dispatched and will be delivered shortly.\nThanks\nTeam Olokart");

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
    private void sendNotificationDelivered() {

        JSONObject json = new JSONObject();
        try {
            json.put("to","/topics/"+userid);
            JSONObject notificationObj = new JSONObject();
            notificationObj.put("title",sname+" has delivered your products!");
            notificationObj.put("body","Hi "+uname+"\nThanks for choosing Olokart. Your order with Order id "+orderid+" is delivered successfully!\nWould love to here your experience.\nThanks\nTeam Olokart");

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