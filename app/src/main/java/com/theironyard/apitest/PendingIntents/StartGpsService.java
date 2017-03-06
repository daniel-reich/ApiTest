package com.theironyard.apitest.PendingIntents;

import android.app.IntentService;
import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.support.annotation.Nullable;

import com.theironyard.apitest.Entities.Station;
import com.theironyard.apitest.Location.GpsListener;

import java.util.ArrayList;

/**
 * Created by Daniel on 2/28/17.
 */

public class StartGpsService extends /*IntentService*/ Service {

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId){
        ArrayList<Station> stations = intent.getParcelableArrayListExtra("stations");
        GpsListener gpsListener = new GpsListener(stations, this);
        if (gpsListener.getGoogleApiClient() == null) {
            gpsListener.buildLocationConnection();
        }
        gpsListener.getLocationConnection();
        if (gpsListener.getGoogleApiClient().isConnected()){
            gpsListener.onConnected(null);
            System.out.println("GPS started up");
        }
        return START_REDELIVER_INTENT;
    }

    @Override
    public void onDestroy(){
        super.onDestroy();
    }

/*

     * Creates an IntentService.  Invoked by your subclass's constructor.
     *
      Used to name the worker thread, important only for debugging.

    public StartGpsService() {
        super("StartGpsService");
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        ArrayList<Station> stations = intent.getParcelableArrayListExtra("stations");
        GpsListener gpsListener = new GpsListener(stations, this);
        if (gpsListener.getGoogleApiClient() == null) {
            gpsListener.buildLocationConnection();
        }
        gpsListener.getLocationConnection();
        if (gpsListener.getGoogleApiClient().isConnected()){
            gpsListener.onConnected(null);
            System.out.println("GPS started up");
        }
    }

    */
}
