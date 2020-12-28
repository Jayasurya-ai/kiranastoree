package com.olokart.vendors;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

public class AddProductsrAdapter extends RecyclerView.Adapter<AddProductsrAdapter.CartViewHolder> {

    Context context;
    ArrayList<GetSet> product;
    private List<GetSet> GetSetListt;
    private AdapterView.OnItemClickListener listener;

    DatabaseReference cartListRef = FirebaseDatabase.getInstance().getReference().child("Cart List");


    public AddProductsrAdapter(List<GetSet> hList, AdapterView.OnItemClickListener listener) {
        this.GetSetListt = hList;
        this.listener = listener;
    }

    public AddProductsrAdapter(Context c, ArrayList<GetSet> h) {
        context = c;
        product = h;
    }

    @NonNull
    @Override
    public AddProductsrAdapter.CartViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new CartViewHolder(LayoutInflater.from(context).inflate(R.layout.addproducts_layout,
                parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull final AddProductsrAdapter.CartViewHolder holder, int position) {


        holder.bind(product.get(position), listener);
        holder.productName = product.get(position).getProduct_name();
        holder.subcat = product.get(position).getProduct_subcat();
        holder.productCat = product.get(position).getProduct_cat();
        holder.setImageProduct = product.get(position).getProduct_image();
        holder.product_name.setText(holder.productName);
        Picasso.get().load(product.get(position).getProduct_image()).placeholder(R.drawable.shopping_cart).into(holder.imageView);


    }

    @Override
    public int getItemCount() {
        return product.size();
    }

    public interface OnItemClickListener {
        void onItemClick(GetSet GetSet);
    }

    public class CartViewHolder extends RecyclerView.ViewHolder {

        TextView product_name;
        String productName, setImageProduct, subcat, productCat;
        ImageView imageView;
        Button add;

        public CartViewHolder(View itemView) {

            super(itemView);

            product_name = itemView.findViewById(R.id.addProductName);
            imageView = itemView.findViewById(R.id.addProductImage);
            add= itemView.findViewById(R.id.addQuantitybtn);
        }

        public void bind(final GetSet item, final AdapterView.OnItemClickListener listener) {

            add.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                   context.startActivity(new Intent(context, PredefinedProducts.class).putExtra("pname", productName).putExtra("pimage", setImageProduct)
                   .putExtra("pcat", productCat).putExtra("psubcat", subcat));
                }
            });
        }
    }
    public void filterList(ArrayList<GetSet> filteredList) {
        product = filteredList;
        notifyDataSetChanged();
    }
}


