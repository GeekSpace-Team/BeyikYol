package com.geekspace.beyikyol.Adapter;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.geekspace.beyikyol.Activity.App.MainMenu;
import com.geekspace.beyikyol.Activity.Car.Add_Car;
import com.geekspace.beyikyol.Database.BenzinDB;
import com.geekspace.beyikyol.Database.CalyshmakDB;
import com.geekspace.beyikyol.Database.CarDB;
import com.geekspace.beyikyol.Model.MyCars;
import com.geekspace.beyikyol.R;
import com.geekspace.beyikyol.Database.RemontDB;
import com.geekspace.beyikyol.Utils.Utils;

import java.util.ArrayList;

public class MyCarsAdapter extends RecyclerView.Adapter<MyCarsAdapter.ViewHolder> {
    ArrayList<MyCars> myCars = new ArrayList<>();
    Context context;
    ArrayList<String> history = new ArrayList<>();
    boolean s = true;


    CarDB carDB;

    public MyCarsAdapter(ArrayList<MyCars> myCars, Context context) {
        this.myCars = myCars;
        this.context = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.car_info, parent, false);
        return new MyCarsAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        carDB = new CarDB(context);
        MyCars cars = myCars.get(position);
        Typeface Ping_regular = Typeface.createFromAsset(context.getAssets(), "fonts/Ping LCG Regular.otf");
        Typeface Ping_medium = Typeface.createFromAsset(context.getAssets(), "fonts/Ping LCG Medium.otf");
        holder.add_info.setTypeface(Ping_medium);
        if (cars.getId().equals("nothing")) {
            holder.first.setVisibility(View.VISIBLE);
            holder.second.setVisibility(View.GONE);
            holder.add.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(context, Add_Car.class);
                    intent.putExtra("type","1");
                    context.startActivity(intent);
                }
            });

            holder.add_car.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(context, Add_Car.class);
                    intent.putExtra("type","1");
                    context.startActivity(intent);
                }
            });
        } else {
            holder.first.setVisibility(View.GONE);
            holder.second.setVisibility(View.VISIBLE);
            holder.name.setText(cars.getName());

            holder.name.setTypeface(Ping_medium);

            byte[] data = carDB.getImage(cars.getId());
            if (data != null) {
                Bitmap bitmap = Utils.getImage(data);
                holder.image.setImageBitmap(bitmap);
            } else {
                holder.image.setImageResource(R.drawable.unnamed);
            }


            holder.benzin.setText(cars.getBenzin());
            holder.benzin.setTypeface(Ping_regular);
            holder.in_benzin.setTypeface(Ping_regular);
            holder.in_benzin_title.setTypeface(Ping_regular);
            holder.in_calyshmak.setTypeface(Ping_regular);
            holder.in_remont.setTypeface(Ping_regular);
            holder.benzin_title.setTypeface(Ping_regular);
            holder.in_calyshmak_title.setTypeface(Ping_regular);
            holder.in_remont_title.setTypeface(Ping_regular);

            if(position==myCars.size()) {
               // Toast.makeText(context, "equals", Toast.LENGTH_SHORT).show();
                MainMenu.get().getMore().setVisibility(View.GONE);
            }



        }

//       Toast.makeText(context,cars.getyValues().size()+"",Toast.LENGTH_SHORT).show();
        BenzinDB benzinDB = new BenzinDB(context);
        CalyshmakDB calyshmakDB = new CalyshmakDB(context);
        RemontDB remontDB = new RemontDB(context);


        Cursor benzin = benzinDB.getSt(cars.getId());
        if (benzin.getCount() != 0) {
            while (benzin.moveToNext()) {
                holder.in_benzin.setText("KM:" + benzin.getString(3) + "\n" + benzin.getString(2) + " litre");
            }
        } else {
            holder.in_benzin.setText("...\n");
        }

        Cursor calysh = calyshmakDB.getSt(cars.getId());

        if (calysh.getCount() != 0) {
            while (calysh.moveToNext()) {
                holder.in_calyshmak.setText("KM:" + calysh.getString(8));
            }
        } else {
            holder.in_calyshmak.setText("...");
        }


        Cursor remont = remontDB.getSt(cars.getId());
        if (remont.getCount() != 0) {

            while (remont.moveToNext()) {
                holder.in_remont.setText("KM:" + remont.getString(6));
            }

        } else {
            holder.in_remont.setText("...");
        }


        holder.setIsRecyclable(true);


    }

    @Override
    public int getItemCount() {
        return myCars.size();
    }


    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView name;
        ImageView image;
        TextView benzin, in_benzin, in_calyshmak, in_remont;
        TextView benzin_title, in_benzin_title, in_calyshmak_title, in_remont_title,add_info;
        ImageButton add_car;
        LinearLayout add;
        RelativeLayout first;
        RelativeLayout second;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            name = itemView.findViewById(R.id.name);
            image = itemView.findViewById(R.id.car_image);
            benzin = itemView.findViewById(R.id.benzin);
            benzin_title = itemView.findViewById(R.id.benzin_title);
            second = itemView.findViewById(R.id.second);
            first = itemView.findViewById(R.id.first);
            add_car = itemView.findViewById(R.id.add_car);
            in_benzin = itemView.findViewById(R.id.in_sonky_benzin);
            in_benzin_title = itemView.findViewById(R.id.in_sonky_benzin_title);
            in_calyshmak = itemView.findViewById(R.id.in_sonky_chalyshyk);
            in_calyshmak_title = itemView.findViewById(R.id.in_sonky_chalyshyk_title);
            in_remont = itemView.findViewById(R.id.in_sonky_remont);
            in_remont_title = itemView.findViewById(R.id.in_sonky_remont_title);
            add_info = itemView.findViewById(R.id.add_info);
            add = itemView.findViewById(R.id.add);

        }
    }

    public boolean isHas(String id) {
        for (int i = 0; i < history.size(); i++) {
            if (id.equals(history.get(i))) {
                return true;
            }
        }
        return false;
    }
}
