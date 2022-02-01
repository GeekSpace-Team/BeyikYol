package com.geekspace.beyikyol.Activity.Car;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.geekspace.beyikyol.Activity.App.Sazlamalar;
import com.geekspace.beyikyol.Database.CarDB;
import com.geekspace.beyikyol.Adapter.Cars_Adapter;
import com.geekspace.beyikyol.R;

import java.util.ArrayList;

public class My_Garage extends AppCompatActivity {
   ArrayList<Cars> carsArrayList=new ArrayList<>();
   Context context=this;
    CarDB carDB;
   ImageButton add_car;
   Button retry;
   LinearLayout empty;
    RecyclerView rec;
   TextView toolbar_title,my_cars;
    private static final int REQUEST_CODE=1;
    String languages="";
    String newLang="";
    boolean isFirst=false;
    TextView empty_tv;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
      //  getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_my__garage);
        SharedPreferences share = getSharedPreferences("Settings", Activity.MODE_PRIVATE);
        languages = share.getString("My_Lang", "");
        newLang = share.getString("My_Lang", "");
        Typeface Ping_medium = Typeface.createFromAsset(getAssets(), "fonts/Ping LCG Medium.otf");
        Typeface Ping_bold = Typeface.createFromAsset(getAssets(), "fonts/Ping LCG Bold.otf");
        Typeface Ping_regular = Typeface.createFromAsset(getAssets(), "fonts/Ping LCG Regular.otf");
        //Typeface trebu = Typeface.createFromAsset(context.getAssets(), "fonts/trebuc.ttf");
        TextView toolbar_title=findViewById(R.id.toolbar_title);
        //TextView my_cars=findViewById(R.id.my_cars);
        toolbar_title.setTypeface(Ping_bold);
       // my_cars.setTypeface(trebu);
        toolbar_title.setText(R.string.my_cars);
        ImageButton settings=findViewById(R.id.settings);
        settings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivityForResult(new Intent(context, Sazlamalar.class),REQUEST_CODE);
               // Animatoo.animateSwipeLeft(context);
            }
        });
        ImageButton back_btn=findViewById(R.id.back_btn);
        back_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });


        empty_tv=findViewById(R.id.empty_tv);


        empty_tv.setTypeface(Ping_medium);


        findViewById(R.id.share).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(My_Garage.this,ShareCar.class));
            }
        });







        add_car=findViewById(R.id.add_car);


        add_car.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
                startActivity(new Intent(My_Garage.this,Add_Car.class).putExtra("type","2"));
              //
                //
                //  Animatoo.animateSwipeLeft(My_Garage.this);
            }
        });

        select(context);


        }

        public void select(Context a){

            rec=((Activity) a).findViewById(R.id.cars);
            empty=((Activity) a).findViewById(R.id.empty);
            carDB=new CarDB(a);
            Cursor res=carDB.getAll();

            if(res.getCount()==0){
//                Toast.makeText(a,"No",Toast.LENGTH_SHORT).show();
                empty.setVisibility(View.VISIBLE);
                rec.setVisibility(View.GONE);

            } else {
                empty.setVisibility(View.GONE);
                rec.setVisibility(View.VISIBLE);
                carsArrayList.clear();
                while (res.moveToNext()) {
                    carsArrayList.add(new Cars(Integer.parseInt(res.getString(0)), res.getString(1), res.getString(2), "",res.getString(8)));

                }



            }

            final Cars_Adapter adapter = new Cars_Adapter(carsArrayList, a);
            LinearLayoutManager layoutManager = new LinearLayoutManager(a);
            rec.setLayoutManager(layoutManager);
//                adapter.notifyItemRangeChanged(0, adapter.getItemCount());
            runOnUiThread(new Runnable() {
                public void run() {
                    adapter.notifyDataSetChanged();
                }
            });
            rec.setAdapter(adapter);

            rec.scheduleLayoutAnimation();






    }

    @Override
    public void onBackPressed() {
        Intent intent=new Intent();
        setResult(RESULT_OK,intent);
        finish();
        // startActivity(exitIntent);
      //  Animatoo.animateSwipeRight(this);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if(requestCode==REQUEST_CODE){
            if(resultCode==RESULT_OK){
                // Toast.makeText(this, data.getStringExtra("result"), Toast.LENGTH_SHORT).show();
//
//                if(data.getStringExtra("result").equals("1")) {
//                    recreate();
//
//                }


                //   Toast.makeText(this, "OK", Toast.LENGTH_SHORT).show();
            }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    protected void onResume() {
        super.onResume();
        SharedPreferences share = getSharedPreferences("Settings", Activity.MODE_PRIVATE);
        newLang = share.getString("My_Lang", "");
        if(!newLang.equals(languages)){
            recreate();
        }

        if(isFirst){
            recreate();
            isFirst=false;
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        isFirst=true;

    }
}