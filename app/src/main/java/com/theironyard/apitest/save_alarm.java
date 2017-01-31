package com.theironyard.apitest;

import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.Spinner;

import com.theironyard.apitest.Database.DatabaseManager;
import com.theironyard.apitest.Database.SharedPrefManager;

import java.util.ArrayList;

public class save_alarm extends AppCompatActivity implements View.OnClickListener {

    Spinner spinner2;
    Spinner spinner3;
    DatabaseManager databaseManager;
    Button button;
    SharedPrefManager sharedPrefManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_save_alarm);

        spinner2 = (Spinner) findViewById(R.id.spinner2);
        spinner3 = (Spinner) findViewById(R.id.spinner3);
        button = (Button) findViewById(R.id.button2);
        button.setOnClickListener(this);
        sharedPrefManager = new SharedPrefManager(this);

        databaseManager = new DatabaseManager(this);
        Cursor results = databaseManager.getLines();
        ArrayList<String> linesArray = new ArrayList<String>();
        while (results.moveToNext()) {
            linesArray.add(results.getString(0));
        }
        ArrayAdapter<String> adapter2 = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, linesArray);
        spinner2.setAdapter(adapter2);




        String line = spinner2.getSelectedItem().toString();
        final ArrayList<String> stationArray = new ArrayList<String>();
        Cursor results2 = databaseManager.getStationsOnLine(line);


        while (results2.moveToNext()) {
            stationArray.add(results2.getString(0));
        }

        final ArrayAdapter<String> adapter3 = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, stationArray);
        spinner3.setAdapter(adapter3);
        spinner2.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                stationArray.clear();
                String line = spinner2.getSelectedItem().toString();

                Cursor results2 = databaseManager.getStationsOnLine(line);
                while (results2.moveToNext()) {
                    stationArray.add(results2.getString(0));
                }

                adapter3.notifyDataSetChanged();

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }

        });


    }

    @Override
    public void onClick(View v){

        //Intent
        sharedPrefManager.addAlarmtoSharedPreferencesMemory(spinner2.getSelectedItem().toString(), spinner3.getSelectedItem().toString());
        Intent setIntent = new Intent(getApplicationContext(), com.theironyard.apitest.set_alarm.class);
        startActivity(setIntent);
    }

}
