package com.theironyard.apitest;

import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.Location;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Vibrator;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.TextView;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.FusedLocationProviderApi;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.theironyard.apitest.AdapterStuff.CustomAdapter;
import com.theironyard.apitest.Database.DatabaseManager;
import com.theironyard.apitest.Database.SharedPrefManager;
import com.theironyard.apitest.Entities.Station;
import com.theironyard.apitest.Location.GpsListener;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class set_alarm extends AppCompatActivity implements View.OnClickListener, /*GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener, LocationListener,*/ CustomAdapter.ItemClickCallback {

    DatabaseManager databaseManager;
    private GoogleApiClient googleApiClient;
    private LocationRequest locationRequest;
    private FusedLocationProviderApi locationProvider = LocationServices.FusedLocationApi;
    private double currentLatitude;
    private double currentLongitude;
    TextView textViewLat;
    TextView textViewLong;
    Button button;
    //ListView listView;
    ArrayAdapter<String> alarms;
    TextView textRing;
    ArrayList<Station> stations;
    RecyclerView recyclerView;
    private CustomAdapter adapter;
    private SharedPrefManager sharedPrefManager;
    private  int recPosition;
    private Vibrator vibrator;
    GpsListener gpsListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_set_alarm);

        textViewLat = (TextView) findViewById(R.id.textViewlat);
        textViewLong = (TextView) findViewById(R.id.textViewlong);
        button = (Button) findViewById(R.id.button3);
        button.setOnClickListener(this);
        databaseManager = new DatabaseManager(this);
        sharedPrefManager = new SharedPrefManager(this);
        textRing = (TextView) findViewById(R.id.textRing);

        if (!sharedPrefManager.checkOpenedBefore()){
            Intent intent = new Intent(getApplicationContext(), com.theironyard.apitest.MainActivity.class);
            startActivity(intent);
        }

        ///RecyclerView set up
        recyclerView = (RecyclerView) findViewById(R.id.recycleView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));


        //Use array to populate recycle view
        stations = sharedPrefManager.populateArrayFromStoredMemory();


        adapter = new CustomAdapter(stations, this);
        recyclerView.setAdapter(adapter);

        //set recycle view interface
        adapter.setItemClickCallback(this);

        /*
        ///Location
        googleApiClient = new GoogleApiClient.Builder(this)
                .addApi(LocationServices.API)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .build();

        locationRequest = new LocationRequest();
        locationRequest.setInterval(5000);
        locationRequest.setFastestInterval(5000);
        locationRequest.setPriority(locationRequest.PRIORITY_HIGH_ACCURACY);
        //locationRequest.setPriority(locationRequest.PRIORITY_BALANCED_POWER_ACCURACY); //Doesn't work in emulator
        */
        ArrayList<Station> armedStations = new ArrayList<>();
        for (int i = 0; i < stations.size(); i++){
            if (stations.get(i).isChecked()){
                armedStations.add(stations.get(i));
            }
        }
        gpsListener = new GpsListener(armedStations, this);
        gpsListener.buildLocationConnection();

    }

    /* see if gps listener works
    @Override
    public void onConnected(@Nullable Bundle bundle) {
        requestLocationUpdates();
    }

    private void requestLocationUpdates() {
        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        LocationServices.FusedLocationApi.requestLocationUpdates(googleApiClient, locationRequest, this);
    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }

    @Override
    public void onLocationChanged(Location location) {
        currentLatitude = location.getLatitude();
        currentLongitude = location.getLongitude();
        textViewLat.setText("Latitude" + String.valueOf(currentLatitude));
        textViewLong.setText("Longitude"+ String.valueOf(currentLongitude));


        for (int i = 0; i < stations.size(); i++){
            if (isBetween(stations.get(i).getLat1(), currentLatitude, stations.get(i).getLat2())){
                if (isBetween(stations.get(i).getLong1(), currentLongitude, stations.get(i).getLong2())){
                    textRing.setText("THIS IS YOUR STATION!");
                    RingtoneManager ringtoneManager = new RingtoneManager(this);
                    Ringtone ring = ringtoneManager.getRingtone(this, stations.get(i).getRingtoneUri());
                    ring.play();

                    if (stations.get(i).isVibrate()){
                        long pattern[] = { 0, 100, 200, 300, 400 };
                        vibrator = (Vibrator) getSystemService(this.VIBRATOR_SERVICE);
                        vibrator.vibrate(pattern, 0);
                    }

                }
            }
        }

    } */

    @Override
    protected void onStart(){
        //googleApiClient.connect(); see if gps listener works
        gpsListener.getLocationConnection();
        super.onStart();

    }

    @Override
    protected void onPause() {
        super.onPause();
        //LocationServices.FusedLocationApi.removeLocationUpdates(googleApiClient,this); see if gps listener works
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (gpsListener.getGoogleApiClient().isConnected()){
            //requestLocationUpdates(); see if gps listener works
            gpsListener.onConnected(null);
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        sharedPrefManager.saveArraytoSharedPreferencesMemory(stations);
        //googleApiClient.disconnect(); see if gps listener works
        gpsListener.stopLocationConnection();
    }

    @Override
    public void onClick(View v){
        Intent intent = new Intent(getApplicationContext(), com.theironyard.apitest.save_alarm.class);
        startActivity(intent);
    }


    public boolean isBetween(double lat1, double current, double lat2 ){
        return lat1>lat2 ? (current < lat1 && current > lat2) : (current > lat1 && current < lat2);
    }

    @Override
    public void onItemDeleteButtonClick(int p) {
        sharedPrefManager.deleteAlarmFromSharedPreferencesMemory(stations.get(p));
        stations.remove(stations.get(p));
        sharedPrefManager.saveArraytoSharedPreferencesMemory(stations);

        adapter.notifyDataSetChanged();
    }

    @Override
    public void onSwitch(int p, boolean isChecked) {
        stations.get(p).setChecked(isChecked);
        if (isChecked && !gpsListener.getStations().contains(stations.get(p))) {
            gpsListener.getStations().add(stations.get(p));
        } else if (!isChecked){
            gpsListener.getStations().remove(stations.get(p));
        }
    }

    @Override
    public void onItemRingtoneButtonClick(int p) {
        Intent intent = new Intent(RingtoneManager.ACTION_RINGTONE_PICKER);
        intent.putExtra(RingtoneManager.EXTRA_RINGTONE_TITLE, "Select your ringtone:");
        intent.putExtra(RingtoneManager.EXTRA_RINGTONE_SHOW_SILENT, false);
        intent.putExtra(RingtoneManager.EXTRA_RINGTONE_SHOW_DEFAULT, true);
        intent.putExtra(RingtoneManager.EXTRA_RINGTONE_TYPE,RingtoneManager.TYPE_ALARM);
        this.startActivityForResult(intent,999);
        recPosition = p;

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent intent) {
            if (resultCode == RESULT_OK) {
                Uri uri = intent.getParcelableExtra(RingtoneManager.EXTRA_RINGTONE_PICKED_URI);
                if (uri != null) {
                    RingtoneManager ringtoneManager = new RingtoneManager(this);

                    stations.get(recPosition).setRingtoneUri(uri);
                    Ringtone ringtone = RingtoneManager.getRingtone(this, uri);
                    stations.get(recPosition).setRingtoneName(ringtone.getTitle(this));
                    adapter.notifyItemChanged(recPosition);
                }

            }

    }

    @Override
    public void onCheck(int p, boolean isChecked){
        stations.get(p).setVibrate(isChecked);
    }
}
