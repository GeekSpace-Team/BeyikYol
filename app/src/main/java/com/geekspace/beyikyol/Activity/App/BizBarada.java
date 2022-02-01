package com.geekspace.beyikyol.Activity.App;

import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.geekspace.beyikyol.R;
import com.geekspace.beyikyol.Common.ServerAddress;

public class BizBarada extends AppCompatActivity {
    TextView title,title2,tv_justified_paragraph;
    ImageButton back;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
       // getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_biz_barada);
        back=findViewById(R.id.back);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        Typeface Ping_medium = Typeface.createFromAsset(getAssets(), "fonts/Ping LCG Medium.otf");
        Typeface Ping_bold = Typeface.createFromAsset(getAssets(), "fonts/Ping LCG Bold.otf");
        Typeface Ping_regular = Typeface.createFromAsset(getAssets(), "fonts/Ping LCG Regular.otf");

        title=findViewById(R.id.title);
        title2=findViewById(R.id.title2);
        tv_justified_paragraph=findViewById(R.id.tv_justified_paragraph);

        title.setTypeface(Ping_bold);
        title2.setTypeface(Ping_medium);
        tv_justified_paragraph.setTypeface(Ping_regular);





    }



    public void gotoGeekSpaceSite(View view){
        Intent intent = new Intent(Intent.ACTION_VIEW).setData(Uri.parse(ServerAddress.GEEKSPACE_SITE_URL));
        startActivity(intent);
    }

    public void rateApp(View view) {
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


    @Override
    public void onBackPressed() {
        super.onBackPressed();
       // Animatoo.animateSwipeRight(this);
    }
}