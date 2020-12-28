package com.olokart.vendors;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

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
import java.util.List;
import java.util.Map;

public class PickupNewOrderAdapter extends RecyclerView.Adapter<PickupNewOrderAdapter.HomeViewHolder> {

    Context context;
    ArrayList<GetSet> product;
    private List<GetSet> GetSetListt;
    private AdapterView.OnItemClickListener listener;


    public PickupNewOrderAdapter(List<GetSet> hList, AdapterView.OnItemClickListener listener) {
        this.GetSetListt = hList;
        this.listener = listener;
    }

    public PickupNewOrderAdapter(Context c, ArrayList<GetSet> h) {
        context = c;
        product = h;
    }

    @NonNull
    @Override
    public PickupNewOrderAdapter.HomeViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new HomeViewHolder(LayoutInflater.from(context).inflate(R.layout.neworders_layout,
                parent, false));
    }
    @Override
    public void onBindViewHolder(@NonNull final PickupNewOrderAdapter.HomeViewHolder holder, final int position) {

        holder.bind(product.get(position), listener);

        holder.bagValue.setText("₹"+product.get(position).getBagprice());
        holder.date.setText(product.get(position).getDate());
        holder.ordertxt.setText(product.get(position).getOrderid());
        holder.orderstr = product.get(position).getOrderid();

        holder.uuid = product.get(position).getUuid();

        if (product.get(position).getOrder().equals("cancelled")) {
            holder.accept.setVisibility(View.GONE);
            holder.decline.setVisibility(View.GONE);
            holder.cancelled.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public int getItemCount() {
        return product.size();
    }


    public interface OnItemClickListener {
        void onItemClick(GetSet GetSet);
    }

    public class HomeViewHolder extends RecyclerView.ViewHolder {

        TextView date, bagValue,cancelled;
        FirebaseAuth firebaseAuth;
        DatabaseReference databaseReference, userOrderRef, orderRef, sellerRef;
        Button  seeall;
        String orderstr, uuid, sname;
        TextView ordertxt;
        Button accept,decline;

        private RequestQueue mRequestQue;
        private String URL = "https://fcm.googleapis.com/fcm/send";
        public HomeViewHolder(View itemView) {

            super(itemView);

            cancelled = itemView.findViewById(R.id.ordercancelhome);
            seeall = itemView.findViewById(R.id.order_seeall);

            firebaseAuth = FirebaseAuth.getInstance();

            mRequestQue = Volley.newRequestQueue(context);

            ordertxt = itemView.findViewById(R.id.newHomeorderid);
            bagValue = itemView.findViewById(R.id.bag_value);
            date = itemView.findViewById(R.id.order_date);
            accept=itemView.findViewById(R.id.accept);
            decline=itemView.findViewById(R.id.decline);
            databaseReference = FirebaseDatabase.getInstance().getReference("Seller Orders").child(
                    firebaseAuth.getCurrentUser().getUid()).child("Pickup");

            userOrderRef = FirebaseDatabase.getInstance().getReference("User Orders");

            sellerRef = FirebaseDatabase.getInstance().getReference("Sellers").child(FirebaseAuth.getInstance().getCurrentUser().getUid());
            orderRef = FirebaseDatabase.getInstance().getReference("Orders").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child("Pickup");




        }



        public void bind(final GetSet item, final AdapterView.OnItemClickListener listener) {

            sellerRef.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    sname = dataSnapshot.child("sname").getValue().toString();
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });

            seeall.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    context.startActivity(new Intent(context,PickupNewAllproducts.class).putExtra("orderid", orderstr));

                }
            });
            accept.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    //mymessage();


                    HashMap<String, Object> promap = new HashMap<>();
                    promap.put("state", "ongoing");
                    orderRef.child(orderstr).updateChildren(promap);
                    userOrderRef.child(uuid).child(orderstr).updateChildren(promap);
                    Toast.makeText(context, "Products added to ongoing...", Toast.LENGTH_SHORT).show();


                                       JSONObject json = new JSONObject();
                    try {
                        json.put("to","/topics/"+uuid);
                        JSONObject notificationObj = new JSONObject();
                        notificationObj.put("title",sname+" accepted your order!");
                        notificationObj.put("body","Our vendor partner accepted your order. We will update you once the order is ready to pickup");

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


                    // startActivity(new Intent(AllProducts.this, HomeNewOrders.class));


                }
            });

            decline.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    orderRef.child(orderstr).removeValue();
                    databaseReference.child(orderstr).removeValue();
                    userOrderRef.child(uuid).child(orderstr).removeValue();
                    Toast.makeText(context, "Order Declined...", Toast.LENGTH_SHORT).show();


                    JSONObject json = new JSONObject();
                    try {
                        json.put("to","/topics/"+uuid);
                        JSONObject notificationObj = new JSONObject();
                        notificationObj.put("title",sname+" declined your order!");
                        notificationObj.put("body","Our vendor partner has declined your order.");

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



                    // startActivity(new Intent(AllProducts.this, HomeNewOrders.class));


                }
            });

        }

                public void filterList(ArrayList<GetSet> filteredList) {
                    product = filteredList;
                    notifyDataSetChanged();
                }
            }


}

