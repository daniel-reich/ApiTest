package com.theironyard.apitest.Database;

import android.content.Context;
import android.content.SharedPreferences;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import com.theironyard.apitest.Entities.Station;
import java.util.ArrayList;
import java.util.Calendar;

public class SharedPrefManager {

    private Context context;
    private DatabaseManager databaseManager;
    private RingtoneManager ringtoneManager;

    public SharedPrefManager(Context context){
        this.context = context;
        this.databaseManager = new DatabaseManager(context);
        this.ringtoneManager = new RingtoneManager(context);
    }


    public ArrayList<Station> populateArrayFromStoredMemory(){

        SharedPreferences setAlarms = context.getSharedPreferences("set_alarms",0);
        int numOfAlarms = setAlarms.getInt("numOfAlarms", 0);
        ArrayList<Station> stations = new ArrayList<>();

        for (int i = 0; i< numOfAlarms; i++){
            int stationId = setAlarms.getInt(String.valueOf(i),0);
            //Populate object with data from sqlite
            Station savedStation = databaseManager.getStationById(stationId);

            //set memory key to be used for shared preferences memory
            savedStation.setMemoryKey(i);

            //is alarm on or off - default is on (true) if no setting is saved
            savedStation.setChecked(setAlarms.getBoolean("isChecked" +String.valueOf(i), true));

            //is alarm set to vibrate - default is on (true)
            savedStation.setVibrate(setAlarms.getBoolean("isVibrate" +String.valueOf(i), true));

            //set alarm ringtone uri and alarm ringtone name
            //uri
            String stringUri = setAlarms.getString("ringtoneUri" + String.valueOf(i), ringtoneManager.getDefaultUri(RingtoneManager.TYPE_ALARM).toString());
            savedStation.setRingtoneUri(Uri.parse(stringUri));
            //name
            Ringtone ringtone = RingtoneManager.getRingtone(context, savedStation.getRingtoneUri());
            savedStation.setRingtoneName(ringtone.getTitle(context));

            //set days
            savedStation.setSunday(setAlarms.getBoolean("sunday" +String.valueOf(i), false));
            savedStation.setMonday(setAlarms.getBoolean("monday" +String.valueOf(i), true));
            savedStation.setTuesday(setAlarms.getBoolean("tuesday" +String.valueOf(i), true));
            savedStation.setWednesday(setAlarms.getBoolean("wednesday" +String.valueOf(i), true));
            savedStation.setThursday(setAlarms.getBoolean("thursday" +String.valueOf(i), true));
            savedStation.setFriday(setAlarms.getBoolean("friday" +String.valueOf(i), true));
            savedStation.setSaturday(setAlarms.getBoolean("saturday" +String.valueOf(i), false));
            savedStation.setHourOfDay(setAlarms.getInt("hour" +String.valueOf(i), Calendar.HOUR_OF_DAY));
            savedStation.setMinute(setAlarms.getInt("minute" +String.valueOf(i), Calendar.MINUTE));
            savedStation.setRadioChanged(setAlarms.getInt("radioChanged" +String.valueOf(i), 30));
            //save object in stations array
            stations.add(savedStation);
        }

        return stations;
    }

    public void saveArraytoSharedPreferencesMemory(ArrayList<Station> stations){
        SharedPreferences setAlarms = context.getSharedPreferences("set_alarms", 0);
        SharedPreferences.Editor editor = setAlarms.edit();

        for(int i = 0; i<stations.size(); i++){
            editor.putInt(String.valueOf(i), stations.get(i).getStationId());
            editor.putBoolean("isChecked" + String.valueOf(i), stations.get(i).isChecked());
            editor.putString("ringtoneUri" + String.valueOf(i), stations.get(i).getRingtoneUri().toString());
            editor.putBoolean("isVibrate" + String.valueOf(i), stations.get(i).isVibrate());
            editor.putBoolean("sunday" + String.valueOf(i), stations.get(i).isSunday());
            editor.putBoolean("monday" + String.valueOf(i), stations.get(i).isMonday());
            editor.putBoolean("tuesday" + String.valueOf(i), stations.get(i).isTuesday());
            editor.putBoolean("wednesday" + String.valueOf(i), stations.get(i).isWednesday());
            editor.putBoolean("thursday" + String.valueOf(i), stations.get(i).isThursday());
            editor.putBoolean("friday" + String.valueOf(i), stations.get(i).isFriday());
            editor.putBoolean("saturday" + String.valueOf(i), stations.get(i).isSaturday());
            editor.putInt("hour"+ String.valueOf(i), stations.get(i).getHourOfDay());
            editor.putInt("minute"+ String.valueOf(i), stations.get(i).getMinute());
            editor.putInt("radioChanged"+ String.valueOf(i), stations.get(i).getRadioChanged());
        }

        editor.putInt("numOfAlarms", stations.size());
        editor.commit();

    }

    public void addAlarmtoSharedPreferencesMemory(String line, String station, int hour, int minute, int radioChanged){
        SharedPreferences setAlarms = context.getSharedPreferences("set_alarms", 0);
        SharedPreferences.Editor editor = setAlarms.edit();
        int stationId = databaseManager.getStationIdByLineAndStationName(line, station);
        editor.putInt(String.valueOf(setAlarms.getInt("numOfAlarms", 0)), stationId);
        editor.putInt("hour" + setAlarms.getInt("numOfAlarms", 0), hour);
        editor.putInt("minute" + setAlarms.getInt("numOfAlarms", 0), minute);
        editor.putInt("radioChanged" + setAlarms.getInt("numOfAlarms", 0), radioChanged);
        editor.putInt("numOfAlarms", setAlarms.getInt("numOfAlarms", 0)+1);

        editor.commit();
    }


    public void deleteAlarmFromSharedPreferencesMemory(Station station){
        SharedPreferences setAlarms = context.getSharedPreferences("set_alarms" ,0);
        SharedPreferences.Editor editor = setAlarms.edit();

        editor.remove(String.valueOf(station.getMemoryKey()));
        editor.remove("isChecked"+String.valueOf(station.getMemoryKey()));
        editor.remove("ringtoneUri"+String.valueOf(station.getMemoryKey()));
        editor.remove("isVibrate"+String.valueOf(station.getMemoryKey()));
        editor.remove("sunday"+String.valueOf(station.getMemoryKey()));
        editor.remove("monday"+String.valueOf(station.getMemoryKey()));
        editor.remove("tuesday"+String.valueOf(station.getMemoryKey()));
        editor.remove("wednesday"+String.valueOf(station.getMemoryKey()));
        editor.remove("thursday"+String.valueOf(station.getMemoryKey()));
        editor.remove("friday"+String.valueOf(station.getMemoryKey()));
        editor.remove("saturday"+String.valueOf(station.getMemoryKey()));
        editor.remove("hour"+String.valueOf(station.getMemoryKey()));
        editor.remove("minute"+String.valueOf(station.getMemoryKey()));
        editor.remove("radioChanged"+String.valueOf(station.getMemoryKey()));
        ///remove time
        editor.putInt("numOfAlarms", setAlarms.getInt("numOfAlarms", 0)-1);
        editor.commit();
    }

    public void setOpenedBefore(){
        SharedPreferences setAlarms = context.getSharedPreferences("set_alarms" ,0);
        SharedPreferences.Editor editor = setAlarms.edit();
        editor.putBoolean("openedBefore", true);
        editor.commit();
    }

    public boolean checkOpenedBefore(){
        SharedPreferences setAlarms = context.getSharedPreferences("set_alarms" ,0);
        return setAlarms.getBoolean("openedBefore", false);
    }

}
