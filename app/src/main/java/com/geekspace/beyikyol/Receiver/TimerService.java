package com.geekspace.beyikyol.Receiver;

import android.app.IntentService;
import android.app.NotificationManager;
import android.content.Intent;
import android.os.Bundle;
import android.os.ResultReceiver;
import android.util.Log;

import androidx.core.app.NotificationCompat;

import com.geekspace.beyikyol.R;

public class TimerService extends IntentService {
    int k=0,j=1;
    public TimerService(){
        super("Timer Service");
    }

    @Override
    public void onCreate() {
        super.onCreate();
        Log.v("timer","Timer service has started.");
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        super.onStartCommand(intent, flags, startId);
        return START_STICKY;
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        if(intent==null){
            for(int i=k;i<j;i++) {
                Log.v("timer","i (intent is null) = "+i);
                notif();
                k++;
                j++;
                try {

                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }

            NotificationCompat.Builder nb=new NotificationCompat.Builder(this);
            nb.setContentText("Timer done.");
            nb.setContentTitle("Hi!");
            nb.setSmallIcon(R.mipmap.ic_launcher);

            NotificationManager nm=(NotificationManager) getSystemService(NOTIFICATION_SERVICE);
            nm.notify(1,nb.build());

            return;
        }
        ResultReceiver receiver=intent.getParcelableExtra("receiver");
        for(int i=k;i<j;i++){
            Log.v("timer","i= "+i);
            notif();
            k++;
            j++;
            try{
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        Bundle bundle=new Bundle();
        bundle.putString("message","Counting done...");

        receiver.send(1234,bundle);
    }

    public void notif(){
        NotificationCompat.Builder nb=new NotificationCompat.Builder(this,"7868667");
        nb.setContentText("Timer done.");
        nb.setContentTitle("Hi!");
        nb.setSmallIcon(R.mipmap.ic_launcher);

        NotificationManager nm=(NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        nm.notify(1,nb.build());
    }
}
