package com.geekspace.beyikyol.Adapter;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Build;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.geekspace.beyikyol.Model.Ew_Klass;
import com.geekspace.beyikyol.R;

import java.util.ArrayList;

public class Ew_Adapter extends RecyclerView.Adapter<Ew_Adapter.ViewHolder> {
    ArrayList<Ew_Klass> ew_array = new ArrayList<>();
    Context context;
    int READ_STORAGE_PERMISSION_REQUEST_CODE = 1;

    public Ew_Adapter(ArrayList<Ew_Klass> ew_array, Context context) {
        this.ew_array = ew_array;
        this.context = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.phone_number_design, parent, false);
        return new Ew_Adapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        final Ew_Klass call = ew_array.get(position);
        Typeface Ping_medium = Typeface.createFromAsset(context.getAssets(), "fonts/Ping LCG Medium.otf");
        holder.cardView.setAnimation(AnimationUtils.loadAnimation(context,R.anim.fade_scale_animation));
        holder.cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                callFunction(call.getPhone_number());
            }
        });
        holder.phone_number.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                callFunction(call.getPhone_number());
            }
        });

        holder.place.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                callFunction(call.getPhone_number());
            }
        });

        holder.phone_number.setText(call.getPhone_number());
        holder.place.setText(call.getPlace());

        holder.phone_number.setTypeface(Ping_medium);
        holder.place.setTypeface(Ping_medium);
        holder.imageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                callFunction(call.getPhone_number());

//                    if (ActivityCompat.checkSelfPermission(context, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
//                        try {
//                            requestPermissionForReadExtertalStorage();
//                        } catch (Exception e) {
//                            e.printStackTrace();
//                        }
//                    } else {

//                    }

            }
        });

    }

    public void callFunction(String tel){
        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.setData(Uri.parse("tel:"+tel));
        context.startActivity(intent);
    }

    @Override
    public int getItemCount() {
        return ew_array.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        RelativeLayout cardView;
        TextView phone_number,place;
        ImageView imageButton;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            cardView=itemView.findViewById(R.id.cardview);
            phone_number=itemView.findViewById(R.id.tel);
            place=itemView.findViewById(R.id.place);
            imageButton=itemView.findViewById(R.id.call_button);
        }
    }
    public boolean checkPermissionForReadExtertalStorage() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            int result = context.checkSelfPermission(Manifest.permission.CALL_PHONE);
            return result == PackageManager.PERMISSION_GRANTED;
        }
        return false;
    }

    public void requestPermissionForReadExtertalStorage() throws Exception {
        try {
            ActivityCompat.requestPermissions((Activity) context, new String[]{Manifest.permission.CALL_PHONE},
                    READ_STORAGE_PERMISSION_REQUEST_CODE);
        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        }
    }




}
