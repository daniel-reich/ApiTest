package com.theironyard.apitest;

import android.content.Intent;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import com.theironyard.apitest.AdapterStuff.CustomAdapter;
import com.theironyard.apitest.AlarmManager.AlarmForGpsListener;
import com.theironyard.apitest.Database.DatabaseManager;
import com.theironyard.apitest.Database.SharedPrefManager;
import com.theironyard.apitest.Entities.Station;
import com.theironyard.apitest.Location.GpsListener;
import java.util.ArrayList;


public class set_alarm extends AppCompatActivity implements View.OnClickListener, CustomAdapter.ItemClickCallback {

    DatabaseManager databaseManager;
    Button button;
    ArrayList<Station> stations;
    RecyclerView recyclerView;
    private CustomAdapter adapter;
    private SharedPrefManager sharedPrefManager;
    private  int recPosition;
    GpsListener gpsListener;
    AlarmForGpsListener alarmForGpsListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        System.out.println("OnCreate in SetAlarm");//////////////////////////
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_set_alarm);

        button = (Button) findViewById(R.id.button3);
        button.setOnClickListener(this);
        databaseManager = new DatabaseManager(this);
        sharedPrefManager = new SharedPrefManager(this);


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

        alarmForGpsListener = new AlarmForGpsListener(this);
        alarmForGpsListener.setAlarmsToStartGpsListener(getArmedStations(stations));

        /*
        gpsListener = new GpsListener(armedStations, this);
        gpsListener.buildLocationConnection(); */

    }

    @Override
    protected void onStart(){
        //gpsListener.getLocationConnection();
        super.onStart();

    }

    @Override
    protected void onPause() {
        super.onPause();
        //LocationServices.FusedLocationApi.removeLocationUpdates(googleApiClient,this); see if gps listener works
    }

    @Override
    protected void onResume() {
        System.out.println("OnResume in SetAlarm");//////////////////////////
        super.onResume();
       /* if (gpsListener.getGoogleApiClient().isConnected()){
            gpsListener.onConnected(null);
        } */
    }

    @Override
    protected void onStop() {
        super.onStop();
        sharedPrefManager.saveArraytoSharedPreferencesMemory(stations);
        //gpsListener.stopLocationConnection();
    }

    @Override
    public void onClick(View v){
        Intent intent = new Intent(getApplicationContext(), com.theironyard.apitest.save_alarm.class);
        startActivity(intent);
    }

    @Override
    public void onItemDeleteButtonClick(int p) {
        System.out.println("OnItemDeleteButton in SetAlarm");//////////////////////////
        sharedPrefManager.deleteAlarmFromSharedPreferencesMemory(stations.get(p));
        stations.remove(stations.get(p));
        sharedPrefManager.saveArraytoSharedPreferencesMemory(stations);
        adapter.notifyDataSetChanged();
        alarmForGpsListener.setAlarmsToStartGpsListener(getArmedStations(stations));
    }

    @Override
    public void onSwitch(int p, boolean isChecked) {
        System.out.println("OnSwitch in SetAlarm");//////////////////////////
        stations.get(p).setChecked(isChecked);
        alarmForGpsListener.setAlarmsToStartGpsListener(getArmedStations(stations));
    }

    @Override
    public void onItemRingtoneButtonClick(int p) {
        System.out.println("OnRingtoneButtonClick in SetAlarm");//////////////////////////
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
        System.out.println("OnAvtivityResult in SetAlarm");//////////////////////////
            if (resultCode == RESULT_OK) {
                Uri uri = intent.getParcelableExtra(RingtoneManager.EXTRA_RINGTONE_PICKED_URI);
                if (uri != null) {
                    RingtoneManager ringtoneManager = new RingtoneManager(this);

                    stations.get(recPosition).setRingtoneUri(uri);
                    Ringtone ringtone = RingtoneManager.getRingtone(this, uri);
                    stations.get(recPosition).setRingtoneName(ringtone.getTitle(this));
                    adapter.notifyItemChanged(recPosition);
                }
                alarmForGpsListener.setAlarmsToStartGpsListener(getArmedStations(stations));
            }

    }

    @Override
    public void onCheck(int p, boolean isChecked){
        System.out.println("OnCheck in SetAlarm");//////////////////////////
        stations.get(p).setVibrate(isChecked);
        alarmForGpsListener.setAlarmsToStartGpsListener(getArmedStations(stations));
    }

    @Override
    public void onDayOfWeekButtonToggle( int p, int day, boolean isChecked){
        System.out.println("OnDayOfWeekButtonToggle in SetAlarm");//////////////////////////
        if (day == 1){
            stations.get(p).setSunday(isChecked);
        } else if (day ==2 ) {
            stations.get(p).setMonday(isChecked);
        } else if (day == 3) {
            stations.get(p).setTuesday(isChecked);
        } else if (day == 4) {
            stations.get(p).setWednesday(isChecked);
        } else if (day == 5) {
            stations.get(p).setThursday(isChecked);
        } else if (day == 6) {
            stations.get(p).setFriday(isChecked);
        } else if (day == 7) {
            stations.get(p).setSaturday(isChecked);
        }

        alarmForGpsListener.setAlarmsToStartGpsListener(getArmedStations(stations));

    }

    @Override
    public void onPlusMinusRadioChange(int p, int tag, boolean isChecked) {
        System.out.println("OnPlusMinusRadioChange in SetAlarm");//////////////////////////
        if (isChecked){
            stations.get(p).setRadioChanged(tag);
        }

        alarmForGpsListener.setAlarmsToStartGpsListener(getArmedStations(stations));
    }

    public ArrayList<Station> getArmedStations(ArrayList <Station> stations){
        ArrayList<Station> armedStations = new ArrayList<>();
        for (int i = 0; i < stations.size(); i++){
            if (stations.get(i).isChecked()){
                armedStations.add(stations.get(i));
            }
        }
        return armedStations;
    }
}
