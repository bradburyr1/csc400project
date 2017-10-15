package com.example.ryan.places;

import android.os.AsyncTask;
import android.util.Log;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

/**
 * Created by Ryan on 10/1/2017.
 */

public class MarkerSearch {
    public String rst = "";//will contain the json string

    public void starter(){
        Log.d("HELLO*************", "Starter");
        fetch("Football");
    }

    public void fetch(String title) {
        Log.d("HELLO*************", "Fetch");
        FetchMarkersTask fmt = new FetchMarkersTask();
        fmt.title = title;
        fmt.execute();
    }

    public void pauseMarkers(){//Make the program wait for the JSON to finish parsing
        FetchMarkersTask fmt = new FetchMarkersTask();
        try {
            fmt.get(1500, TimeUnit.MILLISECONDS);
        }
        catch (InterruptedException i){
        }
        catch (ExecutionException e){
        }
        catch (TimeoutException t){
        }
    }

    public class FetchMarkersTask extends AsyncTask<Void, Void, String> {

        //////////////////////////////////
        //these ip addresses are for testing with wampserver, will change to a better solution with google cloud
        final String ip_address = "192.168.1.17";//home
        final String project = "android_connect";
        final String file = "search.php";

        String title = "";

        String builtUri = "http://" + ip_address + "/" + project + "/" + file + "?sport=";

        //@Override
        protected void onPostExecute(String result){
            //MapsActivity ma = new MapsActivity();
            SearchActivity sa = new SearchActivity();
                Log.d("ONMAPREADY222222--", "ONMAPREADY");
                //ma.parseJSON(result);
                sa.acceptRes(result);
        }

        @Override
        protected String doInBackground(Void... params) {
            Log.d("TEST_TEST_TEST", title);
            HttpURLConnection urlConnection = null;
            String response = "";
            builtUri += title;
            Log.d("HELLO*************", "doInBackground: " + builtUri);
            try {
                URL url = new URL(builtUri);
                urlConnection = (HttpURLConnection) url.openConnection();

                if (urlConnection.getResponseCode() == HttpURLConnection.HTTP_OK) {
                    BufferedReader input = new BufferedReader(new InputStreamReader(urlConnection.getInputStream()), 8192);
                    String jsonResp = null;
                    while ((jsonResp = input.readLine()) != null) {
                        response = response.concat(jsonResp);
                        Log.d("HELLO*************", "While Loop");
                        Log.d("HELLO*************", "Response: " + response);
                        Log.d("HELLO*************", "jsonResp: " + jsonResp);
                    }
                    input.close();
                }
            } catch (IOException e) {
                Log.d("HELLO*************", "HERES YOUR DANG STRING", e);
            }

            ////////////
            Log.d("JSON Line", response);
            rst = response;//store the json string
            ///////////
            return response;
        }

        //return response;
    }
    //////////////////////////////////
}
