package com.geekspace.beyikyol.Receiver;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.os.IBinder;
import android.os.Looper;

import androidx.annotation.Nullable;
import androidx.core.app.NotificationCompat;

import java.util.Timer;

import static com.geekspace.beyikyol.App.App.CHANNEL_ID;

import com.geekspace.beyikyol.Activity.App.MainMenu;
import com.geekspace.beyikyol.Database.CalyshmakDB;
import com.geekspace.beyikyol.Database.CarDB;
import com.geekspace.beyikyol.R;

public class ExampleService extends Service {

    CarDB carDB;
    CalyshmakDB calyshmakDB;
    Timer timer;
    Handler handler;
    int san=1;
    @Override
    public void onCreate() {
        super.onCreate();
    }

    @Override
    public int onStartCommand(final Intent intent, int flags, int startId) {
        timer=new Timer();
        handler=new Handler(Looper.getMainLooper());
        carDB=new CarDB(this);
        calyshmakDB=new CalyshmakDB(this);
//        timer.scheduleAtFixedRate(new TimerTask() {
//            @Override
//            public void run() {
//
//                handler.post(new Runnable() {
//                    @Override
//                    public void run() {

//                        SharedPreferences sh = getSharedPreferences("push", Activity.MODE_PRIVATE);
//                        String p = sh.getString("n", "");
//                        if(p.equals("1")) {
                            String input=intent.getStringExtra("inputExtra");
                            Intent notificationIntent=new Intent(ExampleService.this, MainMenu.class);
                            PendingIntent pendingIntent=PendingIntent.getActivity(ExampleService.this,
                                    0,notificationIntent,0);
                            Notification notification=new NotificationCompat.Builder(ExampleService.this,CHANNEL_ID)
                                    .setContentTitle(ExampleService.this.getResources().getString(R.string.running))
                                    .setContentText("GeekSpace")
                                    .setSmallIcon(R.mipmap.ic_launcher)
                                    .setContentIntent(pendingIntent)
                                    .setNotificationSilent()
                                    .build();

        notification.flags = Notification.FLAG_INSISTENT | Notification.FLAG_AUTO_CANCEL;










                            





                            startForeground(san,notification);

        NotificationManager notificationManager = (NotificationManager) this.getSystemService(Context.NOTIFICATION_SERVICE);

        notificationManager.cancelAll();






//       NotificationManager mNotificationManager =
//                (NotificationManager) ExampleService.this.getSystemService(Context.NOTIFICATION_SERVICE);
//
//       mNotificationManager.cancel(san);

                      //  }

//                    }
//                });
//
//
//            }
//        },0,1000);


        return START_NOT_STICKY;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
}
