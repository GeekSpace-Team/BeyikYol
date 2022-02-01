package com.geekspace.beyikyol.Activity.Car;

import android.app.Activity;
import android.app.ActivityManager;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.Typeface;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationCompat;

import com.geekspace.beyikyol.Activity.App.Sazlamalar;
import com.geekspace.beyikyol.CustomView.Alert;
import com.geekspace.beyikyol.Database.BenzinDB;
import com.geekspace.beyikyol.Database.CalyshmakDB;
import com.geekspace.beyikyol.Database.CarDB;
import com.geekspace.beyikyol.R;
import com.geekspace.beyikyol.Utils.Utils;

import java.util.Calendar;

public class BenzinUpdate extends AppCompatActivity {
    EditText litre,miles;
    Button ok;
    TextView car_name;
    ImageView car_image;
    String type="";
    String languages="";
    String newLang="";
    String selectMarka,selectModel,selectYear,selectEnginePower,selectEngineType,selectTransmission,selectMiles,selectImage="",selectVin,selectPhoneNumber,selectType,select_temp_image,id;
    BenzinDB benzinDB2;
    String gelenID="";
    TextView t1,t2;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
       // getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_benzin_update);
        Typeface Ping_medium = Typeface.createFromAsset(getAssets(), "fonts/Ping LCG Medium.otf");
        Typeface Ping_bold = Typeface.createFromAsset(getAssets(), "fonts/Ping LCG Bold.otf");
        Typeface Ping_regular = Typeface.createFromAsset(getAssets(), "fonts/Ping LCG Regular.otf");
        SharedPreferences share = getSharedPreferences("Settings", Activity.MODE_PRIVATE);
        languages = share.getString("My_Lang", "");
        newLang = share.getString("My_Lang", "");

        Intent intent=getIntent();
        id=intent.getStringExtra("id");
        type=intent.getStringExtra("type");
        gelenID=intent.getStringExtra("costId");
        CarDB carDB=new CarDB(this);
        benzinDB2=new BenzinDB(this);
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

        //Typeface trebu = Typeface.createFromAsset(getAssets(), "fonts/trebuc.ttf");
        TextView toolbar_title=findViewById(R.id.toolbar_title);
        toolbar_title.setTypeface(Ping_bold);
        toolbar_title.setText(getResources().getString(R.string.benzin));
        ImageButton settings=findViewById(R.id.settings);
        settings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(BenzinUpdate.this, Sazlamalar.class));
              //  Animatoo.animateSwipeLeft(BenzinUpdate.this);
            }
        });
        ImageButton back_btn=findViewById(R.id.back_btn);
        back_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });







        ok=findViewById(R.id.ok_btn);
        litre=findViewById(R.id.litre);
        miles=findViewById(R.id.km);
        car_image=findViewById(R.id.image);
        car_name=findViewById(R.id.car_name);
        t1=findViewById(R.id.t1);
        t2=findViewById(R.id.t2);



        car_name.setText(selectMarka+" / "+selectModel);
        car_name.setTypeface(Ping_medium);
        t1.setTypeface(Ping_medium);
        t2.setTypeface(Ping_medium);

        CarDB db=new CarDB(this);
        byte[] data=carDB.getImage(id);
        if(data != null){
            Bitmap bitmap = Utils.getImage(data);
            car_image.setImageBitmap(bitmap);
        } else{
            car_image.setImageResource(R.drawable.unnamed);
        }


        Cursor cursor1=benzinDB2.getSelect(gelenID);
        if(cursor1.getCount()==0){
            return;
        }
        while (cursor1.moveToNext()){
            litre.setText(cursor1.getString(2));
            miles.setText(cursor1.getString(3));
        }
        ok.setTypeface(Ping_medium);

        litre.setTypeface(Ping_medium);
        miles.setTypeface(Ping_medium);
        ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String first=litre.getText().toString();
                String second=miles.getText().toString();
                if(first.isEmpty()){
                    Alert alert=new Alert(BenzinUpdate.this,"no",getResources().getString(R.string.attention),getResources().getString(R.string.litre_error));
                    alert.ShowDialog();
                    return;
                }
                if(second.isEmpty()){
                    Alert alert=new Alert(BenzinUpdate.this,"no",getResources().getString(R.string.attention),getResources().getString(R.string.error8));
                    alert.ShowDialog();
                    return;
                }
                Calendar calendar = Calendar.getInstance();
                int ay=calendar.get(Calendar.MONTH)+1;
                String gun=calendar.get(Calendar.DAY_OF_MONTH)+"";
                String hepde=calendar.get(Calendar.WEEK_OF_YEAR)+"";
                String yyl=calendar.get(Calendar.YEAR)+"";
                BenzinDB benzinDB=new BenzinDB(BenzinUpdate.this);
                boolean isInsert=benzinDB.updateData(gelenID,first,second);

                if(isInsert) {
                    Toast.makeText(BenzinUpdate.this, getResources().getString(R.string.succes), Toast.LENGTH_SHORT).show();
                    onBackPressed();
//                    Cursor getId=benzinDB.getLastInsertId();
//                    String lastId="";
//                    if(getId.getCount()==0){
//                        return;
//                    }
//                    while(getId.moveToNext()){
//                        lastId=getId.getString(0);
//                    }
//                    CostsDB costsDB=new CostsDB(BenzinUpdate.this);
//                    boolean isLast=costsDB.insert(id,lastId,"1",gun,String.valueOf(ay),yyl,hepde);
//                    if(isLast) {
//                        CarDB db = new CarDB(BenzinUpdate.this);
//                        boolean isUpdate = db.updateBenzin(id, second);
//                        if (isUpdate) {
//                            Toast.makeText(BenzinUpdate.this, getResources().getString(R.string.succes), Toast.LENGTH_SHORT).show();
//                            SharedPreferences sh = getSharedPreferences("push", Activity.MODE_PRIVATE);
//                            String p = sh.getString("n", "");
//                            if(p.equals("1")) {
//                                notif();
//                            }
//
//                            onBackPressed();
//
//                        }
//                    }
                } else{
                    Alert alert=new Alert(BenzinUpdate.this,"no",getResources().getString(R.string.error_title2),getResources().getString(R.string.error_title3));
                    alert.ShowDialog();
                }
            }
        });
    }

    @Override
    public void onBackPressed() {
        finish();
        if(type.equals("1")) {
            Intent car = new Intent(BenzinUpdate.this, Car_View.class);
            car.putExtra("id", id);
            startActivity(car);
        }
        if(type.equals("2")){
            Intent car = new Intent(BenzinUpdate.this, My_Costs.class);
            car.putExtra("id", id);
            startActivity(car);
        }
      //  Animatoo.animateSwipeRight(this);
    }
    public boolean Service(){
        ActivityManager services=(ActivityManager) getSystemService(ACTIVITY_SERVICE);
        for(ActivityManager.RunningServiceInfo serviceInfo:services.getRunningServices(Integer.MAX_VALUE)){
            if(getPackageName().equals(serviceInfo.service.getPackageName())){
                return true;
            }
        }
        return false;
    }

    public void notif(){
        String CHANNEL_ID="1";
        CarDB carDB=new CarDB(BenzinUpdate.this);
        CalyshmakDB calyshmakDB=new CalyshmakDB(BenzinUpdate.this);
        Cursor cursor = carDB.getAll();
        if (cursor.getCount() == 0) {
            return;
        }

        while (cursor.moveToNext()) {

            Cursor cursor1 = calyshmakDB.getAlarm(cursor.getString(8),cursor.getString(0));
            if (cursor1.getCount() == 0) {
                continue;
            } else {

                while(cursor1.moveToNext()) {
                    String text="";
                    if(cursor.getString(8).equals(cursor1.getString(9))){
                        text=getResources().getString(R.string.must)+" : "+cursor.getString(8)+" km";
                    } else{
                        int gija_galan=Integer.parseInt(cursor.getString(8))-Integer.parseInt(cursor1.getString(9));
                        text=getResources().getString(R.string.must1)+" : "+cursor.getString(8)+"-"+cursor1.getString(9)+"="+gija_galan+" km";
                    }
                    NotificationManager mNotificationManager;


                    Intent snoozeIntent = new Intent(BenzinUpdate.this, Calyshmak.class);
                    snoozeIntent.setAction("ACTION_SNOOZE");
                    snoozeIntent.putExtra("id", cursor.getString(0));
                    PendingIntent snoozePendingIntent =
                            PendingIntent.getActivity(BenzinUpdate.this, 0, snoozeIntent, 0);


                    NotificationCompat.Builder mBuilder =
                            new NotificationCompat.Builder(BenzinUpdate.this, cursor.getString(1)+"_"+cursor.getString(2));
//        Intent ii = new Intent(c, Chat.class);
//        PendingIntent pendingIntent = PendingIntent.getActivity(c, 0, ii, 0);

                    NotificationCompat.BigTextStyle bigText = new NotificationCompat.BigTextStyle();
                    bigText.bigText(text);
                    bigText.setBigContentTitle(cursor.getString(1)+" / "+cursor.getString(2));
                    bigText.setSummaryText(getResources().getString(R.string.must));
                    mBuilder.setAutoCancel(true);
                    mBuilder.setContentIntent(snoozePendingIntent);
                    mBuilder.setSmallIcon(R.drawable.upd);
                    mBuilder.setContentTitle(cursor.getString(1)+" / "+cursor.getString(2));
                    mBuilder.setContentText(getResources().getString(R.string.must));
                    mBuilder.setPriority(Notification.PRIORITY_MAX);
                    mBuilder.setStyle(bigText);
//                           mBuilder.addAction(R.drawable.ic_baseline_mood_24, getString(R.string.understod),
//                                   snoozePendingIntent);
                    mNotificationManager =
                            (NotificationManager) BenzinUpdate.this.getSystemService(Context.NOTIFICATION_SERVICE);

// === Removed some obsoletes
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                        String channelId = cursor.getString(1)+"_"+cursor.getString(2);
                        NotificationChannel channel = new NotificationChannel(
                                channelId,
                                cursor.getString(1)+"_"+cursor.getString(2),
                                NotificationManager.IMPORTANCE_HIGH);
                        mNotificationManager.createNotificationChannel(channel);
                        mBuilder.setChannelId(channelId);
                    }

                    mNotificationManager.notify(Integer.parseInt(cursor.getString(0))+132, mBuilder.build());

                }
            }
        }
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
}