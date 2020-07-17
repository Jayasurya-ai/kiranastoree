package com.viba.olokart_vendors;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

public class OrdersAdapter extends RecyclerView.Adapter<OrdersAdapter.CartViewHolder> {

    Context context;
    ArrayList<GetSet> getSets;
    private List<GetSet> productsListt;
    private AdapterView.OnItemClickListener listener;

    public OrdersAdapter(List<GetSet> hList, AdapterView.OnItemClickListener listener) {
        this.productsListt = hList;
        this.listener = listener;
    }

    public OrdersAdapter(Context c, ArrayList<GetSet> h) {
        context = c;
        getSets = h;
    }

    @NonNull
    @Override
    public OrdersAdapter.CartViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new CartViewHolder(LayoutInflater.from(context).inflate(R.layout.orders_layout,
                parent, false));
    }
    @Override
    public void onBindViewHolder(@NonNull OrdersAdapter.CartViewHolder holder, int position) {

       // holder.bind(getSets.get(position), listener);
        holder.product_price.setText(getSets.get(position).getTotal_price()+" Rs");
        holder.product_quant.setText("Qty: "+getSets.get(position).getQuantity());
        holder.product_name.setText(getSets.get(position).getProduct_name());
        holder.productdate.setText(getSets.get(position).getDate());
        holder.productTime.setText(getSets.get(position).getTime().substring(0, 5));
        holder.deleteid = getSets.get(position).getOrder_id();
        Picasso.get().load(getSets.get(position).getProduct_image()).into(holder.product_image);

    }

    @Override
    public int getItemCount() {
        return getSets.size();
    }
    public interface OnItemClickListener {
        void onItemClick(GetSet products);
    }

    public class CartViewHolder extends RecyclerView.ViewHolder {

        TextView product_name, product_quant, product_price, productdate, productTime;
        ImageView product_image;
        String pid, deleteid;
        DatabaseReference databaseReference;

        public CartViewHolder(View itemView) {

            super(itemView);
            product_name = itemView.findViewById(R.id.cart_product_name);
            product_quant = itemView.findViewById(R.id.cart_product_qunatity);
            product_price = itemView.findViewById(R.id.cart_product_price);
            product_image = itemView.findViewById(R.id.cartItem_image);
            productdate = itemView.findViewById(R.id.cart_product_date);
            productTime = itemView.findViewById(R.id.cart_product_time);

            databaseReference = FirebaseDatabase.getInstance().getReference("Seller Orders").child(FirebaseAuth.getInstance()
            .getCurrentUser().getUid());

        }
//        public void bind(final GetSet item, final AdapterView.OnItemClickListener listener) {
//
//        }
    }
}
