package com.geekspace.beyikyol.Activity.Car;

import android.Manifest;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.graphics.Typeface;
import android.net.wifi.p2p.WifiP2pManager;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.geekspace.beyikyol.R;
import com.geekspace.beyikyol.Receiver.WifiDirectReciever;

import org.jetbrains.annotations.NotNull;

public class ShareCar extends AppCompatActivity {
    WifiP2pManager.Channel channel;
    WifiP2pManager manager;
    private static final int SERVER_PORT = 11000;
    IntentFilter intentFilter;
    WifiDirectReciever receiver;
    TextView share_title;
    Button send,receive;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_share_car);

        Window window = getWindow();

        // clear FLAG_TRANSLUCENT_STATUS flag:
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);

        // add FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS flag to the window
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);

        // finally change the color
        window.setStatusBarColor(ContextCompat.getColor(ShareCar.this,R.color.first));

        Typeface Ping_medium = Typeface.createFromAsset(getAssets(), "fonts/Ping LCG Medium.otf");
        Typeface Ping_bold = Typeface.createFromAsset(getAssets(), "fonts/Ping LCG Bold.otf");
        Typeface Ping_regular = Typeface.createFromAsset(getAssets(), "fonts/Ping LCG Regular.otf");

        findViewById(R.id.back_btn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        send=findViewById(R.id.send);
        receive=findViewById(R.id.receive);
        share_title=findViewById(R.id.share_title);

        share_title.setTypeface(Ping_bold);
        receive.setTypeface(Ping_medium);
        send.setTypeface(Ping_medium);

        send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(ShareCar.this,SendCar.class));
            }
        });

        receive.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(ShareCar.this, ReciveCar.class));
            }
        });



//        intentFilter = new IntentFilter();
//        // Indicates a change in the Wi-Fi P2P status.
//        intentFilter.addAction(WifiP2pManager.WIFI_P2P_STATE_CHANGED_ACTION);
//
//        // Indicates a change in the list of available peers.
//        intentFilter.addAction(WifiP2pManager.WIFI_P2P_PEERS_CHANGED_ACTION);
//
//        // Indicates the state of Wi-Fi P2P connectivity has changed.
//        intentFilter.addAction(WifiP2pManager.WIFI_P2P_CONNECTION_CHANGED_ACTION);
//
//        // Indicates this device's details have changed.
//        intentFilter.addAction(WifiP2pManager.WIFI_P2P_THIS_DEVICE_CHANGED_ACTION);
//        manager = (WifiP2pManager) getSystemService(Context.WIFI_P2P_SERVICE);
//        channel = manager.initialize(this, getMainLooper(), null);
//
//        receiver = new WifiDirectReciever(manager, channel, this);
//
//        registerReceiver(receiver, intentFilter);
//
//
//        discover();


    }


    private void discover(){
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                    12);
        } else {
            manager.discoverPeers(channel, new WifiP2pManager.ActionListener() {

                @Override
                public void onSuccess() {
                    // Code for when the discovery initiation is successful goes here.
                    // No services have actually been discovered yet, so this method
                    // can often be left blank. Code for peer discovery goes in the
                    // onReceive method, detailed below.
                }

                @Override
                public void onFailure(int reasonCode) {
                    // Code for when the discovery initiation fails goes here.
                    // Alert the user that something went wrong.
                }
            });
        }
    }







    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull @NotNull String[] permissions, @NonNull @NotNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if(requestCode==12){
//            discover();
        }
    }

    /** register the BroadcastReceiver with the intent values to be matched */
    @Override
    public void onResume() {
        super.onResume();

//        registerReceiver(receiver, intentFilter);
    }

    @Override
    public void onPause() {
        super.onPause();
//        unregisterReceiver(receiver);
    }
}