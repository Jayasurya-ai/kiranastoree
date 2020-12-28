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

public class PastpickOrderAdapter extends RecyclerView.Adapter<PastpickOrderAdapter.HomeViewHolder> {

    Context context;
    ArrayList<GetSet> product;
    private List<GetSet> GetSetListt;
    private AdapterView.OnItemClickListener listener;


    public PastpickOrderAdapter(List<GetSet> hList, AdapterView.OnItemClickListener listener) {
        this.GetSetListt = hList;
        this.listener = listener;
    }

    public PastpickOrderAdapter(Context c, ArrayList<GetSet> h) {
        context = c;
        product = h;
    }

    @NonNull
    @Override
    public PastpickOrderAdapter.HomeViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new HomeViewHolder(LayoutInflater.from(context).inflate(R.layout.neworders_layout,
                parent, false));
    }
    @Override
    public void onBindViewHolder(@NonNull final PastpickOrderAdapter.HomeViewHolder holder, final int position) {

        holder.bind(product.get(position), listener);


        holder.bagValue.setText("₹"+product.get(position).getBagprice());
        holder.date.setText(product.get(position).getDate());
        holder.ordertxt.setText(product.get(position).getOrderid());

        holder.orderstr = product.get(position).getOrderid();
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

        TextView date, bagValue;
        FirebaseAuth firebaseAuth;
        DatabaseReference databaseReference;
        Button  seeall,accept,decline;
        String orderstr;
        TextView ordertxt;

        public HomeViewHolder(View itemView) {

            super(itemView);

            seeall = itemView.findViewById(R.id.order_seeall);

            ordertxt = itemView.findViewById(R.id.newHomeorderid);
            bagValue = itemView.findViewById(R.id.bag_value);
            date = itemView.findViewById(R.id.order_date);
            accept=itemView.findViewById(R.id.accept);
            decline=itemView.findViewById(R.id.decline);
            firebaseAuth = FirebaseAuth.getInstance();

            databaseReference = FirebaseDatabase.getInstance().getReference("Seller Orders").child(
                    firebaseAuth.getCurrentUser().getUid()).child("Pickup");




        }



        public void bind(final GetSet item, final AdapterView.OnItemClickListener listener) {

            seeall.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    context.startActivity(new Intent(context,PickPastAllproducts.class).putExtra("orderid", orderstr));

                }
            });
        }

                public void filterList(ArrayList<GetSet> filteredList) {
                    product = filteredList;
                    notifyDataSetChanged();
                }
            }


}

