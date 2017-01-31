package com.theironyard.apitest.Database;

import android.content.Context;
import android.content.SharedPreferences;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import com.theironyard.apitest.Entities.Station;
import java.util.ArrayList;

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
        }

        editor.putInt("numOfAlarms", stations.size());
        editor.commit();

    }

    public void addAlarmtoSharedPreferencesMemory(String line, String station){
        SharedPreferences setAlarms = context.getSharedPreferences("set_alarms", 0);
        SharedPreferences.Editor editor = setAlarms.edit();
        int stationId = databaseManager.getStationIdByLineAndStationName(line, station);
        editor.putInt(String.valueOf(setAlarms.getInt("numOfAlarms", 0)), stationId);
        editor.putInt("numOfAlarms", setAlarms.getInt("numOfAlarms", 0)+1);
        editor.commit();
    }


    public void deleteAlarmFromSharedPreferencesMemory(Station station){
        SharedPreferences setAlarms = context.getSharedPreferences("set_alarms" ,0);
        SharedPreferences.Editor editor = setAlarms.edit();

        editor.remove(String.valueOf(station.getMemoryKey()));
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
