package com.theironyard.apitest;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;
import com.theironyard.apitest.Database.DatabaseManager;
import com.theironyard.apitest.Database.SharedPrefManager;
import com.theironyard.apitest.GetWebData.HttpConnection;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.concurrent.ExecutionException;


public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    DatabaseManager databaseManager;
    TextView output;
    Button button;
    Spinner spinner;
    SharedPrefManager sharedPrefManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        button = (Button) findViewById(R.id.button);
        output = (TextView) findViewById(R.id.textView);
        button.setOnClickListener(this);


        databaseManager = new DatabaseManager(this);
        sharedPrefManager = new SharedPrefManager(this);


    }

    @Override
    public void onClick(View v) {


        String[] urls = {"https://gentle-retreat-49936.herokuapp.com/getData/?transitNetworkName=TRAX"};
        String data = null;
        try {
            data = new HttpConnection().execute(urls).get();

            //output.setText(data);
        } catch (InterruptedException e){
            output.setText("There was an error. (InterruptedException)");
        } catch (ExecutionException e){
            output.setText("There was an error. (ExecutionException)");
        }
        try {
            JSONObject jsonData = new JSONObject(data);
            databaseManager.insertData(jsonData);
        } catch (JSONException e){
            output.setText("There was an error. (JSONException)");
        }




        //Preferences Memorey reset
        /*SharedPreferences setAlarms = getSharedPreferences("set_alarms" ,0);
        setAlarms.edit().clear().commit();*/

        sharedPrefManager.setOpenedBefore();
        Intent intent = new Intent(getApplicationContext(), com.theironyard.apitest.set_alarm.class);
        startActivity(intent);


    }
}
