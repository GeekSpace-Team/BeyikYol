package com.geekspace.beyikyol.Adapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.geekspace.beyikyol.Activity.Car.Add_Car;
import com.geekspace.beyikyol.Database.BenzinDB;
import com.geekspace.beyikyol.Database.CalyshmakDB;
import com.geekspace.beyikyol.Database.CarDB;
import com.geekspace.beyikyol.Model.MyCars;
import com.geekspace.beyikyol.R;
import com.geekspace.beyikyol.Database.RemontDB;
import com.geekspace.beyikyol.Utils.Utils;

import java.util.ArrayList;

public class baseadapter extends BaseAdapter {
    ArrayList<MyCars> myCars=new ArrayList<>();
    Context context;
    LayoutInflater inflater;
    TextView name;
    ImageView image;
    TextView benzin;
    Button add_car;
    RelativeLayout first;
    RelativeLayout second;

    ArrayList<String> history=new ArrayList<>();
    boolean s=true;

    CarDB carDB;
    public baseadapter(ArrayList<MyCars> myCars, Context context) {
        this.myCars = myCars;
        this.context = context;
    }

    @Override
    public int getCount() {
        return myCars.size();
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        inflater=LayoutInflater.from(context);
        View itemView=inflater.inflate(R.layout.car_info,parent,false);
        name=itemView.findViewById(R.id.name);
        image=itemView.findViewById(R.id.car_image);
        benzin=itemView.findViewById(R.id.benzin);
        second=itemView.findViewById(R.id.second);
        first=itemView.findViewById(R.id.first);
        add_car=itemView.findViewById(R.id.add_car);



        carDB=new CarDB(context);
        MyCars cars=myCars.get(position);
        if(cars.getId().equals("nothing")) {
            first.setVisibility(View.VISIBLE);
            second.setVisibility(View.GONE);
            add_car.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent=new Intent(context, Add_Car.class);
                    context.startActivity(intent);
                }
            });
        } else{
            first.setVisibility(View.GONE);
            second.setVisibility(View.VISIBLE);
            name.setText(cars.getName());
            Typeface trebu = Typeface.createFromAsset(context.getAssets(), "fonts/sylfaen.ttf");
            name.setTypeface(trebu);

            byte[] data=carDB.getImage(cars.getId());
            if(data != null){
                Bitmap bitmap = Utils.getImage(data);
                image.setImageBitmap(bitmap);
            } else{
                image.setImageResource(R.drawable.unnamed);
            }


            benzin.setText(context.getResources().getString(R.string.km_miles) + ": " + cars.getBenzin());
            benzin.setTypeface(trebu);
//
//            if(position%2==0){
//                second.setBackgroundResource(R.drawable.bank_card_4);
//            } else{
//                second.setBackgroundResource(R.drawable.bank_card_3);
//            }
//
//            if(position%3==0){
//                second.setBackgroundResource(R.drawable.bank_card_2);
//            }



        }



        BenzinDB benzinDB=new BenzinDB(context);
        CalyshmakDB calyshmakDB=new CalyshmakDB(context);
        RemontDB remontDB=new RemontDB(context);









//             Toast.makeText(context,cars.getId(),Toast.LENGTH_SHORT).show();
        try {

        } catch (Exception ignored){
            ignored.printStackTrace();
        }






        return itemView;
    }
}
