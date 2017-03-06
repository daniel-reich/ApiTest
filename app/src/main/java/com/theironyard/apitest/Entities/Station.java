package com.theironyard.apitest.Entities;

import android.net.Uri;
import android.os.Parcel;
import android.os.Parcelable;

import java.util.Calendar;
import java.util.HashMap;

/**
 * Created by Daniel on 1/25/17.
 */

public class Station implements Parcelable {
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
    private boolean sunday;
    private boolean monday;
    private boolean tuesday;
    private boolean wednesday;
    private boolean thursday;
    private boolean friday;
    private boolean saturday;
    private int hourOfDay;
    private int minute;
    private int radioChanged;

    public Station(){
        super();
    }

    public Station(Parcel parcel){
        this.stationName = parcel.readString();
        this.stationAddress = parcel.readString();
        this.stationLine = parcel.readString();
        this.ringtoneName = parcel.readString();
        this.ringtoneUri = Uri.parse(parcel.readString());
        this.lat1 = parcel.readDouble();
        this.long1 = parcel.readDouble();
        this.lat2 = parcel.readDouble();
        this.long2 = parcel.readDouble();
        this.stationId = parcel.readInt();
        this.hourOfDay = parcel.readInt();
        this.minute = parcel.readInt();
        this.radioChanged = parcel.readInt();
        this.memoryKey = parcel.readInt();
        this.isChecked = (parcel.readByte() == 1);
        this.vibrate = (parcel.readByte() == 1);
        this.sunday = (parcel.readByte() == 1);
        this.monday = (parcel.readByte() == 1);
        this.tuesday = (parcel.readByte() == 1);
        this.wednesday = (parcel.readByte() == 1);
        this.thursday = (parcel.readByte() == 1);
        this.friday = (parcel.readByte() == 1);
        this.saturday = (parcel.readByte() == 1);

    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(stationName);
        dest.writeString(stationAddress);
        dest.writeString(stationLine);
        dest.writeString(ringtoneName);
        dest.writeString(ringtoneUri.toString());
        dest.writeDouble(lat1);
        dest.writeDouble(long1);
        dest.writeDouble(lat2);
        dest.writeDouble(long2);
        dest.writeInt(stationId);
        dest.writeInt(hourOfDay);
        dest.writeInt(minute);
        dest.writeInt(radioChanged);
        dest.writeInt(memoryKey);
        dest.writeByte((byte)(isChecked? 1:0));
        dest.writeByte((byte)(vibrate? 1:0));
        dest.writeByte((byte)(sunday? 1:0));
        dest.writeByte((byte)(monday? 1:0));
        dest.writeByte((byte)(tuesday? 1:0));
        dest.writeByte((byte)(wednesday? 1:0));
        dest.writeByte((byte)(thursday? 1:0));
        dest.writeByte((byte)(friday? 1:0));
        dest.writeByte((byte)(saturday? 1:0));
    }

    public static final Creator<Station> CREATOR = new Creator<Station>() {
        @Override
        public Station createFromParcel(Parcel source) {
            return new Station(source);
        }

        @Override
        public Station[] newArray(int size) {
            return new Station[size];
        }
    };

    public boolean isSunday() {
        return sunday;
    }

    public void setSunday(boolean sunday) {
        this.sunday = sunday;
    }

    public boolean isMonday() {
        return monday;
    }

    public void setMonday(boolean monday) {
        this.monday = monday;
    }

    public boolean isTuesday() {
        return tuesday;
    }

    public void setTuesday(boolean tuesday) {
        this.tuesday = tuesday;
    }

    public boolean isWednesday() {
        return wednesday;
    }

    public void setWednesday(boolean wednesday) {
        this.wednesday = wednesday;
    }

    public boolean isThursday() {
        return thursday;
    }

    public void setThursday(boolean thursday) {
        this.thursday = thursday;
    }

    public boolean isFriday() {
        return friday;
    }

    public void setFriday(boolean friday) {
        this.friday = friday;
    }

    public boolean isSaturday() {
        return saturday;
    }

    public void setSaturday(boolean saturday) {
        this.saturday = saturday;
    }

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

    public int getRadioChanged() {
        return radioChanged;
    }

    public void setRadioChanged(int radioChanged) {
        this.radioChanged = radioChanged;
    }

    public int getHourOfDay() { return hourOfDay; }

    public void setHourOfDay(int hourOfDay) {
        this.hourOfDay = hourOfDay;
    }

    public int getMinute() {
        return minute;
    }

    public void setMinute(int minute) {
        this.minute = minute;
    }
}
