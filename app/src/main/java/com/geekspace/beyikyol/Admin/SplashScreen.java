package com.geekspace.beyikyol.Admin;

import android.app.Activity;
import android.app.ActivityManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;

import androidx.appcompat.app.AppCompatActivity;

import com.geekspace.beyikyol.Activity.App.LAnguage;
import com.geekspace.beyikyol.Activity.App.MainMenu;
import com.geekspace.beyikyol.R;
import com.geekspace.beyikyol.Receiver.SensorService;

public class SplashScreen extends AppCompatActivity {

    private static final String TAG = "SplashScreen";
    private static  int SPLASH_SCREEN = 1920;
    private Animation animation;
    Context context=this;
    Intent mServiceIntent;
    private SensorService mSensorService;


    Context ctx;


    public Context getCtx() {
        return ctx;
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_splash_screen);
        ctx = this;

        Typeface Ping_medium = Typeface.createFromAsset(getAssets(), "fonts/Ping LCG Medium.otf");
        Typeface kabel=Typeface.createFromAsset(this.getAssets(),"fonts/kabel.ttf");
        Typeface shad=Typeface.createFromAsset(this.getAssets(),"fonts/shad.ttf");


        //присваевание анимаций
        animation = AnimationUtils.loadAnimation(this, R.anim.bottom_animation);

        //Соединяем ImageView и TextView с анимациями

       // textView.setAnimation(animation);
        //logotext.setAnimation(animation);

        //Переход на другой Activity
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {

                if(loadSplash().equals("1")){
                    Intent intent = new Intent(SplashScreen.this, MainMenu.class);
                    startActivity(intent);
                    finish();
                } else{

                    SharedPreferences.Editor editor = getSharedPreferences("push", MODE_PRIVATE).edit();
                    editor.putString("n", "1");
                    editor.apply();

                    Intent intent = new Intent(SplashScreen.this, LAnguage.class);
                    startActivity(intent);
                    finish();

                }




            }
        }, SPLASH_SCREEN);
    }

    public String loadSplash() {
        SharedPreferences share = getSharedPreferences("Splash", Activity.MODE_PRIVATE);
        String languages = share.getString("isFistTime", "");
        return languages;
    }

    public boolean Service(){
        ActivityManager services=(ActivityManager) getSystemService(ACTIVITY_SERVICE);
        for(ActivityManager.RunningServiceInfo serviceInfo:services.getRunningServices(Integer.MAX_VALUE)){
            if(context.getPackageName().equals(serviceInfo.service.getPackageName())){
                return true;
            }
        }
        return false;
    }


    private boolean isMyServiceRunning(Class<?> serviceClass) {
        ActivityManager manager = (ActivityManager) getSystemService(Context.ACTIVITY_SERVICE);
        for (ActivityManager.RunningServiceInfo service : manager.getRunningServices(Integer.MAX_VALUE)) {
            if (serviceClass.getName().equals(service.service.getClassName())) {
                Log.i ("isMyServiceRunning?", true+"");
                return true;
            }
        }
        Log.i ("isMyServiceRunning?", false+"");
        return false;
    }


}