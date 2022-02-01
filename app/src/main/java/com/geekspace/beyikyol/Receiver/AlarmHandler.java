package com.geekspace.beyikyol.Receiver;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;

public class AlarmHandler {
    private Context context;
    public AlarmHandler(Context context){
        this.context=context;
    }

    public void setAlarmManager(){
        Intent intent=new Intent(context, ExecutableService.class);
        PendingIntent sender=PendingIntent.getBroadcast(context,2,intent,0);
        AlarmManager an=(AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        if(an!=null) {
            long triggerAfter=100;
            long triggerEvery=100;
            an.setRepeating(AlarmManager.RTC_WAKEUP, triggerAfter, triggerEvery, sender);
        }
    }

    public void cancelAlarmManager(){
        Intent intent=new Intent(context,ExecutableService.class);
        PendingIntent sender=PendingIntent.getBroadcast(context,2,intent,0);
        AlarmManager an=(AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        if(an!=null){
            an.cancel(sender);
        }
    }
}
