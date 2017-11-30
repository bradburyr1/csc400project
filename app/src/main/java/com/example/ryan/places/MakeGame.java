package com.example.ryan.places;

import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

import static com.example.ryan.places.R.id.fun;

/**
 * Created by Ryan on 10/1/2017.
 */

public class MakeGame {
    public static String rst = "";//will contain the json string

    String sport = "";
    String city = "";
    String time = "";
    String address = "";
    String date = "";
    String comp = "";
    String lat = "";
    String lng = "";
    String uid = "";


    public void starter(){
        Log.d("HELLO*************", "Starter");
        fetch(sport, city, time, address, date, comp, lat, lng, uid);
    }

    public void fetch(String sport, String city, String time, String address, String date, String comp, String lat, String lng, String uid) {
        Log.d("HELLO*************", "Fetch");
        FetchMarkersTask fmt = new FetchMarkersTask();
        //move the user's search terms into the AsyncTask
        fmt.sport = sport;
        fmt.city = city;
        fmt.time = time;
        fmt.address = address;
        fmt.date = date;
        fmt.comp = comp;
        fmt.lat = lat;
        fmt.lng = lng;
        fmt.uid = uid;
        fmt.execute();
    }

    public class FetchMarkersTask extends AsyncTask<Void, Void, String> {

        //////////////////////////////////
        //these ip addresses are for testing with wampserver, will change to a better solution with google cloud
        /*final String ip_address = "192.168.1.19";//home
        final String project = "android_connect";
        final String file = "create_game.php";*/

        //And, finally, the better solution with google cloud.
        final String onlineURL = "https://csc-182021.appspot.com/create_game/";

        String sport = "";
        String city = "";
        String time = "";
        String address = "";
        String date = "";
        String comp = "";
        String lat = "";
        String lng = "";
        String uid = "";

        //for local:
        //String builtUri = "http://" + ip_address + "/" + project + "/" + file;

        //for gcp
        String builtUri = onlineURL;

        //@Override
        protected void onPostExecute(String result){
            /*if(rst.equals("Insertion successful")){
                Toast.makeText(this, "Game created", Toast.LENGTH_LONG).show();
            }*/
        }

        @Override
        protected String doInBackground(Void... params) {
            Log.d("TEST_TEST_TEST", sport);
            HttpURLConnection urlConnection = null;

            sport = sport.toLowerCase();
            city = city.toLowerCase();
            time = time.toLowerCase();
            address = address.toLowerCase();
            date = date.toLowerCase();

            builtUri += "?sport=" +
                    sport + "&city=" + city + "&time=" + time + "&address=" + address +
                    "&date=" + date + "&comp=" + comp + "&lat=" + lat + "&long=" + lng + "&uid=" + uid;

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
                        //Log.d("HELLO*************", "While Loop");
                        //Log.d("HELLO*************", "Response: " + response);
                        //Log.d("HELLO*************", "jsonResp: " + jsonResp);
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
