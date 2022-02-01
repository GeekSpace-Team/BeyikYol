package com.geekspace.beyikyol.App;

import android.app.Application;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.os.Build;

import com.geekspace.beyikyol.R;
import com.yandex.mapkit.MapKitFactory;

public class App extends Application {
    public static final String CHANNEL_ID="exampleServiceChannel";

    @Override
    public void onCreate() {
        super.onCreate();
        MapKitFactory.setApiKey("32c7faf6-4337-44bd-b0cc-eaf7a157d5ff");
        createNotificationChannel();
    }

    private void createNotificationChannel() {
        if(Build.VERSION.SDK_INT>=Build.VERSION_CODES.O){
            NotificationChannel serviceChannel=new NotificationChannel(CHANNEL_ID,
                    getResources().getString(R.string.app_name),
                    NotificationManager.IMPORTANCE_NONE);

            NotificationManager manager=getSystemService(NotificationManager.class);

            manager.createNotificationChannel(serviceChannel);


            //manager.cancelAll();
        }
    }
}
