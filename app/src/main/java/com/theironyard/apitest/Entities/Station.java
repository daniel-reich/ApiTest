package com.theironyard.apitest.Entities;

import android.net.Uri;

/**
 * Created by Daniel on 1/25/17.
 */

public class Station {
    private int stationId;
    private String stationName;
    private String stationAddress;
    private String stationLine;
    private double lat1;
    private double long1;
    private double lat2;
    private double long2;
    private int memoryKey;
    private boolean isChecked;
    private Uri ringtoneUri;
    private String ringtoneName;
    private boolean vibrate;

    public boolean isVibrate() {
        return vibrate;
    }

    public void setVibrate(boolean vibrate) {
        this.vibrate = vibrate;
    }

    public String getRingtoneName() {
        return ringtoneName;
    }

    public void setRingtoneName(String ringtoneName) {
        this.ringtoneName = ringtoneName;
    }

    public Uri getRingtoneUri() {
        return ringtoneUri;
    }

    public void setRingtoneUri(Uri uri) {
        this.ringtoneUri = uri;
    }

    public int getStationId() {
        return stationId;
    }

    public void setStationId(int stationId) {
        this.stationId = stationId;
    }

    public String getStationName() {
        return stationName;
    }

    public void setStationName(String stationName) {
        this.stationName = stationName;
    }

    public String getStationAddress() {
        return stationAddress;
    }

    public void setStationAddress(String stationAddress) {
        this.stationAddress = stationAddress;
    }

    public String getStationLine() {
        return stationLine;
    }

    public void setStationLine(String stationLine) {
        this.stationLine = stationLine;
    }

    public double getLat1() {
        return lat1;
    }

    public void setLat1(double lat1) {
        this.lat1 = lat1;
    }

    public double getLong1() {
        return long1;
    }

    public void setLong1(double long1) {
        this.long1 = long1;
    }

    public double getLat2() {
        return lat2;
    }

    public void setLat2(double lat2) {
        this.lat2 = lat2;
    }

    public double getLong2() {
        return long2;
    }

    public void setLong2(double long2) {
        this.long2 = long2;
    }

    public int getMemoryKey() {
        return memoryKey;
    }

    public void setMemoryKey(int memoryKey) {
        this.memoryKey = memoryKey;
    }

    public boolean isChecked() {
        return isChecked;
    }

    public void setChecked(boolean checked) {
        isChecked = checked;
    }
}
