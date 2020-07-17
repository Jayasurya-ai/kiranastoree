package com.viba.olokart_vendors;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

public class HomeAdapter extends RecyclerView.Adapter<HomeAdapter.HomeViewHolder> {

    Context context;
    ArrayList<GetSet> product;
    private List<GetSet> GetSetListt;
    private AdapterView.OnItemClickListener listener;


    public HomeAdapter(List<GetSet> hList, AdapterView.OnItemClickListener listener) {
        this.GetSetListt = hList;
        this.listener = listener;
    }

    public HomeAdapter(Context c, ArrayList<GetSet> h) {
        context = c;
        product = h;
    }

    @NonNull
    @Override
    public HomeAdapter.HomeViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new HomeViewHolder(LayoutInflater.from(context).inflate(R.layout.home_layout,
                parent, false));
    }
    @Override
    public void onBindViewHolder(@NonNull final HomeAdapter.HomeViewHolder holder, int position) {

        holder.bind(product.get(position), listener);
        holder.productName=product.get(position).getProduct_name();
        holder.totalPricestr = product.get(position).getTotal_price();
        holder.product_name.setText(product.get(position).getProduct_name());


        Picasso.get().load(product.get(position).getProduct_image()).placeholder(R.drawable.rice).into(holder.imageView);

    }

    @Override
    public int getItemCount() {
        return product.size();
    }


    public interface OnItemClickListener {
        void onItemClick(GetSet GetSet);
    }

    public class HomeViewHolder extends RecyclerView.ViewHolder {

        TextView product_name, actualPrice, totalPrice,totalpricea,totalpriceb, actualQuant,product_quant;
        String productName, suid, totalPricestr,productDimen;
        ImageView imageView, moreHome;
        FirebaseAuth firebaseAuth;
        DatabaseReference databaseReference;
        ProgressBar progressBar;

        public HomeViewHolder(View itemView) {

            super(itemView);

            product_name = itemView.findViewById(R.id.show_ProductsName);
            imageView = itemView.findViewById(R.id.image_showProducts);
            firebaseAuth = FirebaseAuth.getInstance();

            databaseReference = FirebaseDatabase.getInstance().getReference("Seller Products").child(
                    firebaseAuth.getCurrentUser().getUid());

        }



        public void bind(final GetSet item, final AdapterView.OnItemClickListener listener) {
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    CharSequence options[]=new CharSequence[]{
                           "Edit", "Remove"
                    };
                    AlertDialog.Builder builder=new AlertDialog.Builder(context);
                    builder.setTitle("Product Options");
                    builder.setItems(options, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int i) {

                            switch (i){
                                case  0:

                                    context.startActivity(new Intent(context, SellerEditActivity.class).putExtra("product_name", productName));
                                    break;
                                case 1:
                                    product.clear();
                                    databaseReference.child(productName).removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task) {

                                            if(task.isSuccessful()){
                                                Toast.makeText(context, "Item Removed Succesfully", Toast.LENGTH_SHORT).show();
                                            }
                                        }
                                    });
                                    break;
                            }

                        }
                    });
                    builder.show();

                }
            });

        }
    }
    public void filterList(ArrayList<GetSet> filteredList) {
        product = filteredList;
        notifyDataSetChanged();
    }
}

