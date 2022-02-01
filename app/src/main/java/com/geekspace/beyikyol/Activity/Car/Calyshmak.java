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
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationCompat;

import com.geekspace.beyikyol.Activity.App.Sazlamalar;
import com.geekspace.beyikyol.CustomView.Alert;
import com.geekspace.beyikyol.Database.CalyshmakDB;
import com.geekspace.beyikyol.Database.CarDB;
import com.geekspace.beyikyol.Database.CostsDB;
import com.geekspace.beyikyol.R;
import com.geekspace.beyikyol.Utils.Utils;

import java.util.Calendar;

public class Calyshmak extends AppCompatActivity {
    EditText miles, km;
    Button ok;
    TextView car_name;
    ImageView car_image;
    String calyshylan = "", alarm = "", type = "";
    String languages = "";
    String newLang = "";
    NotificationManager mNotificationManager;
    CheckBox motor_filtre, motor_yag, korobka, howa_filtre, salon_filtre, frion, check_alarm;
    String motor_filtreTV = "", motor_yagTV = "", korobkaTV = "", howa_filtreTV = "", salon_filtreTV = "", frionTV = "", check_alarmTV = "0";
    String selectMarka, selectModel, selectYear, selectEnginePower, selectEngineType, selectTransmission, selectMiles, selectImage = "", selectVin, selectPhoneNumber, selectType, select_temp_image, id;
    TextView t1,t2,t3;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_calyshmak);
        SharedPreferences share = getSharedPreferences("Settings", Activity.MODE_PRIVATE);
        Typeface Ping_medium = Typeface.createFromAsset(getAssets(), "fonts/Ping LCG Medium.otf");
        Typeface Ping_bold = Typeface.createFromAsset(getAssets(), "fonts/Ping LCG Bold.otf");
        Typeface Ping_regular = Typeface.createFromAsset(getAssets(), "fonts/Ping LCG Regular.otf");
        languages = share.getString("My_Lang", "");
        newLang = share.getString("My_Lang", "");
        motor_filtre = findViewById(R.id.motor_filtre);
        motor_yag = findViewById(R.id.motor_yag);
        korobka = findViewById(R.id.korobka);
        howa_filtre = findViewById(R.id.howa_filtre);
        salon_filtre = findViewById(R.id.salon_filtre);
        frion = findViewById(R.id.frion);
        check_alarm = findViewById(R.id.check_alarm);
        km = findViewById(R.id.km);
        t1 = findViewById(R.id.t1);
        t2 = findViewById(R.id.t2);
        t3 = findViewById(R.id.t3);


        motor_filtre.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    motor_filtreTV = getResources().getString(R.string.motor_filtre);
                } else {
                    motor_filtreTV = "";
                }
            }
        });

        motor_yag.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    motor_yagTV = getResources().getString(R.string.motor_yag);
                } else {
                    motor_yagTV = "";
                }
            }
        });

        korobka.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    korobkaTV = getResources().getString(R.string.korobka_yag_we_filtr);
                } else {
                    korobkaTV = "";
                }
            }
        });

        howa_filtre.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    howa_filtreTV = getResources().getString(R.string.howa_filtr);
                } else {
                    howa_filtreTV = "";
                }
            }
        });

        salon_filtre.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    salon_filtreTV = getResources().getString(R.string.salon_filtr);
                } else {
                    salon_filtreTV = "";
                }
            }
        });

        frion.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    frionTV = getResources().getString(R.string.frion);
                } else {
                    frionTV = "";
                }
            }
        });

        check_alarm.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    check_alarmTV = "1";
                } else {
                    check_alarmTV = "0";
                }
            }
        });
        Intent intent = getIntent();
        id = intent.getStringExtra("id");
        type = intent.getStringExtra("type");
        // Toast.makeText(Calyshmak.this,id,Toast.LENGTH_SHORT).show();
        CarDB carDB = new CarDB(this);
        Cursor cursor = carDB.getSelect(id);
        if (cursor.getCount() == 0) {
            finish();
            return;
        } else {
            while (cursor.moveToNext()) {
                if (cursor.getString(0).equals(id)) {
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
        TextView toolbar_title = findViewById(R.id.toolbar_title);
        toolbar_title.setTypeface(Ping_bold);
        toolbar_title.setText(getResources().getString(R.string.calyshmyk));
        ImageButton settings = findViewById(R.id.settings);
        settings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(Calyshmak.this, Sazlamalar.class));
                //Animatoo.animateSwipeLeft(Calyshmak.this);
            }
        });

        ImageButton back_btn = findViewById(R.id.back_btn);
        back_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });


        motor_filtre.setTypeface(Ping_medium);
        motor_yag.setTypeface(Ping_medium);
        korobka.setTypeface(Ping_medium);
        howa_filtre.setTypeface(Ping_medium);
        salon_filtre.setTypeface(Ping_medium);
        frion.setTypeface(Ping_medium);
        check_alarm.setTypeface(Ping_medium);

        ok = findViewById(R.id.ok_btn);
        miles = findViewById(R.id.miles);
        car_image = findViewById(R.id.image);
        car_name = findViewById(R.id.car_name);

        car_name.setTypeface(Ping_medium);
        miles.setTypeface(Ping_medium);
        ok.setTypeface(Ping_medium);
        km.setTypeface(Ping_medium);
        t1.setTypeface(Ping_medium);
        t2.setTypeface(Ping_medium);
        t3.setTypeface(Ping_medium);




        car_name.setText(selectMarka + " / " + selectModel);


        CarDB db = new CarDB(this);
        byte[] data = carDB.getImage(id);
        if (data != null) {
            Bitmap bitmap = Utils.getImage(data);
            car_image.setImageBitmap(bitmap);
        } else {
            car_image.setImageResource(R.drawable.unnamed);
        }


        ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String second = miles.getText().toString();
                String first = km.getText().toString();

                if (first.isEmpty()) {
                    Alert alert = new Alert(Calyshmak.this, "no", getResources().getString(R.string.attention), getResources().getString(R.string.error8));
                    alert.ShowDialog();
                    return;
                }
                if (second.isEmpty()) {
                    Alert alert = new Alert(Calyshmak.this, "no", getResources().getString(R.string.attention), getResources().getString(R.string.warning));
                    alert.ShowDialog();
                    return;
                }


                calyshylan = "";

                if (!motor_filtreTV.isEmpty()) {
                    calyshylan += "1" + ",";
                }
                if (!motor_yagTV.isEmpty()) {
                    calyshylan += "2" + ",";
                }
                if (!korobkaTV.isEmpty()) {
                    calyshylan += "3" + ",";
                }
                if (!howa_filtreTV.isEmpty()) {
                    calyshylan += "4" + ",";
                }
                if (!salon_filtreTV.isEmpty()) {
                    calyshylan += "5" + ",";
                }
                if (!frionTV.isEmpty()) {
                    calyshylan += "6" + ",";
                }
                Calendar calendar = Calendar.getInstance();
                int ay = calendar.get(Calendar.MONTH) + 1;
                String hepde = calendar.get(Calendar.WEEK_OF_YEAR) + "";
                String gun = calendar.get(Calendar.DAY_OF_MONTH) + "";
                String yyl = calendar.get(Calendar.YEAR) + "";


                if (calyshylan.isEmpty()) {
                    Alert alert = new Alert(Calyshmak.this, "no", getResources().getString(R.string.error_title2), getResources().getString(R.string.error_title));
                    alert.ShowDialog();
                    return;
                }
                calyshylan = calyshylan.substring(0, calyshylan.length() - 1);

                CalyshmakDB db = new CalyshmakDB(Calyshmak.this);
                if (db.updateSt(id)) {
                    boolean isInsert = db.insert(id, calyshylan, gun, String.valueOf(ay), yyl, check_alarmTV, "0", second, Integer.parseInt(first));

                    if (isInsert) {
                        Cursor getId = db.getLastInsertId();
                        String lastId = "";
                        if (getId.getCount() == 0) {
                            return;
                        }
                        while (getId.moveToNext()) {
                            lastId = getId.getString(0);
                        }
                        CostsDB costsDB = new CostsDB(Calyshmak.this);
                        boolean isLast = costsDB.insert(id, lastId, "2", gun, String.valueOf(ay), yyl, hepde);
                        if (isLast) {
                            CarDB db1 = new CarDB(Calyshmak.this);
                            boolean isUpdate = db1.updateBenzin(id, miles.getText().toString());
                            if (isUpdate) {
                                Toast.makeText(Calyshmak.this, getResources().getString(R.string.succes), Toast.LENGTH_SHORT).show();
                                mNotificationManager =
                                        (NotificationManager) Calyshmak.this.getSystemService(Context.NOTIFICATION_SERVICE);

                                mNotificationManager.cancelAll();
                                SharedPreferences sh = getSharedPreferences("push", Activity.MODE_PRIVATE);
                                String p = sh.getString("n", "");
                                if (p.equals("1")) {
                                    notif();
                                }

                                onBackPressed();


                            }
                        }


                    } else {
                        Alert alert = new Alert(Calyshmak.this, "no", getResources().getString(R.string.error_title2), getResources().getString(R.string.error_title3));
                        alert.ShowDialog();
                    }
                }
            }
        });
    }

    @Override
    public void onBackPressed() {
        finish();
        if (type == null) {
            Intent car = new Intent(Calyshmak.this, My_Costs.class);
            car.putExtra("id", id);
            startActivity(car);
        } else {
            if (type.equals("1")) {
                Intent car = new Intent(Calyshmak.this, Car_View.class);
                car.putExtra("id", id);
                startActivity(car);
            }
            if (type.equals("2")) {
                Intent car = new Intent(Calyshmak.this, My_Costs.class);
                car.putExtra("id", id);
                startActivity(car);
            }
        }
       // Animatoo.animateSwipeRight(this);
    }

    public boolean Service() {
        ActivityManager services = (ActivityManager) getSystemService(ACTIVITY_SERVICE);
        for (ActivityManager.RunningServiceInfo serviceInfo : services.getRunningServices(Integer.MAX_VALUE)) {
            if (getPackageName().equals(serviceInfo.service.getPackageName())) {
                return true;
            }
        }
        return false;
    }


    public void notif() {
        String CHANNEL_ID = "1";
        CarDB carDB = new CarDB(Calyshmak.this);
        CalyshmakDB calyshmakDB = new CalyshmakDB(Calyshmak.this);
        Cursor cursor = carDB.getAll();
        if (cursor.getCount() == 0) {
            return;
        }

        while (cursor.moveToNext()) {

            Cursor cursor1 = calyshmakDB.getAlarm(cursor.getString(8), cursor.getString(0));
            if (cursor1.getCount() == 0) {
                continue;
            } else {

                while (cursor1.moveToNext()) {
                    String text = "";
                    if (cursor.getString(8).equals(cursor1.getString(9))) {
                        text = getResources().getString(R.string.must) + " : " + cursor.getString(8) + " km";
                    } else {
                        int gija_galan = Integer.parseInt(cursor.getString(8)) - Integer.parseInt(cursor1.getString(9));
                        text = getResources().getString(R.string.must1) + " : " + cursor.getString(8) + "-" + cursor1.getString(9) + "=" + gija_galan + " km";
                    }


                    Intent snoozeIntent = new Intent(Calyshmak.this, Calyshmak.class);
                    snoozeIntent.setAction("ACTION_SNOOZE");
                    snoozeIntent.putExtra("id", cursor.getString(0));
                    PendingIntent snoozePendingIntent =
                            PendingIntent.getActivity(Calyshmak.this, 0, snoozeIntent, 0);


                    NotificationCompat.Builder mBuilder =
                            new NotificationCompat.Builder(Calyshmak.this, cursor.getString(1) + "_" + cursor.getString(2));
//        Intent ii = new Intent(c, Chat.class);
//        PendingIntent pendingIntent = PendingIntent.getActivity(c, 0, ii, 0);

                    NotificationCompat.BigTextStyle bigText = new NotificationCompat.BigTextStyle();
                    bigText.bigText(text);
                    bigText.setBigContentTitle(cursor.getString(1) + " / " + cursor.getString(2));
                    bigText.setSummaryText(getResources().getString(R.string.must));
                    mBuilder.setAutoCancel(true);
                    mBuilder.setContentIntent(snoozePendingIntent);
                    mBuilder.setSmallIcon(R.drawable.upd);
                    mBuilder.setContentTitle(cursor.getString(1) + " / " + cursor.getString(2));
                    mBuilder.setContentText(getResources().getString(R.string.must));
                    mBuilder.setPriority(Notification.PRIORITY_MAX);
                    mBuilder.setStyle(bigText);
//                           mBuilder.addAction(R.drawable.ic_baseline_mood_24, getString(R.string.understod),
//                                   snoozePendingIntent);
                    mNotificationManager =
                            (NotificationManager) Calyshmak.this.getSystemService(Context.NOTIFICATION_SERVICE);

// === Removed some obsoletes
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                        String channelId = cursor.getString(1) + "_" + cursor.getString(2);
                        NotificationChannel channel = new NotificationChannel(
                                channelId,
                                cursor.getString(1) + "_" + cursor.getString(2),
                                NotificationManager.IMPORTANCE_HIGH);
                        mNotificationManager.createNotificationChannel(channel);
                        mBuilder.setChannelId(channelId);
                    }

                    mNotificationManager.notify(Integer.parseInt(cursor.getString(0)) + 132, mBuilder.build());

                }
            }
        }
    }


    @Override
    protected void onResume() {
        super.onResume();
        SharedPreferences share = getSharedPreferences("Settings", Activity.MODE_PRIVATE);
        newLang = share.getString("My_Lang", "");
        if (!newLang.equals(languages)) {
            recreate();
        }
    }


}