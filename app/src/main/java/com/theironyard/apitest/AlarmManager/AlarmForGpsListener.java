package com.theironyard.apitest.AlarmManager;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import com.theironyard.apitest.Entities.Station;
import com.theironyard.apitest.Location.GpsListener;
import com.theironyard.apitest.PendingIntents.StartGpsService;

import java.util.ArrayList;
import java.util.Calendar;


public class AlarmForGpsListener {

    AlarmManager alarmManager;
    Context context;
    Intent stationListenerIntent;
    PendingIntent pendingIntentStart;
    Intent killGpsListenerIntent;
    PendingIntent pendingIntentStop;

    public AlarmForGpsListener(Context context){
        this.context = context;
        stationListenerIntent = new Intent(context, com.theironyard.apitest.PendingIntents.StationListener.class);
        killGpsListenerIntent = new Intent(context, com.theironyard.apitest.PendingIntents.KillGpsListener.class);
        alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
    }




    public void setAlarmsToStartGpsListener(ArrayList<Station> stations){
        GpsListener gpsListener = new GpsListener(stations, context);
        gpsListener.stopLocationConnection();
        System.out.println("SetAlarmsToStartGpsListener in AlarmForGpsListener");//////////////////////////
        stationListenerIntent.putParcelableArrayListExtra("stations", stations);
        killGpsListenerIntent.putParcelableArrayListExtra("stations", stations);
        alarmManager.cancel(pendingIntentStart); ///kills all alarms
        alarmManager.cancel(pendingIntentStop); ///kills all alarms
        System.out.println("We cleared all alarms");/////////////////////////


        //Check to see if we are already in time range to start getting GPS coordinates. If so, start gps
        for (Station station : stations){
            if (isAlarmSetForToday(station) && isCurrentTimeInAlarmWindow(station)) {
                System.out.println("Program says we are in alarm window for station "+station.getStationName()+" at "+station.getHourOfDay()+":"+station.getMinute());

                /* */
                Intent service = new Intent(context, StartGpsService.class);
                service.putParcelableArrayListExtra("stations", stations);
                context.startService(service);
                /*
                if (gpsListener.getGoogleApiClient() == null) {
                    gpsListener.buildLocationConnection();
                }
                gpsListener.getLocationConnection();
                if (gpsListener.getGoogleApiClient().isConnected()){
                   gpsListener.onConnected(null);
                   System.out.println("GPS started up");
                }*/
            }
        }


        int n = 0;
        for (int i = 0; i < stations.size(); i++){
            System.out.println("This is Alarm Number: "+i+" in AlarmForGpsListener");/////////////////////////
            Calendar calendar = Calendar.getInstance();
            Calendar calendar2 = Calendar.getInstance();
            Calendar weekFromToday = Calendar.getInstance();
            weekFromToday.add(Calendar.MINUTE, 60*24*7);

            //find time associated with station
            calendar.set(Calendar.HOUR_OF_DAY, stations.get(i).getHourOfDay());
            calendar.set(Calendar.MINUTE, stations.get(i).getMinute());
            calendar2.set(Calendar.HOUR_OF_DAY, stations.get(i).getHourOfDay());
            calendar2.set(Calendar.MINUTE, stations.get(i).getMinute());


            if (stations.get(i).isSunday()){
                //set day
                calendar.set(Calendar.DAY_OF_WEEK, Calendar.SUNDAY);
                calendar2.set(Calendar.DAY_OF_WEEK, Calendar.SUNDAY);

                //create add and subtract 15, 30, or 60 minutes
                calendar.add(Calendar.MINUTE, -stations.get(i).getRadioChanged());
                calendar2.add(Calendar.MINUTE, stations.get(i).getRadioChanged());
                if(calendar.before(Calendar.getInstance())){
                    calendar.add(Calendar.MINUTE, 60*24*7);
                }
                if(calendar2.before(Calendar.getInstance())){
                    calendar2.add(Calendar.MINUTE, 60*24*7);
                }
                //create the 2 alarms and their associated intents
                System.out.println("For Alarm "+i+" an alarm was set on Sunday. ID: "+n+" Start: "+calendar.getTime()+" End: "+calendar2.getTime());
                pendingIntentStart = PendingIntent.getBroadcast(context, n, stationListenerIntent, PendingIntent.FLAG_CANCEL_CURRENT);
                pendingIntentStop = PendingIntent.getBroadcast(context, n++, killGpsListenerIntent, PendingIntent.FLAG_CANCEL_CURRENT);
                alarmManager.setRepeating(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(),1000*60*60*24*7, pendingIntentStart);
                alarmManager.setRepeating(AlarmManager.RTC_WAKEUP, calendar2.getTimeInMillis(),1000*60*60*24*7, pendingIntentStop);
                calendar.add(Calendar.MINUTE,   stations.get(i).getRadioChanged());
                calendar2.add(Calendar.MINUTE, -stations.get(i).getRadioChanged());
                if(calendar.before(weekFromToday)){
                    calendar.add(Calendar.MINUTE, -60 * 24 * 7);
                }
                if(calendar2.before(weekFromToday)) {
                    calendar2.add(Calendar.MINUTE, -60 * 24 * 7);
                }
            }

            if (stations.get(i).isMonday()){
                calendar.set(Calendar.DAY_OF_WEEK, Calendar.MONDAY);
                calendar2.set(Calendar.DAY_OF_WEEK, Calendar.MONDAY);
                calendar.add(Calendar.MINUTE, -stations.get(i).getRadioChanged());
                calendar2.add(Calendar.MINUTE, stations.get(i).getRadioChanged());
                if(calendar.before(Calendar.getInstance())){
                    calendar.add(Calendar.MINUTE, 60*24*7);
                }
                if(calendar2.before(Calendar.getInstance())){
                    calendar2.add(Calendar.MINUTE, 60*24*7);
                }
                System.out.println("For Alarm "+i+" an alarm was set on Monday. ID: "+n+" Start: "+calendar.getTime()+" End: "+calendar2.getTime());
                pendingIntentStart = PendingIntent.getBroadcast(context, n, stationListenerIntent, PendingIntent.FLAG_CANCEL_CURRENT);
                pendingIntentStop = PendingIntent.getBroadcast(context, n++, killGpsListenerIntent, PendingIntent.FLAG_CANCEL_CURRENT);
                alarmManager.setRepeating(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(),1000*60*60*24*7, pendingIntentStart);
                alarmManager.setRepeating(AlarmManager.RTC_WAKEUP, calendar2.getTimeInMillis(),1000*60*60*24*7, pendingIntentStop);
                calendar.add(Calendar.MINUTE,   stations.get(i).getRadioChanged());
                calendar2.add(Calendar.MINUTE, -stations.get(i).getRadioChanged());
                if(calendar.before(weekFromToday)) {
                    calendar.add(Calendar.MINUTE, -60 * 24 * 7);
                }
                if(calendar2.before(weekFromToday)) {
                    calendar2.add(Calendar.MINUTE, -60 * 24 * 7);
                }
            }

            if (stations.get(i).isTuesday()){
                calendar.set(Calendar.DAY_OF_WEEK, Calendar.TUESDAY);
                calendar2.set(Calendar.DAY_OF_WEEK, Calendar.TUESDAY);
                calendar.add(Calendar.MINUTE, -stations.get(i).getRadioChanged());
                calendar2.add(Calendar.MINUTE, stations.get(i).getRadioChanged());
                if(calendar.before(Calendar.getInstance())){
                    calendar.add(Calendar.MINUTE, 60*24*7);
                }
                if(calendar2.before(Calendar.getInstance())){
                    calendar2.add(Calendar.MINUTE, 60*24*7);
                }
                System.out.println("For Alarm "+i+" an alarm was set on Tuesday. ID: "+n+" Start: "+calendar.getTime()+" End: "+calendar2.getTime());
                pendingIntentStart = PendingIntent.getBroadcast(context, n, stationListenerIntent, PendingIntent.FLAG_CANCEL_CURRENT);
                pendingIntentStop = PendingIntent.getBroadcast(context, n++, killGpsListenerIntent, PendingIntent.FLAG_CANCEL_CURRENT);
                alarmManager.setRepeating(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(),1000*60*60*24*7, pendingIntentStart);
                alarmManager.setRepeating(AlarmManager.RTC_WAKEUP, calendar2.getTimeInMillis(),1000*60*60*24*7, pendingIntentStop);
                calendar.add(Calendar.MINUTE,   stations.get(i).getRadioChanged());
                calendar2.add(Calendar.MINUTE, -stations.get(i).getRadioChanged());
                if(calendar.before(weekFromToday)) {
                    calendar.add(Calendar.MINUTE, -60 * 24 * 7);
                }
                if(calendar2.before(weekFromToday)) {
                    calendar2.add(Calendar.MINUTE, -60 * 24 * 7);
                }
            }

            if (stations.get(i).isWednesday()){
                calendar.set(Calendar.DAY_OF_WEEK, Calendar.WEDNESDAY);
                calendar2.set(Calendar.DAY_OF_WEEK, Calendar.WEDNESDAY);
                calendar.add(Calendar.MINUTE, -stations.get(i).getRadioChanged());
                calendar2.add(Calendar.MINUTE, stations.get(i).getRadioChanged());
                if(calendar.before(Calendar.getInstance())) {
                    calendar.add(Calendar.MINUTE, 60*24*7);
                }
                if(calendar2.before(Calendar.getInstance())) {
                    calendar2.add(Calendar.MINUTE, 60*24*7);
                }
                System.out.println("For Alarm "+i+" an alarm was set on Wednesday. ID: "+n+" Start: "+calendar.getTime()+" End: "+calendar2.getTime());
                pendingIntentStart = PendingIntent.getBroadcast(context, n, stationListenerIntent, PendingIntent.FLAG_CANCEL_CURRENT);
                pendingIntentStop = PendingIntent.getBroadcast(context, n++, killGpsListenerIntent, PendingIntent.FLAG_CANCEL_CURRENT);
                alarmManager.setRepeating(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(),1000*60*60*24*7, pendingIntentStart);
                alarmManager.setRepeating(AlarmManager.RTC_WAKEUP, calendar2.getTimeInMillis(),1000*60*60*24*7, pendingIntentStop);
                calendar.add(Calendar.MINUTE,   stations.get(i).getRadioChanged());
                calendar2.add(Calendar.MINUTE, -stations.get(i).getRadioChanged());
                if(calendar.before(weekFromToday)) {
                    calendar.add(Calendar.MINUTE, -60 * 24 * 7);
                }
                if(calendar2.before(weekFromToday)) {
                    calendar2.add(Calendar.MINUTE, -60 * 24 * 7);
                }
            }

            if (stations.get(i).isThursday()){
                calendar.set(Calendar.DAY_OF_WEEK, Calendar.THURSDAY);
                calendar2.set(Calendar.DAY_OF_WEEK, Calendar.THURSDAY);
                calendar.add(Calendar.MINUTE, -stations.get(i).getRadioChanged());
                calendar2.add(Calendar.MINUTE, stations.get(i).getRadioChanged());
                if(calendar.before(Calendar.getInstance())) {
                    calendar.add(Calendar.MINUTE, 60*24*7);
                }
                if(calendar2.before(Calendar.getInstance())) {
                    calendar2.add(Calendar.MINUTE, 60*24*7);
                }
                System.out.println("For Alarm "+i+" an alarm was set on Thursday. ID: "+n+" Start: "+calendar.getTime()+" End: "+calendar2.getTime());
                pendingIntentStart = PendingIntent.getBroadcast(context, n, stationListenerIntent, PendingIntent.FLAG_CANCEL_CURRENT);
                pendingIntentStop = PendingIntent.getBroadcast(context, n++, killGpsListenerIntent, PendingIntent.FLAG_CANCEL_CURRENT);
                alarmManager.setRepeating(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(),1000*60*60*24*7, pendingIntentStart);
                alarmManager.setRepeating(AlarmManager.RTC_WAKEUP, calendar2.getTimeInMillis(),1000*60*60*24*7, pendingIntentStop);
                calendar.add(Calendar.MINUTE,   stations.get(i).getRadioChanged());
                calendar2.add(Calendar.MINUTE, -stations.get(i).getRadioChanged());
                if(calendar.before(weekFromToday)) {
                    calendar.add(Calendar.MINUTE, -60 * 24 * 7);
                }
                if(calendar2.before(weekFromToday)) {
                    calendar2.add(Calendar.MINUTE, -60 * 24 * 7);
                }
            }

            if (stations.get(i).isFriday()){
                calendar.set(Calendar.DAY_OF_WEEK, Calendar.FRIDAY);
                calendar2.set(Calendar.DAY_OF_WEEK, Calendar.FRIDAY);
                calendar.add(Calendar.MINUTE, -stations.get(i).getRadioChanged());
                calendar2.add(Calendar.MINUTE, stations.get(i).getRadioChanged());
                if(calendar.before(Calendar.getInstance())){
                    calendar.add(Calendar.MINUTE, 60*24*7);
                }
                if(calendar2.before(Calendar.getInstance())){
                    calendar2.add(Calendar.MINUTE, 60*24*7);
                }
                System.out.println("For Alarm "+i+" an alarm was set on Friday. ID: "+n+" Start: "+calendar.getTime()+" End: "+calendar2.getTime());
                pendingIntentStart = PendingIntent.getBroadcast(context, n, stationListenerIntent, PendingIntent.FLAG_CANCEL_CURRENT);
                pendingIntentStop = PendingIntent.getBroadcast(context, n++, killGpsListenerIntent, PendingIntent.FLAG_CANCEL_CURRENT);
                alarmManager.setRepeating(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(),1000*60*60*24*7, pendingIntentStart);
                alarmManager.setRepeating(AlarmManager.RTC_WAKEUP, calendar2.getTimeInMillis(),1000*60*60*24*7, pendingIntentStop);
                calendar.add(Calendar.MINUTE,   stations.get(i).getRadioChanged());
                calendar2.add(Calendar.MINUTE, -stations.get(i).getRadioChanged());
                if(calendar.before(weekFromToday)) {
                    calendar.add(Calendar.MINUTE, -60 * 24 * 7);
                }
                if(calendar2.before(weekFromToday)) {
                    calendar2.add(Calendar.MINUTE, -60 * 24 * 7);
                }
            }

            if (stations.get(i).isSaturday()){
                calendar.set(Calendar.DAY_OF_WEEK, Calendar.SATURDAY);
                calendar2.set(Calendar.DAY_OF_WEEK, Calendar.SATURDAY);
                calendar.add(Calendar.MINUTE, -stations.get(i).getRadioChanged());
                calendar2.add(Calendar.MINUTE, stations.get(i).getRadioChanged());
                if(calendar.before(Calendar.getInstance())){
                    calendar.add(Calendar.MINUTE, 60*24*7);
                }
                if(calendar2.before(Calendar.getInstance())){
                    calendar2.add(Calendar.MINUTE, 60*24*7);
                }
                System.out.println("For Alarm "+i+" an alarm was set on Saturday. ID: "+n+" Start: "+calendar.getTime()+" End: "+calendar2.getTime());
                pendingIntentStart = PendingIntent.getBroadcast(context, n, stationListenerIntent, PendingIntent.FLAG_CANCEL_CURRENT);
                pendingIntentStop = PendingIntent.getBroadcast(context, n++, killGpsListenerIntent, PendingIntent.FLAG_CANCEL_CURRENT);
                alarmManager.setRepeating(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(),1000*60*60*24*7, pendingIntentStart);
                alarmManager.setRepeating(AlarmManager.RTC_WAKEUP, calendar2.getTimeInMillis(),1000*60*60*24*7, pendingIntentStop);
                calendar.add(Calendar.MINUTE,   stations.get(i).getRadioChanged());
                calendar2.add(Calendar.MINUTE, -stations.get(i).getRadioChanged());
                if(calendar.before(weekFromToday)) {
                    calendar.add(Calendar.MINUTE, -60 * 24 * 7);
                }
                if(calendar2.before(weekFromToday)) {
                    calendar2.add(Calendar.MINUTE, -60 * 24 * 7);
                }
            }

        }






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
