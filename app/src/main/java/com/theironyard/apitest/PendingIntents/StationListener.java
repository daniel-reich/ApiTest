package com.theironyard.apitest.PendingIntents;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.theironyard.apitest.Entities.Station;
import com.theironyard.apitest.Location.GpsListener;

import java.util.ArrayList;


public class StationListener extends BroadcastReceiver {



    @Override
    public void onReceive(Context context, Intent intent) {
        System.out.println("OnRecieve in StationListener");//////////////////////////
        ArrayList<Station> stations = intent.getParcelableArrayListExtra("stations");
        GpsListener gpsListener = new GpsListener(stations, context);
        gpsListener.buildLocationConnection();
        gpsListener.getLocationConnection();
        if (gpsListener.getGoogleApiClient().isConnected()){
            gpsListener.onConnected(null);
        }
    }
}
