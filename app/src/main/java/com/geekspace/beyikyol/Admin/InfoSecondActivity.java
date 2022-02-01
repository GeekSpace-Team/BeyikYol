package com.geekspace.beyikyol.Admin;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.geekspace.beyikyol.R;
import com.geekspace.beyikyol.Activity.App.WalkThrough;

import java.util.Locale;

public class InfoSecondActivity extends AppCompatActivity {
    TextView language_dialog,text1;
    boolean lang_selected;
    Context context;
    String lang="en";
    Resources resources;
    Button enter;
    ImageView flag;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
       // getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_info);
        loadLocal();
        language_dialog = findViewById(R.id.dialog_language);
        text1=findViewById(R.id.text1);
        enter=findViewById (R.id.nextBtn);
        flag=findViewById(R.id.flag);



        language_dialog.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String[] Language = {"Türkmen", "Русский", "English"};
                final AlertDialog.Builder builder = new AlertDialog.Builder(InfoSecondActivity.this);
                builder.setTitle(R.string.selectedlanguage)
                        .setSingleChoiceItems (Language, -1, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {

                                language_dialog.setText(Language[which]);
                                lang_selected= Language[which].equals("Türkmen");
                                //if user select prefered language as English then
                                if(Language[which].equals("Türkmen"))
                                {
                                    lang="tm";
                                    setLocale("tm");

                                    text1.setText("Hoş geldiňiz!");
                                    enter.setText("Indiki");
                                    flag.setImageResource(R.drawable.tm);




                                }
                                //if user select prefered language as Hindi then
                                if(Language[which].equals("Русский"))
                                {
                                    lang="ru";
                                    setLocale("ru");
                                    text1.setText("Добро пожаловать!");
                                    enter.setText("Следующий");
                                    flag.setImageResource(R.drawable.ru);

                                }
                                if(Language[which].equals("English"))
                                {
                                    lang="en";
                                    setLocale("en");
                                    text1.setText("Welcome!");
                                    enter.setText("Next");
                                    flag.setImageResource(R.drawable.en);
                                }
                            }
                        })
                        .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick( DialogInterface dialog, int which) {
                                dialog.dismiss();
                                setLocale(lang);
                            }
                        });
                builder.create().show();
            }
        });



    }

    public void nextBtn (View view) {
        Intent intent = new Intent (InfoSecondActivity.this, WalkThrough.class);
        setLocale(lang);
        startActivity (intent);
        finish();

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