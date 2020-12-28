package com.olokart.vendors;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.List;

public class OngoingOrderAdapter extends RecyclerView.Adapter<OngoingOrderAdapter.HomeViewHolder> {

    Context context;
    ArrayList<GetSet> product;
    private List<GetSet> GetSetListt;
    private AdapterView.OnItemClickListener listener;


    public OngoingOrderAdapter(List<GetSet> hList, AdapterView.OnItemClickListener listener) {
        this.GetSetListt = hList;
        this.listener = listener;
    }

    public OngoingOrderAdapter(Context c, ArrayList<GetSet> h) {
        context = c;
        product = h;
    }

    @NonNull
    @Override
    public OngoingOrderAdapter.HomeViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new HomeViewHolder(LayoutInflater.from(context).inflate(R.layout.neworders_layout,
                parent, false));
    }
    @Override
    public void onBindViewHolder(@NonNull final OngoingOrderAdapter.HomeViewHolder holder, final int position) {

        holder.bind(product.get(position), listener);

        holder.bagValue.setText("₹"+product.get(position).getBagprice());
        holder.date.setText(product.get(position).getDate());
        holder.ordertxt.setText(product.get(position).getOrderid());

        holder.orderstr = product.get(position).getOrderid();
        holder.state=product.get(position).getState();
        holder.accept.setVisibility(View.GONE);
        holder.decline.setVisibility(View.GONE);

    }

    @Override
    public int getItemCount() {
        return product.size();
    }


    public interface OnItemClickListener {
        void onItemClick(GetSet GetSet);
    }

    public class HomeViewHolder extends RecyclerView.ViewHolder {

        TextView  date, bagValue;
        FirebaseAuth firebaseAuth;
        DatabaseReference databaseReference;
        Button  seeall;
        TextView ordertxt;
        String orderstr;
        Button accept,decline;
        String state;

        public HomeViewHolder(View itemView) {

            super(itemView);


            seeall = itemView.findViewById(R.id.order_seeall);
            accept=itemView.findViewById(R.id.accept);
            decline=itemView.findViewById(R.id.decline);

            ordertxt = itemView.findViewById(R.id.newHomeorderid);
            bagValue = itemView.findViewById(R.id.bag_value);
            date = itemView.findViewById(R.id.order_date);
            firebaseAuth = FirebaseAuth.getInstance();

            databaseReference = FirebaseDatabase.getInstance().getReference("Seller Orders").child(
                    firebaseAuth.getCurrentUser().getUid()).child("Home Delivery");




        }



        public void bind(final GetSet item, final AdapterView.OnItemClickListener listener) {

            seeall.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    context.startActivity(new Intent(context,AllProductsOngoing.class).putExtra("orderid", orderstr));

                }
            });

        }

                public void filterList(ArrayList<GetSet> filteredList) {
                    product = filteredList;
                    notifyDataSetChanged();
                }
            }


}

