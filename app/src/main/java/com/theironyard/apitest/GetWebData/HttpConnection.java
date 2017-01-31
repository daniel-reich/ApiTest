package com.theironyard.apitest.GetWebData;

import android.os.AsyncTask;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

/**
 * Created by Daniel on 1/23/17.
 */

public class HttpConnection extends AsyncTask<String, String, String> {

    @Override
    protected String doInBackground(String[] urls){
        String outputBuilder = "";
        try {
            URL url = new URL(urls[0]);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");
            conn.setRequestProperty("Accept", "application/json");
            InputStreamReader isr = new InputStreamReader(conn.getInputStream());
            BufferedReader br = new BufferedReader(isr);
            //BufferedReader br = new BufferedReader(new InputStreamReader((conn.getInputStream())));
            String getData;

            while ((getData = br.readLine()) != null) {
                outputBuilder = outputBuilder.concat(getData);
            }
            conn.disconnect();
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return outputBuilder;
    }

}

