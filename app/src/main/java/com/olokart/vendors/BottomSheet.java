package com.olokart.vendors;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.HashMap;

public class BottomSheet extends BottomSheetDialogFragment {

    public BottomSheet() {
    }


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        final View view1 = inflater.inflate(R.layout.minm_order,container,false);
        TextView ok = view1.findViewById(R.id.Ok);

        ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String checkstr="";
                String minimumorder;
                Spinner spin= view1.findViewById(R.id.spin_min_order);
                CheckBox homedelivery= view1.findViewById(R.id.check_home_Delivery);
                CheckBox pickup=view1.findViewById(R.id.check_pick_up);



                minimumorder=spin.getSelectedItem().toString();

                   if((homedelivery.isChecked()) && (pickup.isChecked())){
                    checkstr="Home delivery and Pick up";
                    HashMap<String,Object> savemap=new HashMap<>();
                    savemap.put("minorder",minimumorder);
                    savemap.put("delivery",checkstr);
                    DatabaseReference databaseReference= FirebaseDatabase.getInstance().getReference("Sellers").child(FirebaseAuth.getInstance().getCurrentUser().getUid());
                    databaseReference.updateChildren(savemap);
                    Toast.makeText(getContext(), "Info Saved...", Toast.LENGTH_SHORT).show();
                    startActivity(new Intent(getActivity(), SellerAddActivity.class));
                    getActivity().finish();
                }

          else if(homedelivery.isChecked()){
            checkstr="Home delivery";
            HashMap<String,Object> savemap=new HashMap<>();
            savemap.put("minorder",minimumorder);
            savemap.put("delivery",checkstr);
            DatabaseReference databaseReference= FirebaseDatabase.getInstance().getReference("Sellers").child(FirebaseAuth.getInstance().getCurrentUser().getUid());
            databaseReference.updateChildren(savemap);
            Toast.makeText(getContext(), "Info Saved...", Toast.LENGTH_SHORT).show();
            startActivity(new Intent(getActivity(), SellerAddActivity.class));
            getActivity().finish();
           }
          else if(pickup.isChecked()){
            checkstr="Pick up";
            HashMap<String,Object> savemap=new HashMap<>();
            savemap.put("minorder",minimumorder);
            savemap.put("delivery",checkstr);
            DatabaseReference databaseReference= FirebaseDatabase.getInstance().getReference("Sellers").child(FirebaseAuth.getInstance().getCurrentUser().getUid());
            databaseReference.updateChildren(savemap);
            Toast.makeText(getContext(), "Info Saved...", Toast.LENGTH_SHORT).show();
            startActivity(new Intent(getActivity(), SellerAddActivity.class));
            getActivity().finish();
        }

        else if (!(homedelivery.isChecked() && pickup.isChecked())){
            Toast.makeText(getActivity(), "Please choose any option!", Toast.LENGTH_SHORT).show();
        }
            }
        });

        return view1;
    }

}
