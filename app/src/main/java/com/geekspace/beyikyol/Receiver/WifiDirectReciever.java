package com.geekspace.beyikyol.Receiver;

import android.Manifest;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.wifi.p2p.WifiP2pDevice;
import android.net.wifi.p2p.WifiP2pManager;
import android.util.Log;
import android.widget.Toast;

import androidx.core.app.ActivityCompat;

import com.geekspace.beyikyol.Activity.Car.SendCar;

import java.util.ArrayList;
import java.util.List;

public class WifiDirectReciever extends BroadcastReceiver {
    WifiP2pManager.Channel channel;
    WifiP2pManager manager;
    SendCar activity;
    Context context;
    private List<WifiP2pDevice> peers = new ArrayList<WifiP2pDevice>();

    public WifiDirectReciever(WifiP2pManager manager, WifiP2pManager.Channel channel, SendCar activity) {
        this.channel = channel;
        this.manager = manager;
        this.activity = activity;
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        String action = intent.getAction();
        this.context = context;
        if (WifiP2pManager.WIFI_P2P_STATE_CHANGED_ACTION.equals(action)) {
            // Determine if Wifi P2P mode is enabled or not, alert
            // the Activity.
            int state = intent.getIntExtra(WifiP2pManager.EXTRA_WIFI_STATE, -1);
            if (state == WifiP2pManager.WIFI_P2P_STATE_ENABLED) {
                Toast.makeText(context, "WIFI_P2P_STATE_ENABLED", Toast.LENGTH_SHORT).show();
//                activity.setIsWifiP2pEnabled(true);
            } else {
//                activity.setIsWifiP2pEnabled(false);
                Toast.makeText(context, "WIFI_P2P_STATE_DISABLED", Toast.LENGTH_SHORT).show();
            }
        } else if (WifiP2pManager.WIFI_P2P_PEERS_CHANGED_ACTION.equals(action)) {

            // Request available peers from the wifi p2p manager. This is an
            // asynchronous call and the calling activity is notified with a
            // callback on PeerListListener.onPeersAvailable()
            if (manager != null) {
                if (ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                    Toast.makeText(context, "ACCESS_FINE_LOCATION", Toast.LENGTH_SHORT).show();
                } else {
                    manager.requestPeers(channel, activity.peerListListener);
                }

            }
            Log.d("Share", "P2P peers changed");
            Toast.makeText(context, "WIFI_P2P_PEERS_CHANGED_ACTION", Toast.LENGTH_SHORT).show();

        } else if (WifiP2pManager.WIFI_P2P_CONNECTION_CHANGED_ACTION.equals(action)) {

            Toast.makeText(context, "WIFI_P2P_CONNECTION_CHANGED_ACTION", Toast.LENGTH_SHORT).show();
            // Connection state changed! We should probably do something about
            // that.

        } else if (WifiP2pManager.WIFI_P2P_THIS_DEVICE_CHANGED_ACTION.equals(action)) {
            Toast.makeText(context, "WIFI_P2P_THIS_DEVICE_CHANGED_ACTION", Toast.LENGTH_SHORT).show();
        }
    }





}
