package com.olokart.vendors;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

public class CategoryAdapter extends RecyclerView.Adapter<CategoryAdapter.CartViewHolder>{

    Context context;
    ArrayList<GetSet> getSets;
    private List<GetSet> productsListt;
    private AdapterView.OnItemClickListener listener;

    public CategoryAdapter(List<GetSet> hList, AdapterView.OnItemClickListener listener) {
        this.productsListt = hList;
        this.listener = listener;
    }

    public CategoryAdapter(Context c, ArrayList<GetSet> h) {
        context = c;
        getSets = h;
    }

    @NonNull
    @Override
    public CategoryAdapter.CartViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new CategoryAdapter.CartViewHolder(LayoutInflater.from(context).inflate(R.layout.activity_activitygrid,
                parent, false));
    }
    @Override
    public void onBindViewHolder(@NonNull final CategoryAdapter.CartViewHolder holder, int position) {

        holder.bind(getSets.get(position), listener);
        holder.product_name.setText(getSets.get(position).getPcatname());
        holder.pcat = getSets.get(position).getPcatname();
        Picasso.get().load(getSets.get(position).getPcatimage()).into(holder.product_image);

    }

    @Override
    public int getItemCount() {
        return getSets.size();
    }
    public interface OnItemClickListener {
        void onItemClick(GetSet products);
    }

    public class CartViewHolder extends RecyclerView.ViewHolder {

        TextView product_name;
        ImageView product_image;
        String pcat, suba=" ", subb=" ", subc=" ", subd=" ", sube=" ", add;
        DatabaseReference databaseReference, todb, preRef;

        public CartViewHolder(View itemView) {

            super(itemView);
            product_name = itemView.findViewById(R.id.pcatName);
            product_image = itemView.findViewById(R.id.pcatImage);

            databaseReference = FirebaseDatabase.getInstance().getReference("All Categories");

            todb = FirebaseDatabase.getInstance().getReference("Seller Products")
                    .child(FirebaseAuth.getInstance().getCurrentUser().getUid());
            preRef = FirebaseDatabase.getInstance().getReference("Predefined Products");

        }
        public void bind( GetSet item,  AdapterView.OnItemClickListener listener) {
            itemView
                    .setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            databaseReference.child(pcat).addValueEventListener(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                    try {
                                        add = dataSnapshot.child("sub").getValue().toString();
                                        suba = dataSnapshot.child("suba").getValue().toString();
                                        subb = dataSnapshot.child("subb").getValue().toString();
                                        subc = dataSnapshot.child("subc").getValue().toString();
                                        subd = dataSnapshot.child("subd").getValue().toString();
                                        sube = dataSnapshot.child("sube").getValue().toString();

                                    } catch (Exception e) {
                                        e.printStackTrace();
                                    }
                                    CharSequence options[] = new CharSequence[]{
                                            add, suba, subb, subc, subd, sube
                                    };


                                    final AlertDialog.Builder builder = new AlertDialog.Builder(context);
                                    builder.setTitle("  Choose Sub-Category");
                                    builder.setItems(options, new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int i) {

                                            switch (i) {
                                                case 0:
                                                    ProgressDialog progressDialog = new ProgressDialog(context);
                                                    progressDialog.setCancelable(false);
                                                    progressDialog.setTitle("Uploading products");
                                                    progressDialog.setMessage("Please wait");
                                                    progressDialog.show();

                                                    moveFirebaseRecord(preRef.child(pcat), todb.child(pcat));
//
                                                    todb.child(pcat).addValueEventListener(new ValueEventListener() {
                                                        @Override
                                                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                                                            for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                                                                for (DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()) {
                                                                    todb.child(pcat).child(dataSnapshot.getKey()).child(dataSnapshot1.getKey())
                                                                            .child("suid").setValue(FirebaseAuth.getInstance().getCurrentUser().getUid());
                                                                }
                                                            }
                                                            // Toast.makeText(AddDefaultProducts.this, "Products added!", Toast.LENGTH_SHORT).show();
                                                        }

                                                        @Override
                                                        public void onCancelled(@NonNull DatabaseError error) {

                                                        }
                                                    });
                                                    progressDialog.dismiss();
                                            break;
                                                case 1:
                                                    if (suba.equals(" ")) {
                                                        break;
                                                    } else {
                                                        context.startActivity(new Intent(context, AddProductsActivity.class).putExtra("subcat", suba).putExtra("category", pcat));
                                                    }
                                                    break;
                                                case 2:
                                                    if (subb.equals(" ")) {
                                                        break;
                                                    } else {
                                                        context.startActivity(new Intent(context, AddProductsActivity.class).putExtra("subcat", subb).putExtra("category", pcat));
                                                    }

                                                    break;
                                                case 3:
                                                    if (subc.equals(" ")) {
                                                        break;
                                                    } else {
                                                        context.startActivity(new Intent(context, AddProductsActivity.class).putExtra("subcat", subc).putExtra("category", pcat));
                                                    }

                                                    break;
                                                case 4:
                                                    if (subd.equals(" ")) {
                                                        break;
                                                    } else {
                                                        context.startActivity(new Intent(context, AddProductsActivity.class).putExtra("subcat", subd).putExtra("category", pcat));
                                                    }

                                                    break;
                                                case 5:
                                                    if (sube.equals(" ")) {
                                                        break;
                                                    } else {
                                                        context.startActivity(new Intent(context, AddProductsActivity.class).putExtra("subcat", sube).putExtra("category", pcat));
                                                    }
                                                    break;
                                            }

                                        }
                                    });
                                    builder.show();
                                }

                                    @Override
                                public void onCancelled(@NonNull DatabaseError databaseError) {

                                }
                            });
                    }



                    });

        }
    }

    public void moveFirebaseRecord(final DatabaseReference fromPath, final DatabaseReference toPath) {
       // progressDialog.show();
        fromPath.addListenerForSingleValueEvent(new ValueEventListener()
        {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot)
            {
                toPath.setValue(dataSnapshot.getValue(), new DatabaseReference.CompletionListener() {
                    @Override
                    public void onComplete(@Nullable DatabaseError databaseError, @NonNull DatabaseReference databaseReference) {
                       // progressDialog.dismiss();
                        Toast.makeText(context, "Default Products added!", Toast.LENGTH_SHORT).show();
                    }
                });
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
               // progressDialog.dismiss();
            }
        });
    }

    public void filterList(ArrayList<GetSet> filteredList) {
        getSets = filteredList;
        notifyDataSetChanged();
    }


}