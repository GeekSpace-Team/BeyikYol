package com.geekspace.beyikyol.Adapter;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.geekspace.beyikyol.Activity.Car.My_Costs;
import com.geekspace.beyikyol.R;
import com.geekspace.beyikyol.Model.SmallCar;

import java.util.ArrayList;

public class SmallCarAdapter extends RecyclerView.Adapter<SmallCarAdapter.ViewHolder> {
    Context context;
    ArrayList<SmallCar> smallCars = new ArrayList<>();
    private static int lastClickedPosition = -1;
    TextView oldtv = null;
    int isFirst = 1;
    RelativeLayout oldCard = null;
    String carId;
    String oldId;

    public SmallCarAdapter(Context context, ArrayList<SmallCar> smallCars, String carId, String oldId) {
        this.context = context;
        this.smallCars = smallCars;
        this.carId = carId;
        this.oldId = oldId;
    }

    @NonNull
    @Override
    public SmallCarAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.small_car, parent, false);
        return new SmallCarAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final SmallCarAdapter.ViewHolder holder, final int position) {
        final SmallCar smallCar = smallCars.get(position);
        holder.textView.setText(smallCar.getCarName());
        Typeface Ping_medium = Typeface.createFromAsset(context.getAssets(), "fonts/Ping LCG Medium.otf");
        holder.card.setAnimation(AnimationUtils.loadAnimation(context, R.anim.fade_scale_animation));
//        if (!carId.equals("")) {
//            if (smallCar.getCarId().equals(carId)) {
//                oldtv = holder.textView;
//                oldCard = holder.card;
//                holder.textView.setTextColor(Color.WHITE);
//                holder.card.setBackgroundResource(R.drawable.car_doly);
//
//            }
//        }
//
//        if (!oldId.equals("")) {
//            if (smallCar.getCarId().equals(oldId)) {
//                holder.textView.setTextColor(Color.BLACK);
//                holder.card.setBackgroundResource(R.drawable.car_des);
//                oldId=carId;
//            }
//        }
        holder.textView.setTypeface(Ping_medium);
        if ((oldId.equals("nothing")) && position==0) {
            oldtv = holder.textView;
            oldCard = holder.card;
            holder.textView.setTextColor(Color.WHITE);
            holder.card.setBackgroundResource(R.drawable.car_doly);
        } else if(oldId.equals(smallCar.getCarId())){
            oldtv = holder.textView;
            oldCard = holder.card;
            holder.textView.setTextColor(Color.WHITE);
            holder.card.setBackgroundResource(R.drawable.car_doly);
        }
        holder.card.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                My_Costs my_costs = new My_Costs();

                if(oldtv!=null) {
                    oldtv.setTextColor(context.getResources().getColor(R.color.first));
                    oldCard.setBackgroundResource(R.drawable.car_des);
                }
                oldtv = holder.textView;
                oldCard = holder.card;
                holder.textView.setTextColor(Color.WHITE);
                holder.card.setBackgroundResource(R.drawable.car_doly);
                my_costs.doldur(context, smallCar.getCarId(), oldId);

            }
        });


    }

    @Override
    public int getItemCount() {
        return smallCars.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView textView;
        RelativeLayout card;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            textView = itemView.findViewById(R.id.carName);
            card = itemView.findViewById(R.id.card);
        }
    }
}
