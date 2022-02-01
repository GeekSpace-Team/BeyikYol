package com.geekspace.beyikyol.Activity.Car;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.geekspace.beyikyol.Activity.App.Sazlamalar;
import com.geekspace.beyikyol.Adapter.Ew_Adapter;
import com.geekspace.beyikyol.Model.Ew_Klass;
import com.geekspace.beyikyol.R;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;

public class Ewakuator extends AppCompatActivity {
    ArrayList<Ew_Klass> calls=new ArrayList<>();
    ArrayList<Ew_Klass> search_calls=new ArrayList<>();
    RecyclerView recyclerView;
    EditText search;
    String result="0";
    private static final int REQUEST_CODE=1;
    String languages="";
    String newLang="";
    TextView empty_tv;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_ewakuator);
        recyclerView=findViewById(R.id.phone_numbers);

        Typeface Ping_medium = Typeface.createFromAsset(getAssets(), "fonts/Ping LCG Medium.otf");
        Typeface Ping_bold = Typeface.createFromAsset(getAssets(), "fonts/Ping LCG Bold.otf");
        Typeface Ping_regular = Typeface.createFromAsset(getAssets(), "fonts/Ping LCG Regular.otf");
        SharedPreferences share = getSharedPreferences("Settings", Activity.MODE_PRIVATE);
        languages = share.getString("My_Lang", "");
        newLang = share.getString("My_Lang", "");
        empty_tv=findViewById(R.id.empty_tv);


        empty_tv.setTypeface(Ping_medium);
        //Typeface trebu = Typeface.createFromAsset(getAssets(), "fonts/trebuc.ttf");
        TextView toolbar_title=findViewById(R.id.toolbar_title);
        toolbar_title.setTypeface(Ping_bold);
        toolbar_title.setText(getResources().getString(R.string.ewakuator));
        ImageButton settings=findViewById(R.id.settings);
        settings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivityForResult(new Intent(Ewakuator.this, Sazlamalar.class),REQUEST_CODE);
               // Animatoo.animateSwipeLeft(Ewakuator.this);
            }
        });
        ImageButton back_btn=findViewById(R.id.back_btn);
        back_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
        search=findViewById(R.id.search);
        BufferedReader reader = null;
        try {
            reader = new BufferedReader(
                    new InputStreamReader(getAssets().open("ewakuator.txt")));
            String mLine;
            SharedPreferences share2 = getSharedPreferences("Settings", Activity.MODE_PRIVATE);
            String languages = share2.getString("My_Lang", "");
            while ((mLine = reader.readLine()) != null) {

                String[] strings = mLine.split("/" /*<- Regex */);
                String name="";
                if(languages.equals("ru")){
                    name=strings[3];
                }
                if(languages.equals("tm")){
                    name=strings[2];
                }
                if(languages.equals("en")){
                    name=strings[1];
                }
                calls.add(new Ew_Klass(strings[0],name));
            }

            if(calls.size()==0){
                findViewById(R.id.empty).setVisibility(View.VISIBLE);
            } else{
                findViewById(R.id.empty).setVisibility(View.GONE);
            }

            LinearLayoutManager linearLayoutManager=new LinearLayoutManager(this);
            Ew_Adapter adapter=new Ew_Adapter(calls,this);
            recyclerView.setLayoutManager(linearLayoutManager);
            recyclerView.setAdapter(adapter);



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

        search.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                   String search_query=search.getText().toString();
                   if(search_query.equals("")){
                       Ew_Adapter adapter=new Ew_Adapter(calls,Ewakuator.this);
                       recyclerView.setAdapter(adapter);
                   } else{
                       search_calls.clear();
                       for(int i=0;i<calls.size();i++){
                           Ew_Klass ew_klass=calls.get(i);
                           if(ew_klass.getPhone_number().toUpperCase().contains(search_query.toUpperCase())){
                               search_calls.add(new Ew_Klass(ew_klass.getPhone_number(),ew_klass.getPlace()));
                           }
                       }
                       Ew_Adapter adapter=new Ew_Adapter(search_calls,Ewakuator.this);
                       recyclerView.setAdapter(adapter);
                   }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
    }

    @Override
    public void onBackPressed() {

        Intent intent=new Intent();
        intent.putExtra("result",result);
        setResult(RESULT_OK,intent);
        finish();
        // startActivity(exitIntent);
       // Animatoo.animateSwipeRight(this);
    }

    @Override
    protected void onResume() {
        super.onResume();
        SharedPreferences share = getSharedPreferences("Settings", Activity.MODE_PRIVATE);
        newLang = share.getString("My_Lang", "");
        if(!newLang.equals(languages)){
            recreate();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if(requestCode==REQUEST_CODE){
            if(resultCode==RESULT_OK){
                // Toast.makeText(this, data.getStringExtra("result"), Toast.LENGTH_SHORT).show();

//                if(data.getStringExtra("result").equals("1")) {
//                    recreate();
//
//                }
//                this.result=data.getStringExtra("result");

                //   Toast.makeText(this, "OK", Toast.LENGTH_SHORT).show();
            }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }
}