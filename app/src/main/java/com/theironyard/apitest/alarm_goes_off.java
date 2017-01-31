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

public class alarm_goes_off extends AppCompatActivity implements View.OnClickListener {

    private String stationName;
    private Uri ringtoneUri;
    private boolean vibrate;
    Button stopButton;
    TextView stationNameText;
    Vibrator vibrator;
    Ringtone ring;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_alarm_goes_off);

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
        Intent setIntent = new Intent(getApplicationContext(), com.theironyard.apitest.set_alarm.class);
        startActivity(setIntent);
    }
}
