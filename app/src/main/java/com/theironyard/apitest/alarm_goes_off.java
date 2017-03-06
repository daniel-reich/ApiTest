package com.theironyard.apitest;

import android.content.Intent;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Vibrator;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.theironyard.apitest.Entities.Station;
import com.theironyard.apitest.Location.GpsListener;

import java.util.ArrayList;
import java.util.Calendar;

public class alarm_goes_off extends AppCompatActivity implements View.OnClickListener {

    private String stationName;
    private Uri ringtoneUri;
    private boolean vibrate;
    Button stopButton;
    TextView stationNameText;
    Vibrator vibrator;
    Ringtone ring;
    GpsListener gpsListener;
    ArrayList<Station> stations;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_alarm_goes_off);
        stations = getIntent().getParcelableArrayListExtra("stations");
        gpsListener = new GpsListener(stations, this);
        stationName = getIntent().getStringExtra("stationName");
        ringtoneUri = Uri.parse(getIntent().getStringExtra("ringtone"));
        vibrate = getIntent().getBooleanExtra("vibrate", true);

        stationNameText = (TextView) findViewById(R.id.stationNameText);
        stopButton = (Button) findViewById(R.id.stopButton);

        DisplayMetrics dm = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dm);

        int width = dm.widthPixels;
        int height = dm.heightPixels;

        getWindow().setLayout((int) (width*.8), (int) (height*.6));

        stationNameText.setText("Welcome to "+ stationName);
        stopButton.setOnClickListener(this);


        RingtoneManager ringtoneManager = new RingtoneManager(this);
        ring = ringtoneManager.getRingtone(this, ringtoneUri);
        ring.play();

        if (vibrate){
            long pattern[] = { 0, 100, 200, 300, 400 };
            vibrator = (Vibrator) getSystemService(this.VIBRATOR_SERVICE);
            vibrator.vibrate(pattern, 0);
        }


    }

    @Override
    public void onClick(View v) {
        ring.stop();
        if (vibrate) {
            vibrator.cancel();
        }
        getIntent().removeExtra("stationName");
        getIntent().removeExtra("ringtone");
        getIntent().removeExtra("vibrate");

        //see how many "alarm windows" are open. If its 1 or less, turn off gps
        int howMany = howManyAlarmsSetForNow(stations);///////////////////////////
        System.out.println("--Program says there are "+howMany+ "alarm window(s) open right now");//////////////////////////
        //if (howManyAlarmsSetForNow(stations) < 2) {

        if (howMany < 2) {
            gpsListener.stopLocationConnection();
        }

        //Intent setIntent = new Intent(getApplicationContext(), com.theironyard.apitest.set_alarm.class);
        //startActivity(setIntent);
        finish();
    }







    public int howManyAlarmsSetForNow(ArrayList<Station> stations){
        int i = 0;
        for (Station station: stations){
            if (isAlarmSetForToday(station) && isCurrentTimeInAlarmWindow(station)) {
                i++;
            }
        }
        return i;
    }

    public boolean isCurrentTimeInAlarmWindow(Station station){
        Calendar rightNow = Calendar.getInstance();
        int hour = rightNow.get(Calendar.HOUR_OF_DAY);
        int minute = rightNow.get(Calendar.MINUTE);
        boolean result = false;
        if (hour*60+minute >= station.getHourOfDay()*60+station.getMinute()-station.getRadioChanged() &&
                hour*60+minute <= station.getHourOfDay()*60+station.getMinute()+station.getRadioChanged()){
            result = true;
        }
        return result;

    }

    public boolean isAlarmSetForToday(Station station){
        Calendar rightNow = Calendar.getInstance();
        int day = rightNow.get(Calendar.DAY_OF_WEEK);
        boolean result = false;
        if (day == 1 && station.isSunday()) {
            result = true;
        } else if (day == 2 && station.isMonday()) {
            result = true;
        } else if (day == 3 && station.isTuesday()) {
            result = true;
        } else if (day == 4 && station.isWednesday()) {
            result = true;
        } else if (day == 5 && station.isThursday()) {
            result = true;
        } else if (day == 6 && station.isFriday()) {
            result = true;
        } else if (day == 7 && station.isSaturday()) {
            result = true;
        }
        return result;
    }
}
