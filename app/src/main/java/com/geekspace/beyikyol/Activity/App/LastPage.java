package com.geekspace.beyikyol.Activity.App;

import android.Manifest;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.geekspace.beyikyol.Activity.App.MainMenu;
import com.geekspace.beyikyol.R;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionDeniedResponse;
import com.karumi.dexter.listener.PermissionGrantedResponse;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.single.PermissionListener;

public class LastPage extends AppCompatActivity {
    Animation animation1,animation2,fade,animation3;
    ImageView firstImage;
    TextView title,desc;
    Button button;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_last_page);
        button=findViewById(R.id.bashla);
        firstImage=findViewById(R.id.img_1);
        title=findViewById(R.id.title);
        desc=findViewById(R.id.desc);

        Dexter.withActivity(this)
                .withPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE)
                .withListener(new PermissionListener() {
                    @Override
                    public void onPermissionGranted(PermissionGrantedResponse response) {

                    }

                    @Override
                    public void onPermissionDenied(PermissionDeniedResponse response) {

                    }

                    @Override
                    public void onPermissionRationaleShouldBeShown(PermissionRequest permission, PermissionToken token) {

                    }
                }).check();


        Dexter.withActivity(this)
                .withPermission(Manifest.permission.ACCESS_FINE_LOCATION)
                .withListener(new PermissionListener() {

                    @Override
                    public void onPermissionGranted(PermissionGrantedResponse response) {

                    }

                    @Override
                    public void onPermissionDenied(PermissionDeniedResponse response) {

                    }

                    @Override
                    public void onPermissionRationaleShouldBeShown(PermissionRequest permission, PermissionToken token) {

                    }
                }).check();

        animation1= AnimationUtils.loadAnimation(this,R.anim.anim1);
        animation2= AnimationUtils.loadAnimation(this,R.anim.anim2);
        animation3= AnimationUtils.loadAnimation(this,R.anim.anim3);
        fade= AnimationUtils.loadAnimation(this,R.anim.fade);

        firstImage.startAnimation(fade);
        title.startAnimation(animation1);
        desc.startAnimation(animation2);
        button.startAnimation(animation3);

    }

    public void bashlat(View view){
        SharedPreferences.Editor editor = getSharedPreferences("Splash", MODE_PRIVATE).edit();
        editor.putString("isFistTime", "1");
        editor.apply();
        startActivity(new Intent(this, MainMenu.class));
       // Animatoo.animateInAndOut(this);
        finish();
    }
}