package com.geekspace.beyikyol.Activity.App;

import android.content.Intent;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.geekspace.beyikyol.R;
import com.geekspace.beyikyol.Common.ServerAddress;

public class terms extends AppCompatActivity {
    ImageButton back;
    TextView title,title2,tv_justified_paragraph;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_terms);

        Typeface Ping_medium = Typeface.createFromAsset(getAssets(), "fonts/Ping LCG Medium.otf");
        Typeface Ping_bold = Typeface.createFromAsset(getAssets(), "fonts/Ping LCG Bold.otf");
        Typeface Ping_regular = Typeface.createFromAsset(getAssets(), "fonts/Ping LCG Regular.otf");

        title=findViewById(R.id.title);
        title2=findViewById(R.id.title2);
        tv_justified_paragraph=findViewById(R.id.tv_justified_paragraph);

        title.setTypeface(Ping_bold);
        title2.setTypeface(Ping_medium);
        tv_justified_paragraph.setTypeface(Ping_regular);

        back=findViewById(R.id.back);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
    }
    @Override
    public void onBackPressed() {
        super.onBackPressed();
        //Animatoo.animateSwipeRight(this);
    }
    public void gotoGeekSpaceSite(View view){
        Intent intent = new Intent(Intent.ACTION_VIEW).setData(Uri.parse(ServerAddress.GEEKSPACE_SITE_URL));
        startActivity(intent);
    }
}