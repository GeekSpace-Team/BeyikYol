package com.geekspace.beyikyol.Receiver;

import android.app.Activity;
import android.app.AlarmManager;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.os.Build;
import android.os.Handler;
import android.os.IBinder;
import android.os.Looper;
import android.os.SystemClock;

import androidx.annotation.Nullable;
import androidx.core.app.NotificationCompat;

import com.geekspace.beyikyol.Activity.Car.Calyshmak;
import com.geekspace.beyikyol.Database.CalyshmakDB;
import com.geekspace.beyikyol.Database.CarDB;
import com.geekspace.beyikyol.R;

import java.util.Timer;
import java.util.TimerTask;

public class AlarmService extends Service {
    String CHANNEL_ID="1";
    CarDB carDB;
    CalyshmakDB calyshmakDB;
    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    Timer timer;
    Handler handler;
    @Override
    public void onCreate() {
        super.onCreate();
        timer=new Timer();
        handler=new Handler(Looper.getMainLooper());
        carDB=new CarDB(this);
        calyshmakDB=new CalyshmakDB(this);
        timer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {

                SharedPreferences sh = getSharedPreferences("push", Activity.MODE_PRIVATE);
                String p = sh.getString("n", "");
                if(p.equals("1")) {
                    notification();
                }
            }
        },0,300000);
    }

    public void notification(){
        handler.post(new Runnable() {
            @Override
            public void run() {
                //Toast.makeText(AlarmService.this,"Ok",Toast.LENGTH_SHORT).show();

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


                           Intent notificationIntent=new Intent(AlarmService.this, Calyshmak.class);
                           notificationIntent.putExtra("id",cursor.getString(0));
                           notificationIntent.putExtra("type","3");


                           PendingIntent pendingIntent=PendingIntent.getActivity(AlarmService.this,
                                   0,notificationIntent,0);


                           NotificationCompat.Builder mBuilder =
                                   new NotificationCompat.Builder(AlarmService.this, cursor.getString(1)+"_"+cursor.getString(2));
//        Intent ii = new Intent(c, Chat.class);
//        PendingIntent pendingIntent = PendingIntent.getActivity(c, 0, ii, 0);

                           NotificationCompat.BigTextStyle bigText = new NotificationCompat.BigTextStyle();
                           bigText.bigText(text);
                           bigText.setBigContentTitle(cursor.getString(1)+" / "+cursor.getString(2));
                           bigText.setSummaryText(getResources().getString(R.string.must));
                           mBuilder.setAutoCancel(true);
                           //mBuilder.setContentIntent(pendingIntent);
                           mBuilder.setSmallIcon(R.drawable.upd);
                           mBuilder.setContentTitle(cursor.getString(1)+" / "+cursor.getString(2));
                           mBuilder.setContentText(getResources().getString(R.string.must));
                           mBuilder.setPriority(Notification.PRIORITY_MAX);
                           mBuilder.setStyle(bigText);
//                           mBuilder.addAction(R.drawable.ic_baseline_mood_24, getString(R.string.understod),
//                                   snoozePendingIntent);
                           mNotificationManager =
                                   (NotificationManager) AlarmService.this.getSystemService(Context.NOTIFICATION_SERVICE);

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



                           mNotificationManager.notify(Integer.parseInt(cursor.getString(0)+132), mBuilder.build());

                       }
                    }
                }
            }
        });

    }


    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        return START_STICKY;
    }

    @Override
    public void onTaskRemoved(Intent rootIntent) {
        Intent restartService = new Intent(getApplicationContext(),
                this.getClass());
        restartService.setPackage(getPackageName());
        PendingIntent restartServicePI = PendingIntent.getService(
                getApplicationContext(), 1, restartService,
                PendingIntent.FLAG_ONE_SHOT);

        //Restart the service once it has been killed android


        AlarmManager alarmService = (AlarmManager)getApplicationContext().getSystemService(Context.ALARM_SERVICE);
        alarmService.set(AlarmManager.ELAPSED_REALTIME, SystemClock.elapsedRealtime() +100, restartServicePI);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        timer.cancel();
    }


}
