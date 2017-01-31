package com.theironyard.apitest.Location;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;

import android.os.Bundle;
import android.os.Vibrator;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.FusedLocationProviderApi;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import com.theironyard.apitest.Entities.Station;

import java.util.ArrayList;


public class GpsListener implements GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener, LocationListener {

    private GoogleApiClient googleApiClient;
    private LocationRequest locationRequest;
    private FusedLocationProviderApi locationProvider = LocationServices.FusedLocationApi;
    private Context context;
    private ArrayList<Station> stations;
    private double currentLatitude;
    private double currentLongitude;
    private static boolean suspend = false;
    private static double suspendLat1;
    private static double suspendLong1;
    private static double suspendLat2;
    private static double suspendLong2;

    public GpsListener (ArrayList<Station> stations, Context context){
        this.context = context;
        this.stations = stations;

    }

    public void buildLocationConnection() {
        googleApiClient = new GoogleApiClient.Builder(context)
                .addApi(LocationServices.API)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .build();

        locationRequest = new LocationRequest();
        locationRequest.setInterval(5000);
        locationRequest.setFastestInterval(5000);
        locationRequest.setPriority(locationRequest.PRIORITY_HIGH_ACCURACY);
    }

    public void getLocationConnection() {
        googleApiClient.connect();
    }

    public void stopLocationConnection() {
        googleApiClient.disconnect();
    }



    @Override
    public void onConnected(@Nullable Bundle bundle) {
        requestLocationUpdates();
    }

    private void requestLocationUpdates() {
        if (ActivityCompat.checkSelfPermission(context, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(context, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        //LocationServices.FusedLocationApi.requestLocationUpdates(googleApiClient, locationRequest, this);
        LocationServices.FusedLocationApi.requestLocationUpdates(googleApiClient, locationRequest, this);
    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onLocationChanged(Location location) {
        currentLatitude = location.getLatitude();
        currentLongitude = location.getLongitude();

        if (suspend) {
            if (!isBetween(suspendLat1, currentLatitude, suspendLat2) || !isBetween(suspendLong1, currentLongitude, suspendLong2)) {
                suspend = false;

            }
        } else {
            for (int i = 0; i < stations.size(); i++) {
                if (isBetween(stations.get(i).getLat1(), currentLatitude, stations.get(i).getLat2())) {
                    if (isBetween(stations.get(i).getLong1(), currentLongitude, stations.get(i).getLong2())) {

                        suspend = true;
                        suspendLat1 = stations.get(i).getLat1();
                        suspendLong1 = stations.get(i).getLong1();
                        suspendLat2 = stations.get(i).getLat2();
                        suspendLong2 = stations.get(i).getLong2();

                        Intent intent = new Intent(context, com.theironyard.apitest.alarm_goes_off.class);
                        intent.putExtra("stationName", stations.get(i).getStationName());
                        intent.putExtra("ringtone", stations.get(i).getRingtoneUri().toString());
                        intent.putExtra("vibrate", stations.get(i).isVibrate());
                        context.startActivity(intent);

                    }
                }
            }
        }
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }

    public boolean isBetween(double lat1, double current, double lat2 ){
        return lat1>lat2 ? (current < lat1 && current > lat2) : (current > lat1 && current < lat2);
    }

    public GoogleApiClient getGoogleApiClient() {
        return googleApiClient;
    }

    public ArrayList<Station> getStations() {
        return stations;
    }

    public void setStations(ArrayList<Station> stations) {
        this.stations = stations;
    }
}
