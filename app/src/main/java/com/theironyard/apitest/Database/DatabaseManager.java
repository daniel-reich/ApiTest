package com.theironyard.apitest.Database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.theironyard.apitest.Entities.Station;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class DatabaseManager extends SQLiteOpenHelper {

    public static final String database_name = "Stations.db";
    public static final String table_name = "Stations_table";
    public static final String col_1 = "station_id";
    public static final String col_2 = "station_name";
    public static final String col_3 = "station_address";
    public static final String col_4 = "station_line";
    public static final String col_5 = "lat1";
    public static final String col_6 = "long1";
    public static final String col_7 = "lat2";
    public static final String col_8 = "long2";

    public DatabaseManager(Context context) {
        super(context, database_name, null, 6);
        SQLiteDatabase db = this.getWritableDatabase();
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE " + table_name + "(station_id INTEGER, station_name TEXT, station_address TEXT, " +
                "station_line TEXT, lat1 REAL, long1 REAL, lat2 REAL, long2 REAL);");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + table_name);
        onCreate(db);
    }

    public void insertData(JSONObject json){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();

        try {
            JSONArray stationsArray = json.getJSONArray("stations");

            for (int i = 0; i<stationsArray.length(); i++){
                JSONObject station = stationsArray.getJSONObject(i);
                contentValues.put(col_1, station.getInt("station_id"));
                contentValues.put(col_2, station.getString("station_name"));
                contentValues.put(col_3, station.getString("station_address"));
                contentValues.put(col_4, station.getString("station_line"));
                contentValues.put(col_5, station.getDouble("lat1"));
                contentValues.put(col_6, station.getDouble("long1"));
                contentValues.put(col_7, station.getDouble("lat2"));
                contentValues.put(col_8, station.getDouble("long2"));
                long result = db.insert(table_name, null, contentValues);
                System.out.println("DBResult: "+result);
            }
        } catch (JSONException e){
            System.out.println("JSON ERROR");
        }


    }

    public Cursor getStationNames(){
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor result = db.rawQuery("SELECT station_name FROM "+table_name+";",null);
        return result;
    }

    public Cursor getLines() {
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor result = db.rawQuery("SELECT DISTINCT station_line FROM " + table_name + ";", null);
        return result;
    }

    public Cursor getStationsOnLine(String line) {
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor result = db.rawQuery("SELECT station_name FROM " + table_name + " WHERE station_line ='"+line+"';", null);
        return result;
    }

    public Station getStationObjectFromLineAndName(String line, String name){
        SQLiteDatabase db = this.getWritableDatabase();
        name = name.replace("'","\'");
        Cursor result = db.rawQuery("SELECT * FROM " + table_name + " WHERE station_line ='"+line+"' AND station_name ='"+name+"';", null);
        Station newStation = new Station();
        result.moveToNext();
        newStation.setStationId(result.getInt(0));
        newStation.setStationName(result.getString(1));
        newStation.setStationAddress(result.getString(2));
        newStation.setStationLine(result.getString(3));
        newStation.setLat1(result.getDouble(4));
        newStation.setLong1(result.getDouble(5));
        newStation.setLat2(result.getDouble(6));
        newStation.setLong2(result.getDouble(7));

        return newStation;
    }

    public Station getStationById (int id){
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor result = db.rawQuery("SELECT * FROM " + table_name + " WHERE station_id ="+id+";", null);
        Station newStation = new Station();
        result.moveToNext();
        newStation.setStationId(result.getInt(0));
        newStation.setStationName(result.getString(1));
        newStation.setStationAddress(result.getString(2));
        newStation.setStationLine(result.getString(3));
        newStation.setLat1(result.getDouble(4));
        newStation.setLong1(result.getDouble(5));
        newStation.setLat2(result.getDouble(6));
        newStation.setLong2(result.getDouble(7));

        return newStation;
    }

    public int getStationIdByLineAndStationName(String line, String name){
        name = name.replace("'","\'"); ///Maybe include this in the csv reader, that way processing power would be saved on mobile
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor result = db.rawQuery("SELECT station_id FROM " + table_name + " WHERE station_line ='"+line+"' AND station_name ='"+name+"';", null);

        result.moveToNext();
        return result.getInt(0);
    }
}
