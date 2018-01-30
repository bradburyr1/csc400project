package com.example.ryan.places;

import android.os.AsyncTask;
import android.util.Log;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

import static android.os.Build.VERSION_CODES.M;
import static com.example.ryan.places.R.id.fun;

/**
 * Created by Ryan on 10/29/2017.
 */

/*
This is where latitude and longitude information is obtained from a google api based on address, using a background task
 */

public class GetLatAndLong {
    public String rst = "";//will contain the json string

    /*public String house = "";
    public String street = "";
    public String streetSuf = "";*/
    public String address = "";
    public String city = "";
    public String state = "";
    public String key = "";

    public void starter(){
        Log.d("HELLO*************", "Starter");
        fetch(address, city, state, key);
    }

    public void fetch(String address, String city, String state, String key) {
        Log.d("HELLO*************", "Fetch");
        FetchMarkersTask fmt = new FetchMarkersTask();
        //move the user's search terms into the AsyncTask
        /*fmt.house = house;
        fmt.street = street;
        fmt.streetSuf = streetSuf;*/
        fmt.address = address;
        fmt.city = city;
        fmt.state = state;
        fmt.key = key;
        fmt.execute();
    }

    public class FetchMarkersTask extends AsyncTask<Void, Void, String> {

        //////////////////////////////////
        //these ip addresses are for testing with wampserver, will change to a better solution with google cloud

        /*String house = "";
        String street = "";
        String streetSuf = "";*/
        String address = "";
        String city = "";
        String state = "";
        String key = "";

        String builtUri = "https://maps.googleapis.com/maps/api/geocode/json?address=";

        //@Override
        protected void onPostExecute(String result){
            //MapsActivity ma = new MapsActivity();
            create cr = new create();
            Log.d("ONMAPREADY222222--", "ONMAPREADY");
            try{
                cr.parseForLatAndLong(result);
            }
            catch(IOException e)
            {
                Log.d("IO******EXCEPTION", "(acceptRes) Exception: " + e);
            }
        }

        @Override
        protected String doInBackground(Void... params) {

            HttpURLConnection urlConnection = null;

            //builtUri += house + "+" + street + "+" + streetSuf + ",+" + city + ",+" + state + "&key=" + key;
            builtUri += address + ",+" + city + ",+" + state + "&key=" + key;

            String response = "";
            Log.d("HELLO*************", "doInBackground: " + builtUri);
            try {
                URL url = new URL(builtUri);
                urlConnection = (HttpURLConnection) url.openConnection();

                if (urlConnection.getResponseCode() == HttpURLConnection.HTTP_OK) {
                    BufferedReader input = new BufferedReader(new InputStreamReader(urlConnection.getInputStream()), 8192);
                    String jsonResp = null;
                    while ((jsonResp = input.readLine()) != null) {
                        response = response.concat(jsonResp);
                        //Log.d("HELLO*************", "Response: " + response);
                        //Log.d("HELLO*************", "jsonResp: " + jsonResp);
                    }
                    input.close();
                }
            } catch (IOException e) {
                Log.d("HELLO*************", "HERES YOUR DANG STRING", e);
            }

            ////////////
            //Log.d("JSON Line", response);
            rst = response;//store the json string
            ///////////
            return response;
        }

        //return response;
    }
    //////////////////////////////////
}

