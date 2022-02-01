package com.geekspace.beyikyol.Activity.App;

import android.app.ActionBar;
import android.app.Activity;
import android.app.Dialog;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SwitchCompat;

import com.geekspace.beyikyol.CustomView.Custom_Spinner2;
import com.geekspace.beyikyol.Model.LangClass;
import com.geekspace.beyikyol.R;

import java.util.ArrayList;
import java.util.Locale;

public class Sazlamalar extends AppCompatActivity {
    Spinner lang;
    ArrayList<LangClass> lang_array = new ArrayList<>();
    Context context = this;
    SwitchCompat push;
    String new_lang;
    RelativeLayout feedback, terms, about;
    Dialog dialog;
    ImageButton yza;
    String n = "1";
    String languages;
    TextView tel, value2, value3, value4, value5, value6, title,version_number;
    Intent exitIntent;
    String exitCode = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_sazlamalar);

        Typeface Ping_medium = Typeface.createFromAsset(getAssets(), "fonts/Ping LCG Medium.otf");
        Typeface Ping_bold = Typeface.createFromAsset(getAssets(), "fonts/Ping LCG Bold.otf");
        Typeface Ping_regular = Typeface.createFromAsset(getAssets(), "fonts/Ping LCG Regular.otf");
        title = findViewById(R.id.title);
        tel = findViewById(R.id.tel);
        value2 = findViewById(R.id.value2);
        value3 = findViewById(R.id.value3);
        value4 = findViewById(R.id.value4);
        value5 = findViewById(R.id.value5);
        value6 = findViewById(R.id.value6);
        push = findViewById(R.id.push);
        lang = findViewById(R.id.lang);
        feedback = findViewById(R.id.feedback);
        terms = findViewById(R.id.terms);
        about = findViewById(R.id.about);
        yza = findViewById(R.id.back);
        version_number = findViewById(R.id.version_number);


        tel.setTypeface(Ping_medium);
        value2.setTypeface(Ping_medium);
        value3.setTypeface(Ping_medium);
        value4.setTypeface(Ping_medium);
        value5.setTypeface(Ping_medium);
        value6.setTypeface(Ping_medium);
        push.setTypeface(Ping_medium);
        version_number.setTypeface(Ping_medium);

        PackageInfo pInfo = null;
        try {
            pInfo = context.getPackageManager().getPackageInfo(context.getPackageName(), 0);
            String version = pInfo.versionName;
            version_number.setText(version);
        } catch (PackageManager.NameNotFoundException e) {
           // version_number.setText("1.0");
            e.printStackTrace();
        }



        title.setTypeface(Ping_bold);


        yza.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        SharedPreferences share = getSharedPreferences("Settings", Activity.MODE_PRIVATE);
        languages = share.getString("My_Lang", "");

        if (languages.equals("ru")) {
            lang_array.add(new LangClass("Ru", R.drawable.ru,"Русский"));
            lang_array.add(new LangClass("Tm", R.drawable.tm,"Türkmençe"));
            lang_array.add(new LangClass("En", R.drawable.en,"English"));
        }
        if (languages.equals("tm")) {
            lang_array.add(new LangClass("Tm", R.drawable.tm,"Türkmençe"));
            lang_array.add(new LangClass("Ru", R.drawable.ru,"Русский"));
            lang_array.add(new LangClass("En", R.drawable.en,"English"));
        }
        if (languages.equals("en")) {
            lang_array.add(new LangClass("En", R.drawable.en,"English"));
            lang_array.add(new LangClass("Tm", R.drawable.tm,"Türkmençe"));
            lang_array.add(new LangClass("Ru", R.drawable.ru,"Русский"));
        }

        SharedPreferences sh = getSharedPreferences("push", Activity.MODE_PRIVATE);
        String p = sh.getString("n", "");
        if (!p.isEmpty()) {
            n = p;
        }

//        Toast.makeText(context,n,Toast.LENGTH_SHORT).show();

        if (n.equals("1")) {
            push.setChecked(true);
        } else if (n.equals("0")) {
            push.setChecked(false);
        }


        Custom_Spinner2 adapter = new Custom_Spinner2(context, lang_array);
        lang.setAdapter(adapter);

        lang.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                LangClass l = lang_array.get(position);
                setLocale(l.getValue().toLowerCase());
                setLang(l.getValue().toLowerCase());
                new_lang = l.getValue().toLowerCase();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        push.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    SharedPreferences.Editor editor = getSharedPreferences("push", MODE_PRIVATE).edit();
                    editor.putString("n", "1");
                    editor.apply();
                } else {
                    SharedPreferences.Editor editor = getSharedPreferences("push", MODE_PRIVATE).edit();
                    editor.putString("n", "0");
                    editor.apply();
                }
            }
        });

        feedback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                rateApp();
            }
        });

        terms.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(Sazlamalar.this, com.geekspace.beyikyol.Activity.App.terms.class));
               // Animatoo.animateSwipeLeft(Sazlamalar.this);
            }
        });

        about.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(Sazlamalar.this, BizBarada.class));
               // Animatoo.animateSwipeLeft(Sazlamalar.this);
            }
        });


    }

    private void setLocale(String lang) {
        Locale locale = new Locale(lang);
        Locale.setDefault(locale);
        Configuration config = new Configuration();
        config.locale = locale;
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

    public void setLang(String dil) {
        if (dil.equals("tm")) {
            title.setText("Sazlamalar");
            tel.setText("Dili saýla:");
            value2.setText("Ýatlatmalar:");
            value3.setText("Wersiýasy:");
            value4.setText("Peýdalanmak şertleri");
            value5.setText("Biz barada");
            value6.setText("Bahalandyrmak");

        }
        if (dil.equals("ru")) {
            title.setText("Настройки");
            tel.setText("Выберите язык:");
            value2.setText("Уведомление:");
            value3.setText("Версия:");
            value4.setText("Условия пользования");
            value5.setText("О нас");
            value6.setText("Обратная связь");
        }
        if (dil.equals("en")) {
            title.setText("Settings");
            tel.setText("Select Lang:");
            value2.setText("Notification:");
            value3.setText("Version:");
            value4.setText("Terms of Use");
            value5.setText("About Us");
            value6.setText("Feedback");
        }
    }

    public void rateApp() {
        try {
            Intent rateIntent = rateIntentForUrl("market://details");
            startActivity(rateIntent);
        } catch (ActivityNotFoundException e) {
            Intent rateIntent = rateIntentForUrl("https://play.google.com/store/apps/details");
            startActivity(rateIntent);
        }
    }

    private Intent rateIntentForUrl(String url) {
        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(String.format("%s?id=%s", url, getPackageName())));
        int flags = Intent.FLAG_ACTIVITY_NO_HISTORY | Intent.FLAG_ACTIVITY_MULTIPLE_TASK;
        if (Build.VERSION.SDK_INT >= 21) {
            flags |= Intent.FLAG_ACTIVITY_NEW_DOCUMENT;
        } else {
            //noinspection deprecation
            flags |= Intent.FLAG_ACTIVITY_CLEAR_WHEN_TASK_RESET;
        }
        intent.addFlags(flags);
        return intent;
    }


    public void feedback() {
        Button back, cancel;
        int rating = 0;
        final TextView tv1, tv2;
        final ImageView status;
        RatingBar ratingBar;
        dialog = new Dialog(context);
        dialog.setContentView(R.layout.feedback);
        dialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        Window window = dialog.getWindow();
        window.setGravity(Gravity.CENTER);
        window.getAttributes().windowAnimations = R.style.DialogAnimation;

        back = dialog.findViewById(R.id.back);
        tv1 = dialog.findViewById(R.id.t1);
        tv2 = dialog.findViewById(R.id.t2);
        status = dialog.findViewById(R.id.st_img);
        cancel = dialog.findViewById(R.id.cancel);
        ratingBar = dialog.findViewById(R.id.rating);

        ratingBar.setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener() {
            @Override
            public void onRatingChanged(RatingBar ratingBar, float rating, boolean fromUser) {
                if (rating == 0) {
                    tv2.setText(getResources().getString(R.string.yldyz_go));
                    status.setImageResource(R.drawable.about_icon);
                }
                if (rating == 1) {
                    tv2.setText(getResources().getString(R.string.yldyz_go1));
                    status.setImageResource(R.drawable.about_icon);
                }
                if (rating == 2) {
                    tv2.setText(getResources().getString(R.string.yldyz_go2));
                    status.setImageResource(R.drawable.about_icon);
                }
                if (rating == 3) {
                    tv2.setText(getResources().getString(R.string.yldyz_go3));
                    status.setImageResource(R.drawable.about_icon);
                }
                if (rating == 4) {
                    tv2.setText(getResources().getString(R.string.yldyz_go4));
                    status.setImageResource(R.drawable.about_icon);
                }
                if (rating == 5) {
                    tv2.setText(getResources().getString(R.string.yldyz_go5));
                    status.setImageResource(R.drawable.about_icon);
                }
            }
        });


        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();

            }
        });
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();

            }
        });

        dialog.setCancelable(true);
        window.setLayout(ActionBar.LayoutParams.MATCH_PARENT, ActionBar.LayoutParams.WRAP_CONTENT);
        dialog.show();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
       // Animatoo.animateSwipeRight(context);
//        String data="0";
//        if(new_lang.equals(languages)) {
//            data="0";
////
//        } else{
//            data="1";
//            PackageManager packageManager = context.getPackageManager();
//            Intent intent = new Intent(context,MainMenu.class);
//            ComponentName componentName = intent.getComponent();
//            Intent mainIntent = Intent.makeRestartActivityTask(componentName);
//            context.startActivity(mainIntent);
//            Runtime.getRuntime().exit(0);
//        }
//        Intent intent=new Intent();
//        intent.putExtra("result",data);
//        setResult(RESULT_OK,intent);
//        finish();
//       // startActivity(exitIntent);
//        Animatoo.animateSwipeRight(this);
//
    }
}