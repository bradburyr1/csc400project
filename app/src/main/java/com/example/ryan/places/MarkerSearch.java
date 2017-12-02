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

    public String title = "any";
    public String city = "any";
    public boolean comp = false;
    public boolean fun = false;

    public void starter(){
        //Log.d("HELLO*************", "Starter");
        fetch(title, city, comp, fun);
    }

    public void fetch(String title, String city, boolean comp, boolean fun) {
        //Log.d("HELLO*************", "Fetch");
        FetchMarkersTask fmt = new FetchMarkersTask();
        //move the user's search terms into the AsyncTask
        fmt.title = title;
        fmt.city = city;
        fmt.comp = comp;
        fmt.fun = fun;
        fmt.execute();
    }

    public class FetchMarkersTask extends AsyncTask<Void, Void, String> {

        //////////////////////////////////
        //these ip addresses are for testing with wampserver, will change to a better solution with google cloud
        /*final String ip_address = "192.168.1.19";//home
        final String project = "android_connect";
        final String file = "search.php";*/

        //And, finally, the better solution with google cloud.
        final String onlineURL = "https://csc-182021.appspot.com/";

        String title = "any";
        String city = "any";
        boolean comp = false;
        boolean fun = false;

        //for local:
        //String builtUri = "http://" + ip_address + "/" + project + "/" + file;

        //for gcp
        String builtUri = onlineURL;

        //@Override
        protected void onPostExecute(String result){
            //MapsActivity ma = new MapsActivity();
            SearchActivity sa = new SearchActivity();
                //Log.d("ONMAPREADY222222--", "ONMAPREADY");
                //ma.parseJSON(result);
                sa.acceptRes(result);
        }

        @Override
        protected String doInBackground(Void... params) {
            //Log.d("TEST_TEST_TEST", title);
            HttpURLConnection urlConnection = null;

            if(title.equals("")){
                title = "any";
            }
            if(city.equals("")) {
                city = "any";
            }
            title = title.toLowerCase();
            city = city.toLowerCase();

            builtUri += "?sport=" +
                    title + "&city=" + city + "&comp=" + comp + "&fun=" + fun;

            String response = "";
            Log.d("HELLOttttttttt", "doInBackground: " + builtUri);
            try {
                URL url = new URL(builtUri);
                urlConnection = (HttpURLConnection) url.openConnection();

                if (urlConnection.getResponseCode() == HttpURLConnection.HTTP_OK) {
                    BufferedReader input = new BufferedReader(new InputStreamReader(urlConnection.getInputStream()), 8192);
                    String jsonResp = null;
                    while ((jsonResp = input.readLine()) != null) {
                        response = response.concat(jsonResp);
                        //Log.d("HELLO*************", "While Loop");
                        Log.d("HELLO*************", "Response: " + response);
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
