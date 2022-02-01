package com.geekspace.beyikyol.Activity.App;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.ebanx.swipebtn.OnActiveListener;
import com.ebanx.swipebtn.OnStateChangeListener;
import com.ebanx.swipebtn.SwipeButton;
import com.geekspace.beyikyol.CustomView.Custom_Spinner3;
import com.geekspace.beyikyol.Model.Year;
import com.geekspace.beyikyol.R;

import java.util.ArrayList;
import java.util.Locale;

public class LAnguage extends AppCompatActivity {
    Spinner lang_select;
    ArrayList<Year> langs=new ArrayList<>();
    TextView textView,textView2;
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.lang_layout);
        final SwipeButton enableButton = (SwipeButton) findViewById(R.id.swipe_btn);
        lang_select=findViewById(R.id.lang_select);


        Typeface trebuc=Typeface.createFromAsset(this.getAssets(),"fonts/trebuc.ttf");
        Typeface bold=Typeface.createFromAsset(this.getAssets(),"fonts/Roboto-Bold.ttf");

        textView=findViewById(R.id.text1);
        textView2=findViewById(R.id.text2);

        textView.setTypeface(bold);
        textView2.setTypeface(trebuc);

        langs.add(new Year("Türkmen",R.drawable.tm));
        langs.add(new Year("English",R.drawable.en));
        langs.add(new Year("Русский",R.drawable.ru));

        Custom_Spinner3 adapter=new Custom_Spinner3(this,langs);
        lang_select.setAdapter(adapter);

        lang_select.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if(position==0){
                   textView.setText("Hoş geldiňiz!");
                   textView2.setText("Dili saýlaň we dowam etmek üçin “Indiki” düwmesini sag tarapa çekiň");
                   enableButton.setText("Indiki");
                   setLocale("tm");
                } if(position==1){
                   textView.setText("Welcome!");
                   textView2.setText("Select language and swipe the “Next” button to continue");
                   enableButton.setText("Next");
                   setLocale("en");
                } if(position==2){
                   textView.setText("Добро пожаловать!");
                   textView2.setText("Для того чтобы продолжить, выберите язык и проведите пальцем по кнопке «Следующий»");
                   enableButton.setText("Следующий");
                   setLocale("ru");
                }

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });




        enableButton.setOnStateChangeListener(new OnStateChangeListener() {
            @Override
            public void onStateChange(boolean active) {

            }
        });

        enableButton.setOnActiveListener(new OnActiveListener() {
            @Override
            public void onActive() {
                startActivity(new Intent(LAnguage.this, WalkThrough.class));
                finish();
            }
        });
    }

    private void setLocale (String lang) {
        Locale locale = new Locale(lang);
        Locale.setDefault(locale);
        Configuration config = new Configuration();
        config.locale= locale;
        getBaseContext().getResources().updateConfiguration(config, getBaseContext().getResources().getDisplayMetrics());
        //saved data to shared preferences
        SharedPreferences.Editor editor = getSharedPreferences("Settings", MODE_PRIVATE).edit();
        editor.putString("My_Lang", lang);
        editor.apply();
    }

    // load language saved in shared preferences
    public void loadLocal() {
        SharedPreferences share = getSharedPreferences("Settings", Activity.MODE_PRIVATE);
        String languages = share.getString("My_Lang", "");
        setLocale(languages);
    }
}
