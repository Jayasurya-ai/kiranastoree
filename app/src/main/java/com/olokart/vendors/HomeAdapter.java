package com.olokart.vendors;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
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

        try {

            holder.bind(product.get(position), listener);
            holder.productName = product.get(position).getProduct_name();
            holder.totalPricestr = product.get(position).getTotal_price();
            holder.product_name.setText(product.get(position).getProduct_name());

            holder.productCat = product.get(position).getProduct_cat();
            holder.productSubcat = product.get(position).getProduct_subcat();
//
            holder.pimage = product.get(position).getProduct_image();
            holder.state = product.get(position).getProduct_state();

            if (holder.state.equals("activated")) {
                holder.sw1.setChecked(true);
                holder.stripGreen.setVisibility(View.VISIBLE);
                holder.stripRed.setVisibility(View.GONE);
            } else {
                holder.sw1.setChecked(false);
                holder.stripRed.setVisibility(View.VISIBLE);
                holder.stripGreen.setVisibility(View.GONE);
            }

        } catch (Exception e) {
            e.printStackTrace();
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

        TextView product_name;
        String productName, state, totalPricestr, productCat, productSubcat, pimage;
        ImageView stripGreen, stripRed;
        FirebaseAuth firebaseAuth;
        DatabaseReference databaseReference;
        Switch sw1;
        ArrayList<GetSet> getSets;

        public HomeViewHolder(View itemView) {

            super(itemView);

            product_name = itemView.findViewById(R.id.show_ProductsName);
            //imageView = itemView.findViewById(R.id.image_showProducts);
            sw1 = (Switch) itemView.findViewById(R.id.switch1);
            firebaseAuth = FirebaseAuth.getInstance();
            stripGreen = itemView.findViewById(R.id.stripgreen);
            stripRed = itemView.findViewById(R.id.stripred);

            databaseReference = FirebaseDatabase.getInstance().getReference("Seller Products").child(
                    firebaseAuth.getCurrentUser().getUid());

            getSets = new ArrayList<>();
            // getSets.clear();

        }


        public void bind(final GetSet item, final AdapterView.OnItemClickListener listener) {

            sw1.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    if (isChecked) {
                        HashMap<String, Object> pushmap = new HashMap<>();
                        pushmap.put("product_state", "activated");
                        databaseReference.child(productCat).child(productSubcat).child(productName).updateChildren(pushmap);

                    } else {
                        HashMap<String, Object> pushmap = new HashMap<>();
                        pushmap.put("product_state", "deactivated");
                        databaseReference.child(productCat).child(productSubcat).child(productName).updateChildren(pushmap);

                    }
                }//
            });

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    CharSequence options[] = new CharSequence[]{
                           "Edit", "Remove"
                    };


                    final AlertDialog.Builder builder = new AlertDialog.Builder(context);
                    builder.setTitle("  Choose Option");
                    builder.setItems(options, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int i) {

                            switch (i) {
                                case 0:

                                    context.startActivity(new Intent(context, EditPredefinedProducts.class)
                                    .putExtra("pcat", productCat).putExtra("psubcat", productSubcat)
                                    .putExtra("pimage", pimage).putExtra("pname", productName));
                                    break;
                                case 1:

                                    databaseReference.child(productCat).child(productSubcat).child(productName).removeValue(new DatabaseReference.CompletionListener() {
                                        @Override
                                        public void onComplete(@Nullable DatabaseError databaseError, @NonNull DatabaseReference databaseReference) {
                                            Toast.makeText(context, "Product removed!", Toast.LENGTH_SHORT).show();
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

