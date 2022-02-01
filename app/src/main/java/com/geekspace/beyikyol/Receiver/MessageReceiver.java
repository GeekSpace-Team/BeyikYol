package com.geekspace.beyikyol.Receiver;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.os.Handler;
import android.os.ResultReceiver;

import com.geekspace.beyikyol.Activity.App.MainMenu;

@SuppressLint("ParcelCreator")
public class MessageReceiver extends ResultReceiver {
    private MainMenu.Message message;
    public MessageReceiver(MainMenu.Message message){
        super(new Handler());
        this.message=message;
    }
    @Override
    protected void onReceiveResult(int resultCode, Bundle resultData){
        message.displayMessage(resultCode,resultData);
    }
}
