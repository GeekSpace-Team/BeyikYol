package com.geekspace.beyikyol.Activity.Car;

import android.app.ActionBar;
import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.Typeface;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.geekspace.beyikyol.Activity.App.Sazlamalar;
import com.geekspace.beyikyol.Database.CarDB;
import com.geekspace.beyikyol.CustomView.Custom_Spinner;
import com.geekspace.beyikyol.R;
import com.geekspace.beyikyol.Utils.Utils;
import com.geekspace.beyikyol.Model.Year;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.List;

public class UpdateCar extends AppCompatActivity {
    ArrayList<Year> year=new ArrayList<>();
    ArrayList<Year> marka=new ArrayList<>();
    ArrayList<Year> model=new ArrayList<>();
    ArrayList<Year> type=new ArrayList<>();
    ArrayList<Year> Engine_power=new ArrayList<>();
    ArrayList<Year> transmission=new ArrayList<>();
    ArrayList<Year> engine_type=new ArrayList<>();
    CheckBox add_image_st;
    ImageView car_image;
    EditText miles,vin,phone_number;
    Uri uri;
    int isModel=0;
    RelativeLayout add_picture;
    TextView tv_add_picture,region;
    String id;
    Button save;
    private static  final int GALLERY_INTENT=2;
    Dialog dialog;
    String insertMarka,inserModel,insertYear,insertEnginePower,insertEngineType,insertTransmission,insertMiles,insertImage,insertVin,insertPhoneNumber,insertType,temp_image;
    String selectMarka,selectModel,selectYear,selectEnginePower,selectEngineType,selectTransmission,selectMiles,selectImage="",selectVin,selectPhoneNumber,selectType,select_temp_image;
    Spinner yearSpinner,markaSpinner,modelSpinner,typesSpinner,engine_typeSpinner,power,transmissionSpinner;
    String languages="";
    String newLang="";

    TextView one,two,three,four,five,six,seven,eight,nine,ten;

    Bitmap carBitmap;

    LinearLayout placeholoder;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
       // getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_update_car);
        Typeface Ping_medium = Typeface.createFromAsset(getAssets(), "fonts/Ping LCG Medium.otf");
        Typeface Ping_bold = Typeface.createFromAsset(getAssets(), "fonts/Ping LCG Bold.otf");
        Typeface Ping_regular = Typeface.createFromAsset(getAssets(), "fonts/Ping LCG Regular.otf");
        SharedPreferences share = getSharedPreferences("Settings", Activity.MODE_PRIVATE);
        languages = share.getString("My_Lang", "");
        newLang = share.getString("My_Lang", "");
        Intent intent=getIntent();
        id=intent.getStringExtra("id");
        CarDB carDB=new CarDB(this);
        Cursor cursor=carDB.getSelect(id);
        if(cursor.getCount()==0){
            finish();
            return;
        } else{
            while(cursor.moveToNext()){
                if(cursor.getString(0).equals(id)) {
                    selectMarka = cursor.getString(1);
                    selectModel = cursor.getString(2);
                    selectYear = cursor.getString(7);
                    selectEnginePower = cursor.getString(3);
                    selectEngineType = cursor.getString(4);
                    selectTransmission = cursor.getString(6);
                    selectMiles = cursor.getString(8);
                    selectImage = "";
                    selectVin = cursor.getString(9);
                    selectPhoneNumber = cursor.getString(10);
                    selectType = cursor.getString(5);
                }
            }
        }



        initYear();
        initMarka();
        initType();
        initengine_type();
        initPower();
        initTransmission();
        yearSpinner=findViewById(R.id.yearSpinner);
        markaSpinner=findViewById(R.id.marka);
        modelSpinner=findViewById(R.id.model);
        typesSpinner=findViewById(R.id.type);
        tv_add_picture=findViewById(R.id.tv_add_picture);
        add_picture=findViewById(R.id.add_picture);
        engine_typeSpinner=findViewById(R.id.engine_type);
        power=findViewById(R.id.power);
        transmissionSpinner=findViewById(R.id.transmission);
        add_image_st=findViewById(R.id.add_image_st);
        car_image=findViewById(R.id.car_image);
        miles=findViewById(R.id.miles);
        vin=findViewById(R.id.vin);
        phone_number=findViewById(R.id.phone_number);
        save=findViewById(R.id.save);
        region = findViewById(R.id.region);


        one=findViewById(R.id.one);
        two=findViewById(R.id.two);
        three=findViewById(R.id.three);
        four=findViewById(R.id.four);
        five=findViewById(R.id.five);
        six=findViewById(R.id.six);
        seven=findViewById(R.id.seven);
        eight=findViewById(R.id.eight);
        nine=findViewById(R.id.nine);
        ten=findViewById(R.id.ten);
        placeholoder=findViewById(R.id.placeholoder);



        one.setTypeface(Ping_regular);
        two.setTypeface(Ping_regular);
        three.setTypeface(Ping_regular);
        four.setTypeface(Ping_regular);
        five.setTypeface(Ping_regular);
        six.setTypeface(Ping_regular);
        seven.setTypeface(Ping_regular);
        eight.setTypeface(Ping_regular);
        nine.setTypeface(Ping_regular);
        ten.setTypeface(Ping_regular);

        miles.setTypeface(Ping_medium);
        vin.setTypeface(Ping_medium);
        phone_number.setTypeface(Ping_medium);
        save.setTypeface(Ping_medium);
        region.setTypeface(Ping_medium);
        add_image_st.setTypeface(Ping_regular);

        if(selectPhoneNumber.length()>4){
            phone_number.setText(selectPhoneNumber.substring(4));
        }

        miles.setText(selectMiles);
        vin.setText(selectVin);

        CarDB db=new CarDB(this);
        byte[] data=carDB.getImage(id);
        if(data != null){
            Bitmap bitmap = Utils.getImage(data);
            car_image.setImageBitmap(bitmap);
            carBitmap=bitmap;
            insertImage="123";
        } else{
            car_image.setImageResource(R.drawable.unnamed);
            insertImage="";
        }
        temp_image=selectImage;
        insertImage=selectImage;


        add_image_st.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked){
                    add_picture.setVisibility(View.VISIBLE);
                    insertImage=temp_image;
                } else{
                    add_picture.setVisibility(View.GONE);
                    insertImage="";
                }
            }
        });

        car_image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(Intent.ACTION_PICK);
                intent.setType("image/*");
                startActivityForResult(intent,GALLERY_INTENT);
            }
        });

        add_picture.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(Intent.ACTION_PICK);
                intent.setType("image/*");
                startActivityForResult(intent,GALLERY_INTENT);
            }
        });

        tv_add_picture.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(Intent.ACTION_PICK);
                intent.setType("image/*");
                startActivityForResult(intent,GALLERY_INTENT);
            }
        });




        Custom_Spinner adapter = new Custom_Spinner(this, year);
        yearSpinner.setAdapter(adapter);

        Custom_Spinner marka_adapter=new Custom_Spinner(this,marka);
        markaSpinner.setAdapter(marka_adapter);

        Custom_Spinner type_adapter=new Custom_Spinner(this,type);
        typesSpinner.setAdapter(type_adapter);

        Custom_Spinner engine_type_adapter=new Custom_Spinner(this,engine_type);
        engine_typeSpinner.setAdapter(engine_type_adapter);

        Custom_Spinner engine_power_adapter=new Custom_Spinner(this,Engine_power);
        power.setAdapter(engine_power_adapter);

        Custom_Spinner transmission_adapter=new Custom_Spinner(this,transmission);
        transmissionSpinner.setAdapter(transmission_adapter);

        transmissionSpinner.setSelection(Integer.parseInt(selectTransmission));
        engine_typeSpinner.setSelection(Integer.parseInt(selectEngineType));
        typesSpinner.setSelection(Integer.parseInt(selectType));

        markaSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                Year select_marka=marka.get(position);
                insertMarka=select_marka.getValue();
                if(isModel==0){
                    isModel=1;
                    model.add(new Year(selectModel,R.drawable.about_icon));
                } else{
                    model.clear();
                }
                initModel(select_marka.getValue());

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        model.add(new Year(selectModel,R.drawable.about_icon));


        modelSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                Year select=model.get(position);
                inserModel=select.getValue();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        engine_typeSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                Year select=engine_type.get(position);
                insertEngineType=position+"";
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        power.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                Year select=Engine_power.get(position);
                insertEnginePower=select.getValue();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        typesSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                Year select=type.get(position);
                insertType=position+"";
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });


        yearSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                Year select=year.get(position);
                insertYear=select.getValue();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        transmissionSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                Year select=transmission.get(position);
                insertTransmission=position+"";
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });



        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                insertMiles=miles.getText().toString();
                insertPhoneNumber=phone_number.getText().toString();
                insertVin=vin.getText().toString();

                if(insertPhoneNumber.trim().length()!=0){
                    insertPhoneNumber="+993"+insertPhoneNumber;
                }

                if(insertMarka.isEmpty()){
                    ShowDialog("no",getResources().getString(R.string.error_title),getResources().getString(R.string.error1));
                    return;
                } if(inserModel.isEmpty()){
                    ShowDialog("no",getResources().getString(R.string.error_title),getResources().getString(R.string.error2));
                    return;
                } if(insertEnginePower.isEmpty()){
                    ShowDialog("no",getResources().getString(R.string.error_title),getResources().getString(R.string.error6));
                    return;
                } if(insertEngineType.isEmpty()){
                    ShowDialog("no",getResources().getString(R.string.error_title),getResources().getString(R.string.error5));
                    return;
                } if(insertTransmission.isEmpty()){
                    ShowDialog("no",getResources().getString(R.string.error_title),getResources().getString(R.string.error7));
                    return;
                } if(insertType.isEmpty()){
                    ShowDialog("no",getResources().getString(R.string.error_title),getResources().getString(R.string.error3));
                    return;
                } if(insertYear.isEmpty()){
                    ShowDialog("no",getResources().getString(R.string.error_title),getResources().getString(R.string.error4));
                    return;
                } if(insertMiles.isEmpty()){
                    ShowDialog("no",getResources().getString(R.string.error_title),getResources().getString(R.string.error8));
                    return;
                } if(insertVin.length()!=17){
                    ShowDialog("no",getResources().getString(R.string.error_title),getResources().getString(R.string.error9));
                    return;
                }
//                if(add_image_st.isChecked() && insertImage.isEmpty()){
//                    ShowDialog("no",getResources().getString(R.string.error_title2),getResources().getString(R.string.surat_bosh));
//                    return;
//                }

                CarDB db=new CarDB(UpdateCar.this);
                if(insertImage.isEmpty() && !add_image_st.isChecked()){
                    car_image.setImageBitmap(carBitmap);
                }
                Bitmap bitmap=((BitmapDrawable)car_image.getDrawable()).getBitmap();
                boolean isInsert=db.updateData(id,insertMarka,inserModel,insertEnginePower,insertEngineType,insertType,insertTransmission,insertYear,insertMiles,insertVin,insertPhoneNumber,Utils.getBytes(bitmap));
                if(isInsert){
                    onBackPressed();
                } else{
                    ShowDialog("no",getResources().getString(R.string.error_title2),getResources().getString(R.string.error_title3));
                }


            }
        });


       // Typeface trebu = Typeface.createFromAsset(getAssets(), "fonts/trebuc.ttf");
        TextView toolbar_title=findViewById(R.id.toolbar_title);
        toolbar_title.setTypeface(Ping_bold);
        toolbar_title.setText(getResources().getString(R.string.ulag_u));
        ImageButton settings=findViewById(R.id.settings);
        settings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(UpdateCar.this, Sazlamalar.class));
               // Animatoo.animateSwipeLeft(UpdateCar.this);
            }
        });
        ImageButton back_btn=findViewById(R.id.back_btn);
        back_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
    }


    @Override
    public void onBackPressed() {
        finish();
        startActivity(new Intent(UpdateCar.this,My_Garage.class));
      //  Animatoo.animateSwipeRight(this);
    }

    private void ShowDialog(String yes, String t1, String t2) {
        Button back;
        TextView tv1,tv2;
        ImageView status;


        dialog=new Dialog(this);
        dialog.setContentView(R.layout.custom_dialog);
        dialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        Window window=dialog.getWindow();
        window.setGravity(Gravity.CENTER);
        window.getAttributes().windowAnimations=R.style.DialogAnimation;
        Typeface Ping_medium = Typeface.createFromAsset(getAssets(), "fonts/Ping LCG Medium.otf");
        back=dialog.findViewById(R.id.back);
        tv1=dialog.findViewById(R.id.t1);
        tv2=dialog.findViewById(R.id.t2);
        status=dialog.findViewById(R.id.status);


        tv1.setText(t1);
        tv2.setText(t2);
        tv1.setTypeface(Ping_medium);
        tv2.setTypeface(Ping_medium);
        back.setTypeface(Ping_medium);
//        if(yes.equals("yes")){
////            status.setImageResource(R.drawable.smile);
////            tv1.setTextColor(Color.GREEN);
////            back.setBackgroundResource(R.drawable.green);
//
//        } else if(yes.equals("no")){
////            status.setImageResource(R.drawable.sad_emoji);
////            tv1.setTextColor(Color.RED);
////            back.setBackgroundResource(R.drawable.r_s);
//        }
//
//        if(yes.equals("info")){
////            status.setImageResource(R.drawable.costs_icon);
////            tv1.setTextColor(Color.GREEN);
////            back.setBackgroundResource(R.drawable.green);
//        }

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

        dialog.setCancelable(true);
        window.setLayout(ActionBar.LayoutParams.WRAP_CONTENT,ActionBar.LayoutParams.WRAP_CONTENT);
        dialog.show();

    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);



        if (requestCode == GALLERY_INTENT && resultCode == RESULT_OK) {
            tv_add_picture.setVisibility(View.GONE);
            placeholoder.setVisibility(View.GONE);
            uri = data.getData();
            car_image.setVisibility(View.VISIBLE);
            Glide.with(UpdateCar.this).load(uri).placeholder(R.drawable.error).into(car_image);
            temp_image = data.getDataString();
            insertImage = data.getDataString();

        }
    }


    private void initYear(){

        year.clear();
        year.add(new Year(selectYear,R.drawable.year));
        Calendar calendar=Calendar.getInstance();
        int y= calendar.get(Calendar.YEAR);
        for(int i=1980;i<=y;i++){

            year.add(new Year(String.valueOf(i),R.drawable.year));
        }
    }

    private void initMarka(){
        marka.clear();
        marka.add(new Year(selectMarka,R.drawable.about_icon));
        BufferedReader reader = null;
        try {
            reader = new BufferedReader(
                    new InputStreamReader(getAssets().open("marka/file.txt")));
            String mLine;
            while ((mLine = reader.readLine()) != null) {
                if(!mLine.equals(selectMarka))
                marka.add(new Year(mLine,R.drawable.about_icon));
            }

        } catch (IOException e) {
            //log the exception
        } finally {
            if (reader != null) {
                try {
                    reader.close();
                } catch (IOException e) {
                    //log the exception
                }
            }
        }

    }

    private void initModel(String marka){


        BufferedReader reader = null;
        try {
            reader = new BufferedReader(
                    new InputStreamReader(getAssets().open("models/"+marka+".txt")));
            String mLine;
            while ((mLine = reader.readLine()) != null) {
                if(!mLine.isEmpty()) {
                    model.add(new Year(mLine, R.drawable.about_icon));
                }
            }

        } catch (IOException e) {
            //log the exception
            model.add(new Year("Null",R.drawable.about_icon));
        } finally {
            if (reader != null) {
                try {
                    reader.close();
                } catch (IOException e) {
                    //log the exception
                }
            }
            Custom_Spinner model_adapter=new Custom_Spinner(this,model);
            modelSpinner.setAdapter(model_adapter);
        }
    }

    private void initType(){
      //  type.add(new Year(selectType,R.drawable.f));
        if(languages.equals("en")) {
            List<String> temp = Arrays.asList(getResources().getStringArray(R.array.types_en));
            for(int i=0;i<temp.size();i++){
                type.add(new Year(temp.get(i),R.drawable.about_icon));
            }
        }
        if(languages.equals("tm")) {
            List<String> temp = Arrays.asList(getResources().getStringArray(R.array.types_tm));
            for(int i=0;i<temp.size();i++){
                type.add(new Year(temp.get(i),R.drawable.about_icon));
            }
        }
        if(languages.equals("ru")) {
            List<String> temp = Arrays.asList(getResources().getStringArray(R.array.types_ru));
            for(int i=0;i<temp.size();i++){
                type.add(new Year(temp.get(i),R.drawable.about_icon));
            }
        }

    }

    private void initengine_type(){
        engine_type.clear();
      //  engine_type.add(new Year(selectEngineType,R.drawable.c));
        BufferedReader reader = null;
        try {

            reader = new BufferedReader(new InputStreamReader(getAssets().open("engine_types_"+languages+".txt")));

            String mLine;
            while ((mLine = reader.readLine()) != null) {
                engine_type.add(new Year(mLine,R.drawable.about_icon));
            }

        } catch (IOException e) {
            //log the exception
        } finally {
            if (reader != null) {
                try {
                    reader.close();
                } catch (IOException e) {
                    //log the exception
                }
            }

        }
    }

    private void initPower(){
        Engine_power.add(new Year(selectEnginePower,R.drawable.about_icon));
        for(float p = (float) 0.5; p<=5.1; p+=0.1){

            Engine_power.add(new Year(String.valueOf(p).substring(0,3),R.drawable.about_icon));
        }
    }

    private void initTransmission(){
      //  transmission.add(new Year(selectTransmission,R.drawable.g));
        if(languages.equals("en")) {
            List<String> temp = Arrays.asList(getResources().getStringArray(R.array.transmission_en));
            for (int i = 0; i < temp.size(); i++) {
                transmission.add(new Year(temp.get(i), R.drawable.about_icon));
            }
        }
        if(languages.equals("tm")) {
            List<String> temp = Arrays.asList(getResources().getStringArray(R.array.transmission_tm));
            for (int i = 0; i < temp.size(); i++) {
                transmission.add(new Year(temp.get(i), R.drawable.about_icon));
            }
        }
        if(languages.equals("ru")) {
            List<String> temp = Arrays.asList(getResources().getStringArray(R.array.transmission_ru));
            for (int i = 0; i < temp.size(); i++) {
                transmission.add(new Year(temp.get(i), R.drawable.about_icon));
            }
        }
    }


    protected void onResume() {
        super.onResume();
        SharedPreferences share = getSharedPreferences("Settings", Activity.MODE_PRIVATE);
        newLang = share.getString("My_Lang", "");
        if(!newLang.equals(languages)){
            recreate();
        }
    }
}