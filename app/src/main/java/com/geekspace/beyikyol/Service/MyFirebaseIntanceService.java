package com.geekspace.beyikyol.Service;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Build;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.NotificationCompat;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.CustomTarget;
import com.bumptech.glide.request.transition.Transition;
import com.geekspace.beyikyol.R;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Map;
import java.util.Random;

public class MyFirebaseIntanceService extends FirebaseMessagingService {
    Bitmap image=null;
    @Override
    public void onMessageReceived(@NonNull RemoteMessage remoteMessage) {
        super.onMessageReceived(remoteMessage);
        //if(remoteMessage.getData().isEmpty())
        showNotification(remoteMessage.getNotification().getTitle(), remoteMessage.getNotification().getBody(),remoteMessage.getNotification().getImageUrl());
//        else
//            showNotification(remoteMessage.getData());
    }

    private void showNotification(Map<String, String> data) {
        String title = data.get("title").toString();
        String body = data.get("body").toString();
        NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        String NOTIFICATION_CHANNEL_ID = "com.geekspace.beyikyol.test";

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            CharSequence name;
            NotificationChannel notificationChannel = new NotificationChannel(NOTIFICATION_CHANNEL_ID, "Notification",
                    NotificationManager.IMPORTANCE_DEFAULT);

            notificationChannel.setDescription(getResources().getString(R.string.app_name));
            notificationChannel.enableLights(true);
            notificationChannel.setLightColor(Color.BLUE);
            notificationChannel.setVibrationPattern(new long[]{0, 1000, 500, 1000});
            notificationChannel.enableLights(true);
            notificationManager.createNotificationChannel(notificationChannel);
        }


        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(this, NOTIFICATION_CHANNEL_ID);
        notificationBuilder.setAutoCancel(true)
                .setDefaults(Notification.DEFAULT_ALL)
                .setWhen(System.currentTimeMillis())
                .setSmallIcon(R.mipmap.beyik_yol_icon)
                .setContentTitle(title)
                .setContentText(body)
                .setContentInfo("Info");

        notificationManager.notify(new Random().nextInt(), notificationBuilder.build());
    }

    private void showNotification(final String title, final String body, Uri path) {
        //Log.d("URL",path.toString());

        Glide.with(this)
                .asBitmap()
                .load(path)
                .into(new CustomTarget<Bitmap>() {
                    @Override
                    public void onResourceReady(@NonNull Bitmap resource, @Nullable Transition<? super Bitmap> transition) {
                        image=resource;

                        NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
                        String NOTIFICATION_CHANNEL_ID = "com.geekspace.beyikyol.test";

                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                            CharSequence name;
                            NotificationChannel notificationChannel = new NotificationChannel(NOTIFICATION_CHANNEL_ID, "Notification",
                                    NotificationManager.IMPORTANCE_DEFAULT);

                            notificationChannel.setDescription(getResources().getString(R.string.app_name));
                            notificationChannel.enableLights(true);
                            notificationChannel.setLightColor(Color.BLUE);
                            notificationChannel.setVibrationPattern(new long[]{0, 1000, 500, 1000});
                            notificationChannel.enableLights(true);
                            notificationManager.createNotificationChannel(notificationChannel);
                        }


                        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(MyFirebaseIntanceService.this, NOTIFICATION_CHANNEL_ID);
                        notificationBuilder.setAutoCancel(true)
                                .setDefaults(Notification.DEFAULT_ALL)
                                .setWhen(System.currentTimeMillis())
                                .setSmallIcon(R.mipmap.beyik_yol_icon)
                                .setLargeIcon(image)
                                .setContentTitle(title)
                                .setContentText(body)
                                .setStyle(new NotificationCompat.BigPictureStyle().bigPicture(image))
                                .setContentInfo("Info");

                        notificationManager.notify(new Random().nextInt(), notificationBuilder.build());
                    }

                    @Override
                    public void onLoadCleared(@Nullable Drawable placeholder) {

                    }

            @Override
            public void onLoadFailed(@Nullable Drawable errorDrawable) {
                        if(image==null) {
                            NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
                            String NOTIFICATION_CHANNEL_ID = "com.geekspace.beyikyol.test";

                            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                                CharSequence name;
                                NotificationChannel notificationChannel = new NotificationChannel(NOTIFICATION_CHANNEL_ID, "Notification",
                                        NotificationManager.IMPORTANCE_DEFAULT);

                                notificationChannel.setDescription(getResources().getString(R.string.app_name));
                                notificationChannel.enableLights(true);
                                notificationChannel.setLightColor(Color.BLUE);
                                notificationChannel.setVibrationPattern(new long[]{0, 1000, 500, 1000});
                                notificationChannel.enableLights(true);
                                notificationManager.createNotificationChannel(notificationChannel);
                            }


                            NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(MyFirebaseIntanceService.this, NOTIFICATION_CHANNEL_ID);
                            notificationBuilder.setAutoCancel(true)
                                    .setDefaults(Notification.DEFAULT_ALL)
                                    .setWhen(System.currentTimeMillis())
                                    .setSmallIcon(R.mipmap.beyik_yol_icon)
                                    .setContentTitle(title)
                                    .setContentText(body)
                                    .setContentInfo("Info");

                            notificationManager.notify(new Random().nextInt(), notificationBuilder.build());

                        }
                super.onLoadFailed(errorDrawable);
            }
        });

    }

    public static Bitmap getBitmapFromURL(String src) {
        try {
            URL url = new URL(src);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setDoInput(true);
            connection.connect();
            InputStream input = connection.getInputStream();
            Bitmap myBitmap = BitmapFactory.decodeStream(input);
            return myBitmap;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    } // Author: silentnuke

    @Override
    public void onNewToken(@NonNull String s) {
        super.onNewToken(s);

        Log.d("TOKENFIREBASE", s);
    }
}
