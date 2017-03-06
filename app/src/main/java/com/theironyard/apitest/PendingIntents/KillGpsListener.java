package com.theironyard.apitest.PendingIntents;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import com.theironyard.apitest.Entities.Station;
import com.theironyard.apitest.Location.GpsListener;
import java.util.ArrayList;
import java.util.Calendar;

public class KillGpsListener extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        System.out.println("OnRecieve in KillGpsListener");//////////////////////////
        ArrayList<Station> stations = intent.getParcelableArrayListExtra("stations");
        GpsListener gpsListener = new GpsListener(stations, context);


        //see how many "alarm windows" are open. If its 1 or less, turn off gps
        int howMany = howManyAlarmsSetForNow(stations);///////////////////////////
        System.out.println("--Program says there are "+howMany+ "alarm window(s) open right now");//////////////////////////
        //if (howManyAlarmsSetForNow(stations) < 2) {

        if (howMany < 2) {
            gpsListener.stopLocationConnection();
        }

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
