package com.geekspace.beyikyol.Receiver;

import android.app.Activity;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.os.Build;
import android.os.Handler;
import android.widget.Toast;

import androidx.core.app.NotificationCompat;

import com.geekspace.beyikyol.Activity.Car.Calyshmak;
import com.geekspace.beyikyol.Database.CalyshmakDB;
import com.geekspace.beyikyol.Database.CarDB;
import com.geekspace.beyikyol.R;

import java.util.Timer;

public class CustomBroadcast extends BroadcastReceiver {
    String CHANNEL_ID="1";
    CarDB carDB;
    CalyshmakDB calyshmakDB;

    Timer timer;
    Handler handler;

    public CustomBroadcast(){

    }

    @Override
    public void onReceive(final Context context, Intent intent) {
        Toast.makeText(context, "Hello", Toast.LENGTH_SHORT).show();
        if(Intent.ACTION_BOOT_COMPLETED.equals(intent.getAction())){

        SharedPreferences sh = context.getSharedPreferences("push", Activity.MODE_PRIVATE);
        String p = sh.getString("n", "");
        if(p.equals("1")) {

            carDB = new CarDB(context);
            calyshmakDB = new CalyshmakDB(context);

            //Toast.makeText(AlarmService.this,"Ok",Toast.LENGTH_SHORT).show();

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
                            text = context.getResources().getString(R.string.must) + " : " + cursor.getString(8) + " km";
                        } else {
                            int gija_galan = Integer.parseInt(cursor.getString(8)) - Integer.parseInt(cursor1.getString(9));
                            text = context.getResources().getString(R.string.must1) + " : " + cursor.getString(8) + "-" + cursor1.getString(9) + "=" + gija_galan + " km";
                        }
                        NotificationManager mNotificationManager;


                        Intent notificationIntent = new Intent(context, Calyshmak.class);
                        notificationIntent.putExtra("id", cursor.getString(0));
                        notificationIntent.putExtra("type", "3");


                        PendingIntent pendingIntent = PendingIntent.getActivity(context,
                                0, notificationIntent, 0);


                        NotificationCompat.Builder mBuilder =
                                new NotificationCompat.Builder(context, cursor.getString(1) + "_" + cursor.getString(2));
//        Intent ii = new Intent(c, Chat.class);
//        PendingIntent pendingIntent = PendingIntent.getActivity(c, 0, ii, 0);

                        NotificationCompat.BigTextStyle bigText = new NotificationCompat.BigTextStyle();
                        bigText.bigText(text);
                        bigText.setBigContentTitle(cursor.getString(1) + " / " + cursor.getString(2));
                        bigText.setSummaryText(context.getResources().getString(R.string.must));
                        mBuilder.setAutoCancel(true);
                        //mBuilder.setContentIntent(pendingIntent);
                        mBuilder.setSmallIcon(R.drawable.upd);
                        mBuilder.setContentTitle(cursor.getString(1) + " / " + cursor.getString(2));
                        mBuilder.setContentText(context.getResources().getString(R.string.must));
                        mBuilder.setPriority(Notification.PRIORITY_MAX);
                        mBuilder.setStyle(bigText);
//                           mBuilder.addAction(R.drawable.ic_baseline_mood_24, getString(R.string.understod),
//                                   snoozePendingIntent);
                        mNotificationManager =
                                (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);

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


                        mNotificationManager.notify(Integer.parseInt(cursor.getString(0) + 132), mBuilder.build());

                    }
                }
            }
        }

        }
        throw new UnsupportedOperationException("Not yet implemented");
    }

    public void notification(){


    }
}
